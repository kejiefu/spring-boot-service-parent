package project.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * @Auther kejiefu
 * @Date 2018/9/10 0010
 */
@Controller
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.save.path}")
    private String fileSavePath;

    private final static String BAR = "/";

    @RequestMapping(value = "/file/index", method = RequestMethod.GET)
    public String t1() {
        return "/file/index.html";
    }

    @RequestMapping("/check")
    public void check(HttpServletRequest request, HttpServletResponse response) {
        logger.info("check...");

        String action = request.getParameter("action");

        //保存文件的后缀
        String suffix = request.getParameter("suffix");

        if ("mergeChunks".equals(action)) {
            // 获得需要合并的目录
            String fileMd5 = request.getParameter("fileMd5");

            // 读取目录所有文件
            File f = new File(fileSavePath + BAR + fileMd5);
            File[] fileArray = f.listFiles(new FileFilter() {
                // 排除目录，只要文件
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return false;
                    }
                    return true;
                }
            });

            // 转成集合，便于排序
            List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
            // 从小到大排序
            Collections.sort(fileList, new Comparator<File>() {

                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                        return -1;
                    }
                    return 1;
                }

            });


            //保存文件的名称
            String fileName = fileSavePath + BAR + UUID.randomUUID().toString();

            // 新建保存文件
            File outputFile = new File(fileName + suffix);

            try {
                boolean createNewFileResult = outputFile.createNewFile();
                if (!createNewFileResult) {
                    logger.error("新建保存文件失败!" + outputFile.getName());
                }

                // 输出流
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                FileChannel outChannel = fileOutputStream.getChannel();

                // 合并
                FileChannel inChannel;
                for (File file : fileList) {
                    inChannel = new FileInputStream(file).getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    inChannel.close();

                    // 删除分片
                    boolean deleteResult = file.delete();
                    if (!deleteResult) {
                        logger.error("删除分片失败!" + file.getName());
                    }
                }

                // 关闭流
                fileOutputStream.close();
                outChannel.close();

                //删除文件夹
                File tempFile = new File(fileSavePath + BAR + fileMd5);
                if (tempFile.isDirectory() && tempFile.exists()) {
                    boolean deleteResult = tempFile.delete();
                    if (!deleteResult) {
                        logger.error("删除文件夹失败!" + tempFile.getName());
                    }
                }
            } catch (IOException e) {
                logger.error("", e);
            }

            logger.info("合并文件成功");

        } else if ("checkChunk".equals(action)) {
            // 校验文件是否已经上传并返回结果给前端

            // 文件唯一表示
            String fileMd5 = request.getParameter("fileMd5");
            // 当前分块下标
            String chunk = request.getParameter("chunk");
            // 当前分块大小
            String chunkSize = request.getParameter("chunkSize");

            // 找到分块文件
            File checkFile = new File(fileSavePath + BAR + fileMd5 + BAR + chunk);

            // 检查文件是否存在，且大小一致
            response.setContentType("text/html;charset=utf-8");
            try {
                Map<String, Integer> map = new HashMap();
                if (checkFile.exists() && checkFile.length() == Integer.parseInt((chunkSize))) {
                    map.put("ifExist", 1);
                } else {
                    map.put("ifExist", 0);
                }
                response.getWriter().write(JSONObject.toJSONString(map));
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    @RequestMapping("/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) {
        logger.info("upload...");

        // 1.创建DiskFileItemFactory对象，配置缓存用
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        // 2. 创建 ServletFileUpload对象
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

        // 3. 设置文件名称编码
        servletFileUpload.setHeaderEncoding("utf-8");

        // 4. 开始解析文件
        // 文件md5获取的字符串
        String fileMd5 = null;
        // 文件的索引
        String chunk = null;
        try {
            List<FileItem> items = servletFileUpload.parseRequest(new ServletRequestContext(request));
            for (FileItem fileItem : items) {
                if (fileItem.isFormField()) { // >> 普通数据
                    String fieldName = fileItem.getFieldName();
                    if ("info".equals(fieldName)) {
                        String info = fileItem.getString("utf-8");
                        System.out.println("info:" + info);
                    }
                    if ("fileMd5".equals(fieldName)) {
                        fileMd5 = fileItem.getString("utf-8");
                        System.out.println("fileMd5:" + fileMd5);
                    }
                    if ("chunk".equals(fieldName)) {
                        chunk = fileItem.getString("utf-8");
                        System.out.println("chunk:" + chunk);
                    }
                } else {
                    // 如果文件夹没有创建文件夹
                    File file = new File(fileSavePath + BAR + fileMd5);
                    if (!file.exists()) {
                        boolean result = file.mkdirs();
                        System.out.println(result);
                    }
                    // 保存文件
                    File chunkFile = new File(fileSavePath + BAR + fileMd5 + BAR + chunk);
                    FileUtils.copyInputStreamToFile(fileItem.getInputStream(), chunkFile);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}

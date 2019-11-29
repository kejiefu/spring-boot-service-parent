package project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


/**
 * @Auther kejiefu
 * @Date 2018/8/30
 */
@Controller
public class Mp4Controller {

    private static final Logger logger = LoggerFactory.getLogger(Mp4Controller.class);

    @RequestMapping(value = "/mp4/index", method = RequestMethod.GET)
    public String t1() {
        return "/mp4/index.html";
    }

    @RequestMapping(value = "/getMp4", method = RequestMethod.GET)
    public void getMp4(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) {
        //1.创建文件对象
        File f = new File(this.getClass().getClassLoader().getResource("").getPath() + "static/video/1.mp4");
        //2.获取文件名称
        String fileName = f.getName();
        //3.导出文件
        String agent = httpServletRequest.getHeader("User-Agent").toUpperCase();
        InputStream fis = null;
        OutputStream os = null;
        try {
            //4.获取输入流
            fis = new BufferedInputStream(new FileInputStream(f.getPath()));
            byte[] buffer;
            buffer = new byte[fis.available()];
            int result = fis.read(buffer);
            httpServletResponse.reset();
            //5.由于火狐和其他浏览器显示名称的方式不相同，需要进行不同的编码处理
            if (agent.contains("FIREFOX")) {//火狐浏览器
                httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO-8859-1"));
            } else {//其他浏览器
                httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            }
            //6.设置response编码
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.addHeader("Content-Length", "" + f.length());
            //设置输出文件类型
            httpServletResponse.setContentType("video/mpeg4");
            //7.获取response输出流
            os = httpServletResponse.getOutputStream();
            os.flush();
            //8.输出文件
            os.write(buffer);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            //关闭流
            try {
                if (fis != null) {
                    fis.close();
                }
                if (os != null) {
                    os.flush();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }


}

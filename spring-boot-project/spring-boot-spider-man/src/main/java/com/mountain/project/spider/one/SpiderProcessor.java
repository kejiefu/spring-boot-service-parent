package com.mountain.project.spider.one;

import com.mountain.project.spider.one.dao.ArticleDao;
import com.mountain.project.spider.one.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @Auther kejiefu
 * @Date 2019/1/18 0018
 */
@Service
public class SpiderProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpiderProcessor.class);

    private Site site = Site.me().setDomain("wufazhuce.com");

    @Autowired
    ArticleDao articleDao;

    @Override
    public void process(Page page) {
        String number =  page.getUrl().regex("\\d+").toString();
        String title = page.getHtml().$("div.comilla-cerrar").toString().replace("<div class=\"comilla-cerrar\">", "").replace("</div>", "");
        String content = page.getHtml().$("div.articulo-contenido").toString();
        try {
            Article article = new Article();
            article.setNumber(Integer.parseInt(number));
            article.setContent(content);
            article.setTitle(title);
            articleDao.insertArticle(article);
        } catch (Exception ex) {
            logger.error("addArticle:", ex);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


}

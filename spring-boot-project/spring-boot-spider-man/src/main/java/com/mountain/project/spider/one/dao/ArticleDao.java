package com.mountain.project.spider.one.dao;


import com.mountain.project.spider.one.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * @Auther kejiefu
 * @Date 2019/1/18 0018
 */

@Repository
public class ArticleDao {

    @Qualifier("readNamedParameterJdbcTemplate")
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateReadOnly;


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Integer insertArticle(Article article) {
        String sql = "insert into `one_spider`.`t_one_article`(`number`, `title`, `content`) values ( :number,:title, :content)";
        SqlParameterSource ps = new BeanPropertySqlParameterSource(article);
        return namedParameterJdbcTemplate.update(sql, ps);
    }
}

package com.mountain.project.spider.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
public class MultiDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.readwrite")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("readWriteDataSource")
    @ConfigurationProperties("spring.datasource.readwrite")
    public DataSource firstDataSource() {
        return firstDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.read")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "readDatasource")
    @ConfigurationProperties("spring.datasource.read")
    public DataSource secondDataSource() {
        return secondDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.elasticjob")
    public DataSourceProperties jobDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @Primary
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("readWriteDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "readJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(@Qualifier("readDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Primary
    public NamedParameterJdbcTemplate primaryNamedParameterJdbcTemplate(@Qualifier("readWriteDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "readNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate secondNamedParameterJdbcTemplate(@Qualifier("readDatasource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * 开启事务
     * @param prodDataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("readWriteDataSource") DataSource prodDataSource) {
        return new DataSourceTransactionManager(prodDataSource);
    }

    @Bean
    public PlatformTransactionManager secondTransactionManager(@Qualifier("readDatasource") DataSource sitDataSource) {
        return new DataSourceTransactionManager(sitDataSource);
    }

}


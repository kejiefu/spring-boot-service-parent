package com.mountain.elasticsearch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mountain.elasticsearch.model.User;
import com.mountain.elasticsearch.repository.UserRepository;
import com.mountain.elasticsearch.service.UserService;
import com.mountain.elasticsearch.utils.RandomUtils;
import com.mountain.elasticsearch.utils.SnowflakeIdUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther kejiefu
 * @Date 2020/1/2 0002
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 插入一条数据
     */
    public void insertUser() {
        User user = new User();
        user.setId("662676429309214648");
        user.setName("oiibhmm8");
        user.setAge(99);
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(user).build();
        String result = elasticsearchTemplate.index(indexQuery);
        System.out.println(result);
    }

    /**
     * 批量插入数据
     */
    public void batchInsertUser() {
        List<IndexQuery> queries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(String.valueOf(SnowflakeIdUtils.nextId()));
            user.setName(RandomUtils.genRandomString(8));
            user.setAge(i);
            IndexQuery indexQuery = new IndexQueryBuilder().withId(user.getId()).withVersion(7L).withObject(user).build();
            queries.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(queries);
    }

    /**
     * 查询单条数据
     */
    public void getUserById() {
        GetQuery getQuery = new GetQuery();
        getQuery.setId("662620937065070592");
        User user = elasticsearchTemplate.queryForObject(getQuery, User.class);
        System.out.println(JSONObject.toJSONString(user));
    }


    /**
     * 多条件查询单条数据
     */
    public void getUser() {
        Criteria criteria = new Criteria("id").is("662676429309214720");
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        User user = elasticsearchTemplate.queryForObject(criteriaQuery, User.class);
        System.out.println(JSONObject.toJSONString(user));


        Criteria criteria1 = new Criteria("name").is("oiibhmm8").and(new Criteria("age").is("20"));
        CriteriaQuery criteriaQuery1 = new CriteriaQuery(criteria1);
        User user1 = elasticsearchTemplate.queryForObject(criteriaQuery1, User.class);
        System.out.println(JSONObject.toJSONString(user1));
    }

    public void listUser() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", "oiibhmm8"))
                .withPageable(PageRequest.of(0, 10))
                .build();
        List<User> list = elasticsearchTemplate.queryForList(searchQuery, User.class);
        System.out.println(JSONObject.toJSONString(list));

        //Criteria criteria1 = new Criteria("name").is("oiibhmm8").and(new Criteria("age").is("20"));
        Criteria criteria1 = new Criteria("name").is("oiibhmm8");
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "age");
        orders.add(order);
        Sort sort = Sort.by(orders);
        PageRequest pageRequest = PageRequest.of(0, 10, sort);
        CriteriaQuery criteriaQuery1 = new CriteriaQuery(criteria1, pageRequest);
        List<User> list1 = elasticsearchTemplate.queryForList(criteriaQuery1, User.class);
        System.out.println(JSONObject.toJSONString(list1));

    }
}

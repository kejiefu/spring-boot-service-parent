package com.mountain.elasticsearch.repository;

import com.mountain.elasticsearch.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Auther kejiefu
 * @Date 2020/1/2 0002
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

}
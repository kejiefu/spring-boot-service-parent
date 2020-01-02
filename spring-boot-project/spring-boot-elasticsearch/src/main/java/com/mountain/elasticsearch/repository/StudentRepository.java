package com.mountain.elasticsearch.repository;


import com.mountain.elasticsearch.model.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends ElasticsearchRepository<Student, String> {

}
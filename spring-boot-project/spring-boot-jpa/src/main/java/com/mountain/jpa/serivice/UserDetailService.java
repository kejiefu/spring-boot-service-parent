package com.mountain.jpa.serivice;


import com.mountain.jpa.controller.vo.UserDetailVo;
import com.mountain.jpa.dao.entity.UserDetail;
import com.mountain.jpa.dao.repository.UserDetailRepository;
import com.mysql.cj.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailService  {

    @Resource
    private UserDetailRepository userDetailRepository;

    public Page<UserDetail> findByCondition(UserDetailVo userDetailVo, Pageable pageable){

        return userDetailRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            //equal 示例
            if (!StringUtils.isNullOrEmpty(userDetailVo.getIntroduction())){
                predicates.add(cb.equal(root.get("introduction"),userDetailVo.getIntroduction()));
            }
            //like 示例
            if (!StringUtils.isNullOrEmpty(userDetailVo.getRealName())){
                predicates.add(cb.like(root.get("realName"),"%"+userDetailVo.getRealName()+"%"));
            }
            //between 示例
            if (userDetailVo.getMinAge()!=null && userDetailVo.getMaxAge()!=null) {
                Predicate agePredicate = cb.between(root.get("age"), userDetailVo.getMinAge(), userDetailVo.getMaxAge());
                predicates.add(agePredicate);
            }
            //greaterThan 大于等于示例
            if (userDetailVo.getMinAge()!=null){
                predicates.add(cb.greaterThan(root.get("age"),userDetailVo.getMinAge()));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageable);

    }
}

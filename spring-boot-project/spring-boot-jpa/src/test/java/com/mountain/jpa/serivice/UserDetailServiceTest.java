package com.mountain.jpa.serivice;


import com.mountain.jpa.controller.vo.UserDetailVO;
import com.mountain.jpa.dao.entity.UserDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Auther kejiefu
 * @Date 2019/12/10 0010
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailServiceTest {
    @Resource
    private UserDetailService userDetailService;

    @Test
    public void testFindByCondition() {
        int page = 0, size = 10;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        UserDetailVO param = new UserDetailVO();
        //param.setIntroduction("程序员");
        param.setMinAge(10);
        param.setMaxAge(30);
        Page<UserDetail> page1 = userDetailService.findByCondition(param, pageable);
        for (UserDetail userDetail : page1) {
            System.out.println("userDetail: " + userDetail.toString());
        }
    }
}
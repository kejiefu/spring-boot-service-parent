package com.mountain.elasticsearch.service.impl;

import com.mountain.elasticsearch.model.Commodity;
import com.mountain.elasticsearch.repository.UserRepository;
import com.mountain.elasticsearch.service.CommodityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther kejiefu
 * @Date 2020/1/2 0002
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommodityServiceImplTest {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
        System.out.println(commodityService.count());
    }

    @Test
    public void testInsert() {
        Commodity commodity1 = new Commodity();
        commodity1.setSkuId("1501009001");
        commodity1.setName("原味切片面包（10片装）");
        commodity1.setCategory("101");
        commodity1.setPrice(880);
        commodity1.setBrand("良品铺子");
        commodityService.save(commodity1);

        Commodity commodity2 = new Commodity();
        commodity2.setSkuId("1501009002");
        commodity2.setName("原味切片面包（6片装）");
        commodity2.setCategory("101");
        commodity2.setPrice(680);
        commodity2.setBrand("良品铺子");
        commodityService.save(commodity2);

        Commodity commodity3 = new Commodity();
        commodity3.setSkuId("1501009004");
        commodity3.setName("元气吐司850g");
        commodity3.setCategory("101");
        commodity3.setPrice(120);
        commodity3.setBrand("百草味");
        commodityService.save(commodity3);


        Commodity commodity4 = new Commodity();
        commodity4.setSkuId("1501009005");
        commodity4.setName("元气吐司1850g");
        commodity4.setCategory("101");
        commodity4.setPrice(500);
        commodity4.setBrand("巧克力味");
        Commodity result = commodityService.save(commodity4);
        System.out.println(result);

    }

    @Test
    public void testDelete() {
        Commodity commodity = new Commodity();
        commodity.setSkuId("1501009002");
        commodityService.delete(commodity);
    }

    @Test
    public void testGetAll() {
        Iterable<Commodity> iterable = commodityService.getAll();
        iterable.forEach(e -> System.out.println(e.toString()));
    }

    @Test
    public void testGetByName() {
        List<Commodity> list = commodityService.getByName("面包");
        System.out.println(list);
    }

    @Test
    public void testPage() {
        Page<Commodity> page = commodityService.pageQuery(0, 10, "切片");
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getContent());
    }

}
package com.yhh;

import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import com.yhh.demo.entity.RoleDemo;
import com.yhh.demo.entity.UserDemo;
import com.yhh.demo.service.IRoleDemoService;
import com.yhh.demo.service.IUserDemoService;
import com.yhh.util.EsWrapper;

@SpringBootTest
class YhhJavaEsApplicationTests {

    @Autowired
    private IUserDemoService userService;
    
    @Autowired
    private IRoleDemoService roleService;
    
    @Test
    public void add() {
        UserDemo user = UserDemo.builder().id(4l).username("杨鸿昊4").age(18).email("xxx@162.com").desc("描述").build();
        System.out.println(userService.save(user)); 
        RoleDemo role = RoleDemo.builder().id(1l).roleName("超级管理员").build();
        System.out.println(roleService.save(role));
    }
    
    @Test
    void count() {
        System.out.println(userService.count());
    }
    
    @Test
    void countByWrapper() {
        System.out.println(userService.count(new EsWrapper().eq("id", 2l)));
    }
    
    @Test
    void list() {
        System.out.println(userService.list());
        System.out.println(roleService.list());
    }
    
    @Test
    void listByWrapper() {
        System.out.println(userService.list(new EsWrapper().like("email", 16).order("id", SortOrder.ASC)));
    }
    
    @Test
    void page() {
        System.out.println(userService.page(PageRequest.of(1, 1),true));
    } 
    
    @Test
    void pageByWrapper() {
        System.out.println(userService.page(PageRequest.of(1, 1)));
    }
}

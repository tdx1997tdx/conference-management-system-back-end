package com.sustech.conferenceSystem.service;
import com.sustech.conferenceSystem.dto.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void loginTest1(){
        User user=new User("tom");
        Map res=userService.loginService(user);
        System.out.println(res);
    }

    @Test
    public void registTest2(){
        User user=new User("tom");
        Map res=userService.registService(user);
        System.out.println(res);
    }
}
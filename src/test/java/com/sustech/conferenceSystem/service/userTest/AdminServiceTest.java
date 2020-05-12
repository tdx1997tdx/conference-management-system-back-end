package com.sustech.conferenceSystem.service.userTest;

import com.sustech.conferenceSystem.service.user.AdminService;
import io.swagger.models.auth.In;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Test
    public void adminSearchServiceTest(){
        Map map=adminService.adminSearchService("tom",1,10);
        List list=(List)map.get("list");
        Long total=(Long)map.get("total");
        Assert.assertEquals(total.intValue(),list.size());
    }
}

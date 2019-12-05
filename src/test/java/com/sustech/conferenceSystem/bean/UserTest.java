package com.sustech.conferenceSystem.bean;

import com.sustech.conferenceSystem.dto.User;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testAdd(){
        User u=new User("null");
        User.attributeFilter(u);
        assertEquals(null,u.getUsername());
    }

}

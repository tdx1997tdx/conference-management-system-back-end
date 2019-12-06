package com.sustech.conferenceSystem.util;

import com.sustech.conferenceSystem.dto.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testAdd(){
        User u=new User();
        u.setName("null");
        Filter.attributeFilter(u);
        assertEquals(null,u.getUsername());
    }
}

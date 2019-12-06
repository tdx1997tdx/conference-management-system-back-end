package com.sustech.conferenceSystem.util;

import com.sustech.conferenceSystem.dto.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testFilter(){
        User u=new User();
        u.setName("null");
        Filter.attributeFilter(u);
        assertEquals(null,u.getUsername());
    }

    @Test
    public void testToken(){
        String s=Token.getInstance().makeToken("11wewe");
        System.out.println(s);
    }
}

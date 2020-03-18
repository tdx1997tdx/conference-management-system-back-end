package com.sustech.conferenceSystem.bean;

import lombok.Data;

@Data
public class Authorization {
    private String userId;
    private String token;
    public boolean setAuthorization(String cookie){
        String[] cookies=cookie.split(";");
        String[] userIdParam=cookies[0].split("=");
        String[] tokenParam=cookies[1].split("=");
        System.out.println(userIdParam[0]);
        System.out.println(tokenParam[0]);
        if(!userIdParam[0].equals("user_id")||tokenParam[0].equals("token")){
            return false;
        }
        this.userId=userIdParam[1];
        this.token=tokenParam[1];
        return true;
    }
}

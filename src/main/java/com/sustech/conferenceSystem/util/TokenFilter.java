package com.sustech.conferenceSystem.util;

import com.sustech.conferenceSystem.bean.Authorization;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@WebFilter(filterName="TokenFilter",urlPatterns={"/*"})
public class TokenFilter implements Filter {
    //排除不拦截的url
    private static final String[] excludePathPatterns = { "/user/login","/user/regist"};
    @Resource
    RedisUtil redisUtil;

    @Override
    public void destroy() {
        System.out.println("--------------token过滤器销毁------------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("进入token过滤器");
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse res=(HttpServletResponse) response;
        //忽略预检请求
        if(!(req.getMethod().equals("GET")||req.getMethod().equals("POST"))){
            chain.doFilter(request, response);
            return;
        }
        // 获取请求url地址，不拦截excludePathPatterns中的url
        String url = req.getRequestURI();
        if (Arrays.asList(excludePathPatterns).contains(url)) {
            //放行，相当于第一种方法中LoginInterceptor返回值为true
            chain.doFilter(request, response);
            return;
        }
        String cookies=req.getHeader("Authorization");
        if (cookies==null){
            res.sendError(407,"Authorization中未检测到信息");
            return;
        }
        Authorization au=new Authorization();
        if(au.setAuthorization(cookies)){
            res.sendError(407,"Authorization中cookie信息格式不正确");
            return;
        }
        String CheckToken=(String) redisUtil.get(au.getUserId());
        if(CheckToken==null){
            res.sendError(407,"redis中找不到对应人物id的token数据");
            return;
        }
        if(!au.getToken().equals(CheckToken)){
            res.sendError(407,"token输入错误");
            return;
        }
        chain.doFilter(request, response);
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("--------------token过滤器初始化------------");
    }
}

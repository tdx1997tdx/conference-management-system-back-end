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
@WebFilter(filterName="PrecheckFilter",urlPatterns={"/*"})
public class PrecheckFilter implements Filter {
    //排除不拦截的url
    private static final String[] excludePathPatterns = { "/user/login","/user/regist"};
    @Resource
    RedisUtil redisUtil;

    @Override
    public void destroy() {
        System.out.println("--------------预检请求过滤器销毁------------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException,ServletException{
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with");
        httpResponse.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        chain.doFilter(request, response);
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("--------------预检请求过滤器初始化------------");
    }
}

package com.sustech.conferenceSystem.util;

import com.sustech.conferenceSystem.dto.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.sustech.conferenceSystem.service.inform.InformConstants.INFORM_TEST_ON;

@Component
@WebFilter(filterName="TokenFilter",urlPatterns={"/*"})
@Slf4j
public class TokenFilter implements Filter {
    //排除不拦截的url
    private static final String[] excludePathPatterns = { "/user/login","/user/regist"};
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse res=(HttpServletResponse) response;
        log.info("{}访问接口: {}",req.getHeader("Origin"),req.getRequestURI());
        //忽略预检请求
        if(!(req.getMethod().equals("GET")||req.getMethod().equals("POST"))||INFORM_TEST_ON){
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
        String SecWebSocketVersion = req.getHeader("Sec-WebSocket-Version");
        String cookies = req.getHeader("Authorization");
        if (SecWebSocketVersion != null){
            log.info("Sec-WebSocket-Version: ",SecWebSocketVersion);
            chain.doFilter(request, response);
            return;
        }

        if (cookies==null){
            res.sendError(407,"Authorization中未检测到信息");
            return;
        }
        Authorization au=new Authorization();
        boolean isOk=au.setAuthorization(cookies);
        log.info("请求的id={},token={}",au.getUserId(),au.getToken());
        if(!isOk){
            res.sendError(407,"Authorization中cookie信息格式不正确");
            return;
        }
//        if (!au.getToken().equals("abc123")) {
//            res.sendError(407,"token数据" + au.getToken());
//            return;
//        }
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
    public void init(FilterConfig arg0){

    }
}

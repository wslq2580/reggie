package com.cjb.demo.filter;

/*
* 检查用户是否完成登录,过滤器
*
* */

import com.alibaba.fastjson.JSON;
import com.cjb.demo.common.BaseContext;
import com.cjb.demo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher(); //路径匹配器

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;//向下转型
        HttpServletResponse response = (HttpServletResponse) servletResponse; //向下转型

        //获取本次请求的uri
        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}",requestURI);
        //不需要拦截的请求路径
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg"
        };

        //判断本次请求是否需要处理
         if(check(uris,requestURI)){
             log.info("放行请求：{}",requestURI);
             filterChain.doFilter(request,response); //放行
             return;
         }

         //判断登陆状态，若已登录，则放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，id为：{}",request.getSession().getAttribute("employee"));
            Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContext. setThreadLocal(empId);

            filterChain.doFilter(request,response);//放行
            return;
        }

        //4-2、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


        log.info("用户未登录");
        //检测到未登录，通过输出流向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


    }

    /*路径匹配*/
    public boolean check(String[] uris,String requestURI){
        for (String uri : uris) {
            boolean match = PATH_MATCHER.match(uri,requestURI);
            if(match){
                return true;
            }
        }
            return false;
    }
}

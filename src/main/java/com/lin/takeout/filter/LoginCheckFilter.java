package com.lin.takeout.filter;


import com.alibaba.fastjson.JSON;
import com.lin.takeout.common.Result;
import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    private static AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //静态页面
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/templates/backend/**",
                "/templates/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        boolean match = check(urls, request.getRequestURI());

        //静态路径可直接放行
        if (match){
            filterChain.doFilter(request,response);
            return;
        }

        //判断是否为员工登录
        if (request.getSession() != null){
            if (request.getSession().getAttribute("employee") != null){
                filterChain.doFilter(request,response);
                return;
            }
        }

        //用户未登录
        response.getWriter().write(JSON.toJSONString(Result.error("NOLOGIN")));
    }

    private boolean check(String[] urls,String relURL){
        for (String url:urls) {
            if (PATH_MATCHER.match(url,relURL)){
                return true;
            }
        }
        return false;
    }
}

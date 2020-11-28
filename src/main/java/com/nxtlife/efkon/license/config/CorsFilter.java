package com.nxtlife.efkon.license.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component

@Order(Ordered.HIGHEST_PRECEDENCE)

public class CorsFilter implements Filter {


    @Override

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)

            throws IOException, ServletException {

        final HttpServletResponse response = (HttpServletResponse) res;

        final HttpServletRequest request = (HttpServletRequest) req;


        response.setCharacterEncoding("UTF-8");

        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");

        response.setHeader("Access-Control-Allow-Headers",

                "Origin, X-Requested-With, Content-Type,isWeb, fileUrl, Accept, Authorization");

        response.setHeader("Access-Control-Max-Age", "3600");

        if (!request.getRequestURI().startsWith("/api/websocket")) {

            response.setHeader("Access-Control-Allow-Origin", "*");

        }

        if (request.getMethod().equals("OPTIONS")) {


        } else {

            chain.doFilter(req, res);

        }

    }


    @Override

    public void destroy() {

    }


    @Override

    public void init(FilterConfig config) throws ServletException {

    }

}

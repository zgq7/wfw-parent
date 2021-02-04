package com.wfw.framework.web.filter;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author liaonanzhou
 * @date 2021/2/3 15:38
 * @description
 */
public class WebApiFilter implements Filter, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(WebApiFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        long start = System.currentTimeMillis();
        Thread.currentThread().setName(UUID.randomUUID().toString());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("ip", request.getRemoteAddr());
        map.put("url", request.getRequestURL());
        map.put("type", request.getMethod());

        logger.info("{}", JSON.toJSONString(map));

        filterChain.doFilter(request, response);

        logger.info("消耗时间：{} ms", System.currentTimeMillis() - start);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("WebFilter init...");
    }
}

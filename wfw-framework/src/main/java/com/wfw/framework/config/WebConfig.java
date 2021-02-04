package com.wfw.framework.config;

import com.wfw.framework.web.filter.WebApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaonanzhou
 * @date 2021-02-03 15:46
 * @description
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<WebApiFilter> filterFilterRegistrationBean(WebApiFilter webApiFilter) {
        FilterRegistrationBean<WebApiFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        //拦截路径配置
        List<String> uriList = new ArrayList<>(10);
        uriList.add("/*");

        filterFilterRegistrationBean.setFilter(webApiFilter);
        filterFilterRegistrationBean.setEnabled(true);
        filterFilterRegistrationBean.setUrlPatterns(uriList);
        filterFilterRegistrationBean.setName("baseFilter");
        filterFilterRegistrationBean.setOrder(1);

        return filterFilterRegistrationBean;
    }

    @Bean
    public WebApiFilter webApiFilter() {
        return new WebApiFilter();
    }

}

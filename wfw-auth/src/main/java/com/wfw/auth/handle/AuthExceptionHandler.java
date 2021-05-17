package com.wfw.auth.handle;

import com.alibaba.fastjson.JSON;
import com.wfw.auth.helper.FilterHelper;
import com.wfw.framework.exception.ServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liaonanzhou
 * @date 2021/5/17 11:26
 * @description
 **/
//@Component
public class AuthExceptionHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);
        logger.info("登录失败");
        //设置状态码
        response.setStatus(500);
        response.setContentType("application/json;charset=UTF-8");
        //将 登录失败 信息打包成json格式返回
        FilterHelper.errorResponse(response,new ServiceException(exception.getMessage()));
    }
}

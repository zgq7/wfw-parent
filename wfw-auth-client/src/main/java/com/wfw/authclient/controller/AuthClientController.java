package com.wfw.authclient.controller;

import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth/client")
public class AuthClientController extends WebApiController {

    @RequestMapping(value = "/demo")
    public ResponseEntity<String> demo() {
        return response(WebApiResponse.ok());
    }
}

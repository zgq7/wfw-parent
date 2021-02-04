package com.wfw.web.controller;

import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liaonanzhou
 * @date 2021/2/3 15:54
 * @description
 */
@RestController
public class WebController extends WebApiController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @GetMapping
    public ResponseEntity<String> getSome() {
        logger.info("access info ...");
        return response(WebApiResponse.ok(RandomStringUtils.random(8)));
    }
}

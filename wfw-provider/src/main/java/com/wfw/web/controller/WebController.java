package com.wfw.web.controller;

import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liaonanzhou
 * @date 2021/2/3 15:54
 * @description
 */
@RestController
@RequestMapping(value = "/web")
public class WebController extends WebApiController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @GetMapping(value = "/getSome")
    public ResponseEntity<String> getSome() {
        logger.info("access info ...");
        long px = ThreadLocalRandom.current().nextLong(1000000000000L, 1000000000000L * 10);
        return response(WebApiResponse.ok(px));
    }
}

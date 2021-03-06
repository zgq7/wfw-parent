package com.wfw.feign.controller;

import com.wfw.feign.feign.WebFeign;
import com.wfw.framework.web.WebApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2019-08-26 16:06.
 *
 * @author zgq7
 */
@RestController
public class FeignController extends WebApiController {

    private static final Logger logger = LoggerFactory.getLogger(FeignController.class);

    private final WebFeign webFeign;

    public FeignController(WebFeign webFeign) {
        this.webFeign = webFeign;
    }

    @GetMapping(value = "/getSome")
    public ResponseEntity<String> getSome() {
        logger.info("access feign");
        return response(webFeign.getSome());
    }
}

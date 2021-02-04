package com.wfw.ribbon.controller;

import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created on 2019-08-26 16:06.
 *
 * @author zgq7
 */
@RestController
public class RibbonController extends WebApiController {

    private static final Logger logger = LoggerFactory.getLogger(RibbonController.class);
    private static final String URL_PATH = "http://WFW-PROVIDER/";

    private final RestTemplate restTemplate;

    public RibbonController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<String> getSome() {
        WebApiResponse<String> response = restTemplate.getForEntity(URL_PATH, WebApiResponse.class).getBody();
        logger.info("access ribbon");
        return response(response);
    }
}

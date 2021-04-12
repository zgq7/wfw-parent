package com.wfw.framework.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @Author zhugenqi
 * @Date 2021/2/3 15:21
 * @Description 所有controller 进行继承，集中处理相应层公共代码
 */
public class WebApiController {

    /**
     * 不过滤
     **/
    public <T> ResponseEntity<String> response(WebApiResponse<T> result) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(JSON.toJSONString(result));
    }

    /**
     * 按选择过滤
     **/
    public <T> ResponseEntity<String> response(WebApiResponse<T> result, SerializeFilter[] serializeFilters) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(JSON.toJSONString(result, serializeFilters));
    }

}

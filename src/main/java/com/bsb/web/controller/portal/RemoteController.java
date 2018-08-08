package com.bsb.web.controller.portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/remote")
public class RemoteController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/test")
    public String hello() {

        logger.info("测试远程debug");
        return "hello natapp";
    }
}

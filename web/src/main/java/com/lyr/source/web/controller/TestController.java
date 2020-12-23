package com.lyr.source.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LinYuRong
 * @Date 2020/12/23 15:40
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping()
    public String test() {
        return "Hello World";
    }
}

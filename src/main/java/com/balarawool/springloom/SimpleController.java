package com.balarawool.springloom;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/thread")
    public String thread() {
        return Thread.currentThread().toString();
    }
}

package com.springboot.springboot.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {

    @GetMapping("/")
    public String SayHello() {
        return "Hello World test";
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("testZ");
        return "Hell";
    }


}

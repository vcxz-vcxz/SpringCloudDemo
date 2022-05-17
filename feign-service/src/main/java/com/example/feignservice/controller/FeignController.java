package com.example.feignservice.controller;

import com.example.feignservice.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {
    @Autowired
    private DemoService demoService;

    @GetMapping("/testFeign")
    public String testFeign() {
        return demoService.testGet()+"testFeign";
    }
}

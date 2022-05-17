package com.example.feignservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value = "eureka-client")
public interface DemoService {
    @GetMapping("/demo/testGet")
    String testGet();
}

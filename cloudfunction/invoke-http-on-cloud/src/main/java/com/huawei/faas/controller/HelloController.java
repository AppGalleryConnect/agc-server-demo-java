package com.huawei.faas.controller;

import com.huawei.faas.service.HelloService;

import com.alibaba.fastjson.JSONObject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {

    @Resource
    HelloService helloService;

    @RequestMapping(path = "/invoke_function", method = RequestMethod.GET)
    public JSONObject invokeHttpFunctionOnCloud() {
        return helloService.invokeHttpFunctionOnCloudService();
    }
}
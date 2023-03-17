/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.faas.controller;

import com.alibaba.fastjson.JSONObject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping(path = "/invoke", method = RequestMethod.GET)
    public JSONObject invokeHttpFunctionOnCloud() {
        JSONObject res = new JSONObject();
        res.put("message", "ok");
        return res;
    }
}
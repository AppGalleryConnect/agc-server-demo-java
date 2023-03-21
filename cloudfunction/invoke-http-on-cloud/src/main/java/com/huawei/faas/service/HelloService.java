package com.huawei.faas.service;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.credential.CredentialService;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.commons.rest.HttpMethod;
import com.huawei.agconnect.server.commons.rest.RestResponse;
import com.huawei.agconnect.server.commons.util.StringUtils;
import com.huawei.agconnect.server.function.AGCFunction;
import com.huawei.agconnect.server.function.AGCFunctionException;
import com.huawei.agconnect.server.function.FunctionCallable;
import com.huawei.faas.utils.Constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HelloService {
    private static final FunctionCallable callable;

    /**
     * Initialize configurations of SDK(agconnect-function-server)
     */
    static {
        CredentialService credential = null;
        try {
            credential = CredentialParser.toCredential(Constants.AGC_API_CLIENT_KEY_PATH);
            AGCParameter agcParameter = AGCParameter.builder().setCredential(credential).build();
            AGCClient.initialize(agcParameter);
            AGCFunction function = AGCFunction.getInstance();
            // TODO: fill in the functionName and version
            callable = function.wrap("called", "$latest");
        } catch (AGCException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Using the SDK to Call CloudFunction
     *
     * @return JSONObject
     */
    public JSONObject invokeHttpFunctionOnCloudService() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        JSONObject jsonObject = JSON.parseObject("{}");
        RestResponse restResponse = null;
        try {
            // TODO: fill in the HttpMethod, url, headers and requestBody
            restResponse = callable.callHttpOnCloud(HttpMethod.POST, "/invoke", headers, jsonObject);
        } catch (AGCFunctionException e) {
            throw new RuntimeException(e);
        }
        JSONObject result = new JSONObject();
        result.put("data", restResponseToString(restResponse));
        return result;
    }

    /**
     * Transform the response of the called function
     *
     * @param restResponse rest response of called cloudFunction
     * @return JSONObject
     */
    private static JSONObject restResponseToString(RestResponse restResponse) {
        JSONObject jsonObject = new JSONObject();
        if (null != restResponse.getHttpStatus() && StringUtils.isNotBlank(restResponse.getHttpStatus().toString())) {
            jsonObject.put("HttpStatus", restResponse.getHttpStatus().toString());
        }
        if (null != restResponse.getHeader() && StringUtils.isNotBlank(restResponse.getHeader().toString())) {
            jsonObject.put("Header", restResponse.getHeader().toString());
        }
        if (null != restResponse.getContent() && StringUtils.isNotBlank(restResponse.getContent())) {
            jsonObject.put("Content", restResponse.getContent());
        }
        if (null != restResponse.getBody() && StringUtils.isNotBlank(restResponse.getBody().toString())) {
            jsonObject.put("Body", restResponse.getBody());
        }
        return jsonObject;
    }
}

/*
 * Copyright 2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package faas;

import com.alibaba.fastjson.JSONObject;
import com.huawei.wisecloud.wisefunction.runtime.CanonicalHttpTriggerResponse;
import com.huawei.wisecloud.wisefunction.runtime.Context;
import com.huawei.wisecloud.wisefunction.runtime.log.LogFactory;
import com.huawei.wisecloud.wisefunction.runtime.log.RunLog;

import java.util.HashMap;
import java.util.Map;

public class HelloPojo {
    /**
     * Describe the basic method of Cloud Functions
     *
     * @param request
     * @param context
     * @return HTTP response
     */
    public CanonicalHttpTriggerResponse handleRequest(JSONObject request, Context context) {
        // example of display environment variables
        String env1 = context.getClientContext().getEnvironments().get("env1");

        // example of display logs
        RunLog log = LogFactory.getRunLog();
        log.info("Test info log");
        log.warn("Test warn log");
        log.debug("Test debug log");
        log.error("Test error log");

        log.info("----------Start-----------");
        JSONObject result = new JSONObject();
        result.put("code", 0);
        try {
            long interval;
            long startTime = System.currentTimeMillis();

            // print input parameters and environment variables
            String req = request.getString("request");
            log.info("request : " + req + ", env1 : " + env1);

            long endTime = System.currentTimeMillis();
            interval = endTime - startTime;
            log.info("intervalTime: " + interval + "(ms)");
            result.put("intervalTime: ", interval + "(ms)");
            log.info("--------Finished---------");
        } catch (Exception e) {
            log.error("the ex is " + e.getMessage());
            result.put("msg", e.getMessage());
        }
        Map<String, String> header = new HashMap<>();
        header.put("faas-content-type", "json");
        header.put("res-type", "context.env");
        CanonicalHttpTriggerResponse resp = new CanonicalHttpTriggerResponse.Builder()
                .withContentType("application/json")
                .withHeaders(header)
                .base64Flag(false)
                .withStatusCode(200)
                .build();
        resp.setBody(result.toJSONString());
        return resp;
    }
}
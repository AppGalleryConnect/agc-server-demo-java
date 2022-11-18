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
package com.allinfinance.dev.ccp.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author qipeng
 * @date 2022/9/8 10:50
 * @desc
 */
@RestController
public class TestController {

    @GetMapping("/test/get/{name}")
    public String getMethodTest(@PathVariable("name") String name) {
        String result = "hello " + name + "!";
        System.out.println(result);
        return result;
    }

    @PostMapping(path = "/test/post/{message}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String postMethodTest(@PathVariable("message") String message, @RequestBody User user) {
        System.out.println("接收到请求：" + message + ", " + user);
        String result = "{\n" +
                "  \"data\": {\n" +
                "    \"totalRows\": 12,\n" +
                "    \"nextPageFlag\": true,\n" +
                "    \"firstRow\": 0,\n" +
                "    \"lastRow\": 9,\n" +
                "    \"interestDetails\": [\n" +
                "      {\n" +
                "        \"noDefInterest\": 0.0000,\n" +
                "        \"pastDefInterest\": 0.0000,\n" +
                "        \"currentDefInterest\": 0.0000,\n" +
                "        \"chargeType\": \"1\",\n" +
                "        \"firstCardNo\": \"6252180280003841\",\n" +
                "        \"mediumCardNo\": \"6252180280003841\",\n" +
                "        \"planType\": \"D\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"success\": true,\n" +
                "  \"errorStack\": []\n" +
                "}";
        return result;
    }
}

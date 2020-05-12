package com.a360inhands.qiniu.controller;

import com.a360inhands.qiniu.utils.JsonBuilder;
import com.a360inhands.qiniu.utils.TokenHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    private JsonBuilder getJsonBuilder(int status, String info) {
        return new JsonBuilder()
                .put("status", status)
                .put("info", info);
    }

    private JsonBuilder buildToken(String token) {
        return getJsonBuilder(200, "success").put("token", token);
    }

    private JsonBuilder BAD(String info) {
        return getJsonBuilder(400, info);
    }

    @RequestMapping("/get")
    public void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "Authentication");
        resp.setContentType("application/json;charset=utf-8");
//        String accessKey = req.getParameter("accessKey");
//        String secretKey = req.getParameter("secretKey");
        String bucket = req.getParameter("bucket");
        if (accessKey == null || accessKey.isEmpty() || secretKey == null || secretKey.isEmpty() || bucket == null || bucket.isEmpty()) {
            resp.getWriter().write(BAD("参数不能为空，参数：accessKey,secretKey,bucket").getJson());
            return;
        }
//        System.out.println(accessKey + "\n" + secretKey + "\n" + bucket);
        TokenHelper tokenHelper = TokenHelper.create(accessKey, secretKey);
        String token = tokenHelper.getToken(bucket);
        String json = buildToken(token).getJson();
//        System.out.println(json);
        resp.getWriter().write(json);

    }
}

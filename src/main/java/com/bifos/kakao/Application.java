package com.bifos.kakao;

import com.bifos.kakao.network.HttpClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();

        // test
        Map<String, Object> body = new HashMap<>();
        body.put("problem", "1");
        String result = httpClient.(BASE_URL + "/start", headers, body);
        System.out.println(result);
    }
}

package com.bifos.kakao;

import com.bifos.kakao.network.HttpClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Application {

    private static final String X_AUTH_TOKEN = "62cba2ec88c4796e8b6736fa4cf2de08";
    private static final String BASE_URL = "https://kox947ka1a.execute-api.ap-northeast-2.amazonaws.com/prod/users";

    public static void main(String[] args) {
        Gson gson = new Gson();
        HttpClient httpClient = new HttpClient(gson);

        // test
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Auth-Token", X_AUTH_TOKEN);

        Map<String, Object> body = new HashMap<>();
        body.put("problem", "1");
        String result = httpClient.send(BASE_URL + "/start", headers, body);
        System.out.println(result);
    }
}

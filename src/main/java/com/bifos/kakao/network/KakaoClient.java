package com.bifos.kakao.network;

import com.bifos.kakao.network.annotation.*;
import com.bifos.kakao.network.model.BaseResponse;
import com.bifos.kakao.network.model.StartModel;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class KakaoClient {

    private static final String X_AUTH_TOKEN = "8fc76cb7d4499ac3f81ad6b12035102d";
    private static final String BASE_URL = "https://kox947ka1a.execute-api.ap-northeast-2.amazonaws.com/prod/users";

    private HttpClient httpClient;

    public KakaoClient(Gson gson) {
        httpClient = new HttpClient(gson);
    }

    public BaseResponse<StartModel> start(int problemId) {
        Map<String, String> header = new HashMap<>();
        header.put("X-Auth-Token", X_AUTH_TOKEN);
        header.put("Content-Type", "application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("problem", problemId);
        return httpClient.request(HttpClient.RequestMethod.POST, BASE_URL + "/start", header, body, StartModel.class);
    }

    // todo kbt : 테스트 중
    // interface 의 애노테이션 읽고 파싱하기
    private <T> BaseResponse<T> invokeRequest(Method method, Class<T> tClass) {
        StringBuilder requestUrl = new StringBuilder(BASE_URL);
        HttpClient.RequestMethod requestMethod = null;
        Map<String, String> headerMap = new HashMap<>();
        Map<String, String> queryMap = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();

        // 메소드 애노테이션 파싱
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof GET) {
                requestUrl.append(((GET) annotation).value());
                requestMethod = HttpClient.RequestMethod.GET;
            } else if (annotation instanceof POST) {
                requestUrl.append(((POST) annotation).value());
                requestMethod = HttpClient.RequestMethod.POST;
            } else if (annotation instanceof PUT) {
                requestUrl.append(((PUT) annotation).value());
                requestMethod = HttpClient.RequestMethod.PUT;
            } else if (annotation instanceof Headers) {
                // 헤더 파싱
                String[] headers = ((Headers) annotation).value();
                for (String header : headers) {
                    String[] splits = header.split(":");
                    headerMap.put(splits[0].trim(), splits[1].trim());
                }
            }
        }

        // 파라미터 애노테이션 파싱
        for (Parameter parameter : method.getParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                if (annotation instanceof Query) {
                    String key = ((Query) annotation).value();
                }
            }
        }

        return httpClient.request(requestMethod, requestUrl.toString(), headerMap, bodyMap, tClass);
    }
}

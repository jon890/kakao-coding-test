package com.bifos.kakao.network;

public class KakaoClient {

    private static final String X_AUTH_TOKEN = "1ca30343d45d24c969d746a27f0373d2";
    private static final String BASE_URL = "https://kox947ka1a.execute-api.ap-northeast-2.amazonaws.com/prod/users";

    private HttpClient httpClient;

    public KakaoClient() {
        httpClient = new HttpClient();
    }
}

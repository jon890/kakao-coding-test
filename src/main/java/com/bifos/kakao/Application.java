package com.bifos.kakao;

import com.bifos.kakao.network.KakaoClient;
import com.bifos.kakao.network.model.BaseResponse;
import com.bifos.kakao.network.model.StartModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Application {

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        // 카카오 클라이언트 초기화
        Gson gson = new GsonBuilder().create();
        KakaoClient kakaoClient = new KakaoClient(gson);

        // start
        BaseResponse<StartModel> startResponse = kakaoClient.start(1);
        System.out.println(startResponse);
    }
}

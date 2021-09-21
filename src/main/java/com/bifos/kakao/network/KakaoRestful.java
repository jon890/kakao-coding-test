package com.bifos.kakao.network;

import com.bifos.kakao.network.annotation.GET;
import com.bifos.kakao.network.annotation.Headers;
import com.bifos.kakao.network.annotation.Query;
import com.bifos.kakao.network.model.BaseResponse;
import com.bifos.kakao.network.model.StartModel;

public interface KakaoRestful {

    @GET("/start")
    @Headers("X-AUTH-TOKEN: 1ca30343d45d24c969d746a27f0373d2")
    BaseResponse<StartModel> start(@Query("problems") int problemId);
}

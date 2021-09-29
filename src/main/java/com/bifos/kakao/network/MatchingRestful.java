package com.bifos.kakao.network;

import com.bifos.kakao.constant.MatchingConfig;
import com.bifos.kakao.dto.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface MatchingRestful {

    @POST("start")
    @Headers({"X-AUTH-TOKEN:" + MatchingConfig.X_AUTH_TOKEN,
            "Content-Type: application/json"})
    Call<StartResponseDto> start(@Body StartRequestDto body);

    @GET("waiting_line")
    @Headers("Content-Type: application/json")
    Call<WaitingLineResponseDto> getWaitingLine(@Header("Authorization") String authKey);

    @GET("game_result")
    @Headers("Content-Type: application/json")
    Call<GameResultResponseDto> getGameResult(@Header("Authorization") String authKey);

    @GET("user_info")
    @Headers("Content-Type: application/json")
    Call<UserInfoResponseDto> getUserInfo(@Header("Authorization") String authKey);

    @PUT("match")
    @Headers("Content-Type: application/json")
    Call<MatchResponseDto> match(@Header("Authorization") String authKey, @Body MatchRequestDto body);

    @PUT("change_grade")
    @Headers("Content-Type: application/json")
    Call<ChangeGradeResponseDto> changeGrade(@Header("Authorization") String authKey, @Body ChangeGradeRequestDto body);

    @GET("score")
    @Headers("Content-Type: application/json")
    Call<ScoreResponseDto> getScore(@Header("Authorization") String authKey);
}
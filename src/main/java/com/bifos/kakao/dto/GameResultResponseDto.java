package com.bifos.kakao.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class GameResultResponseDto {

    @SerializedName(value = "game_result")
    List<GameResultInfo> gameResultInfoList;

    @Getter
    @ToString
    public static class GameResultInfo {

        @SerializedName(value = "win")
        int winId;

        @SerializedName(value = "lose")
        int loseId;

        /**
         * 게임에 걸린시간
         */
        int taken;
    }

}

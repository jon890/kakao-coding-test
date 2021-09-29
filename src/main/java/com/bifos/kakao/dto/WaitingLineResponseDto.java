package com.bifos.kakao.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class WaitingLineResponseDto {

    @SerializedName(value = "waiting_line")
    List<WaitingInfo> waitingInfoList;

    @Getter
    @ToString
    public static class WaitingInfo {

        @SerializedName(value = "id")
        int userId;

        int from;
    }
}

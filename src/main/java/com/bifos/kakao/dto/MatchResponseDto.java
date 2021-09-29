package com.bifos.kakao.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MatchResponseDto {

    @SerializedName(value = "status")
    String status;

    int time;

}

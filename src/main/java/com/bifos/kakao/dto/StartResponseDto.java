package com.bifos.kakao.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StartResponseDto {

    @SerializedName(value = "auth_key")
    String authKey;

    int problem;

    int time;
    
}

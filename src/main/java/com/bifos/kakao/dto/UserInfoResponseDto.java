package com.bifos.kakao.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class UserInfoResponseDto {

    @SerializedName(value = "user_info")
    List<UserInfo> userInfoList;

    @Getter
    @ToString
    public static class UserInfo {

        @SerializedName(value = "id")
        int id;

        @SerializedName(value = "lose")
        int grade;

    }
}

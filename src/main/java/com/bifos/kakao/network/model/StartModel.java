package com.bifos.kakao.network.model;

import com.google.gson.annotations.SerializedName;

public class StartModel {

    @SerializedName(value = "auth_key")
    String authKey;

    int problem;

    int time;

    @Override
    public String toString() {
        return "StartModel{" +
                "authKey='" + authKey + '\'' +
                ", problem=" + problem +
                ", time=" + time +
                '}';
    }
}

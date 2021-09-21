package com.bifos.kakao.network.model;

public class BaseResponse<T> {

    int responseCode;
    T data;

    public BaseResponse(int responseCode, T data) {
        this.responseCode = responseCode;
        this.data = data;
    }
}

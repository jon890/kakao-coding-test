package com.bifos.kakao.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChangeGradeResponseDto {

    @SerializedName(value = "status")
    String status;

}

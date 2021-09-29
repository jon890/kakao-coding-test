package com.bifos.kakao.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ScoreResponseDto {

    String status;

    @SerializedName(value = "efficiency_score")
    float efficiencyScore;

    @SerializedName(value = "accuracy_score1")
    float accurancyScore1;

    @SerializedName(value = "accuracy_score2")
    float accurancyScore2;

    @SerializedName(value = "score")
    float total;
    
}

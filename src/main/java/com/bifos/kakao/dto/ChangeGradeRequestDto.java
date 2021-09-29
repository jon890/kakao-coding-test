package com.bifos.kakao.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChangeGradeRequestDto {

    @SerializedName(value = "commands")
    public final List<Command> commandList;

    public static class Command {
        int id;
        int grade;

        @Builder
        public Command(int id, int grade) {
            this.id = id;
            this.grade = grade;
        }
    }
}

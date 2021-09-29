package com.bifos.kakao.dto;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MatchRequestDto {

    public final List<List<Integer>> pairs;

    public static MatchRequestDto getNullRequest() {
        return new MatchRequestDto(new ArrayList<>());
    }
}

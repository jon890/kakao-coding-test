package com.bifos.kakao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServerState {

    None("None"),
    Ready("ready"),
    Progress("in_progress");

    final String value;

    public static ServerState ofValue(String value) {
        for (ServerState state : ServerState.values()) {
            if (state.value.equals(value)) {
                return state;
            }
        }

        return None;
    }
}

package com.example.builder;

/**
 * 커스텀 게임의 맵. 모든 단계가 함께 쓴다.
 *
 * 자바에는 java.util.Map도 있지만 자동으로 import되지 않으므로 이 예제에서는 겹치지 않는다.
 */
public enum Map {
    SUMMONERS_RIFT("소환사의 협곡"),
    HOWLING_ABYSS("칼바람 나락");

    private final String koreanName;

    Map(String koreanName) {
        this.koreanName = koreanName;
    }

    public String koreanName() {
        return koreanName;
    }
}

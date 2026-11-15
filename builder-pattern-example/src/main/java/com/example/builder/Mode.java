package com.example.builder;

/** 커스텀 게임의 모드. 모든 단계가 함께 쓴다. */
public enum Mode {
    BLIND_PICK("일반"),
    ALL_RANDOM("무작위");

    private final String koreanName;

    Mode(String koreanName) {
        this.koreanName = koreanName;
    }

    public String koreanName() {
        return koreanName;
    }
}

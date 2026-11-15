package com.example.builder.step1_nopattern;

import com.example.builder.Map;
import com.example.builder.Mode;

/**
 * 1단계 방법 A: 생성자에 값을 전부 넘기기.
 *
 * 필드가 전부 final이라 한번 만들면 바뀌지 않는다. 여기까지는 좋다.
 *
 * 문제점
 *  1. new CustomGame("내전 하실 분", ..., 5, null, true, false)
 *     호출하는 쪽만 봐서는 null, true, false가 각각 무엇인지 알 수 없다.
 *  2. allowSpectators와 addBots는 둘 다 boolean이다.
 *     자리를 바꿔 써도 컴파일러는 모른다. 에러 없이 "다른 방"이 만들어진다.
 *  3. 선택 값이 늘어날수록 인자 목록이 길어진다. 안 쓰는 값도 매번 넘겨야 한다.
 */
public class CustomGame {

    private final String title;
    private final Map map;
    private final Mode mode;
    private final int teamSize;
    private final String password;          // 없으면 null
    private final boolean allowSpectators;
    private final boolean addBots;

    public CustomGame(String title, Map map, Mode mode, int teamSize,
                      String password, boolean allowSpectators, boolean addBots) {
        this.title = title;
        this.map = map;
        this.mode = mode;
        this.teamSize = teamSize;
        this.password = password;
        this.allowSpectators = allowSpectators;
        this.addBots = addBots;
    }

    public boolean isAllowSpectators() {
        return allowSpectators;
    }

    public boolean isAddBots() {
        return addBots;
    }

    @Override
    public String toString() {
        return "[%s] %s / %s / %d vs %d / 비밀번호 %s / 관전 %s / 봇 %s".formatted(
                title, map.koreanName(), mode.koreanName(), teamSize, teamSize,
                password == null ? "없음" : "있음", allowSpectators ? "O" : "X", addBots ? "O" : "X");
    }
}

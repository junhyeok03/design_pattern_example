package com.example.builder.step2_builder;

import com.example.builder.Map;
import com.example.builder.Mode;

import java.util.Objects;

/**
 * 2단계: 요즘 흔히 쓰는 빌더. (이펙티브 자바 아이템 2)
 *
 *   CustomGame.builder("내전 하실 분")     ← 필수 값은 builder()의 인자로
 *           .map(Map.HOWLING_ABYSS)        ← 선택 값은 이름 붙은 메서드로
 *           .allowSpectators(true)
 *           .build();                      ← 규칙 검사는 여기서 한 번에
 *
 * 1단계 문제가 어떻게 풀리는지
 *  - true/false 자리 사고: 값마다 메서드 이름이 붙어 있어서 섞일 수 없다.
 *  - 반쯤 된 방: build()가 끝나기 전에는 CustomGame이 아예 존재하지 않는다.
 *  - 만든 뒤 바뀌는 방: 모든 필드가 final이고 setter가 없다. (불변 객체)
 *
 * Lombok의 @Builder가 자동으로 만들어 주는 게 바로 이 Builder 클래스다.
 * 여기서는 원리를 보려고 직접 썼다.
 *
 * 과한 경우: 설정이 2~3개뿐이면 생성자 하나로 충분하다. 빌더는 코드만 두 배가 된다.
 */
public class CustomGame {

    private final String title;
    private final Map map;
    private final Mode mode;
    private final int teamSize;
    private final String password;          // 없으면 null
    private final boolean allowSpectators;
    private final boolean addBots;

    // 생성자는 private. 바깥에서는 Builder를 거치지 않고는 만들 수 없다.
    private CustomGame(Builder b) {
        this.title = b.title;
        this.map = b.map;
        this.mode = b.mode;
        this.teamSize = b.teamSize;
        this.password = b.password;
        this.allowSpectators = b.allowSpectators;
        this.addBots = b.addBots;
    }

    public static Builder builder(String title) {
        return new Builder(title);
    }

    public static class Builder {

        // 필수 값
        private final String title;

        // 선택 값: 안 고르면 이 기본값이 들어간다
        private Map map = Map.SUMMONERS_RIFT;
        private Mode mode = Mode.BLIND_PICK;
        private int teamSize = 5;
        private String password = null;
        private boolean allowSpectators = false;
        private boolean addBots = false;

        private Builder(String title) {
            this.title = Objects.requireNonNull(title, "방 이름은 필수입니다");
        }

        // 각 메서드는 this를 돌려준다. 그래서 .map(...).mode(...)처럼 이어 쓸 수 있다.
        public Builder map(Map map) { this.map = map; return this; }
        public Builder mode(Mode mode) { this.mode = mode; return this; }
        public Builder teamSize(int teamSize) { this.teamSize = teamSize; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder allowSpectators(boolean allow) { this.allowSpectators = allow; return this; }
        public Builder addBots(boolean add) { this.addBots = add; return this; }

        /** 규칙을 한 번에 검사하고, 통과해야만 방을 만든다. (규칙은 예제용으로 지어낸 것) */
        public CustomGame build() {
            if (teamSize < 1 || teamSize > 5) {
                throw new IllegalStateException("팀 인원은 1~5명입니다: " + teamSize);
            }
            if (password != null && password.length() < 4) {
                throw new IllegalStateException("비밀번호는 4자 이상입니다: " + password.length() + "자");
            }
            if (map == Map.HOWLING_ABYSS && mode != Mode.ALL_RANDOM) {
                throw new IllegalStateException("칼바람 나락은 무작위 모드만 가능합니다: " + mode.koreanName());
            }
            return new CustomGame(this);
        }
    }

    // getter만 있고 setter는 없다
    public String getTitle() { return title; }
    public Map getMap() { return map; }
    public Mode getMode() { return mode; }
    public int getTeamSize() { return teamSize; }
    public String getPassword() { return password; }
    public boolean isAllowSpectators() { return allowSpectators; }
    public boolean isAddBots() { return addBots; }

    @Override
    public String toString() {
        return "[%s] %s / %s / %d vs %d / 비밀번호 %s / 관전 %s / 봇 %s".formatted(
                title, map.koreanName(), mode.koreanName(), teamSize, teamSize,
                password == null ? "없음" : "있음", allowSpectators ? "O" : "X", addBots ? "O" : "X");
    }
}

package com.example.builder;

/** 선수 한 명의 경기 기록. 모든 단계가 함께 쓴다. */
public record Player(String champion, int kills, int deaths, int assists) {

    /** KDA = (킬 + 어시스트) / 데스. 데스가 0이면 1로 나눈다. */
    public double kda() {
        return (double) (kills + assists) / Math.max(1, deaths);
    }

    public String score() {
        return "%d/%d/%d".formatted(kills, deaths, assists);
    }
}

package com.example.singleton.step5_stateful;

/**
 * 잘못된 예: 싱글톤인데 요청마다 바뀌는 값을 필드에 저장한다.
 * 모든 사용자가 객체 하나를 함께 쓰므로, 다른 사용자의 값이 덮어쓴다.
 */
public class StatefulMoodService {

    private String lastMood;   // 모든 사용자가 공유하는 필드

    public void record(String user, String mood) {
        System.out.println(user + "의 기분 기록: " + mood);
        this.lastMood = mood;
    }

    public String lastMood() {
        return lastMood;
    }
}

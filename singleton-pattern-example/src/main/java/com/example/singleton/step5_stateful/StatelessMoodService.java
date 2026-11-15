package com.example.singleton.step5_stateful;

/**
 * 고친 예: 필드에 저장하지 않고 결과를 바로 돌려준다.
 * 값은 메서드 안의 지역 변수에만 머물고, 지역 변수는 요청(스레드)마다 따로 생긴다.
 */
public class StatelessMoodService {

    public String record(String user, String mood) {
        System.out.println(user + "의 기분 기록: " + mood);
        return mood;
    }
}

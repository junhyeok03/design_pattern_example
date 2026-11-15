package com.example.builder.step3_gof_builder;

import com.example.builder.Map;
import com.example.builder.Mode;

/**
 * 3단계: 방을 만드는 "단계" 목록. (GoF의 Builder, 헤드 퍼스트의 AbstractBuilder)
 *
 * 2단계 빌더와 다른 점
 *  - 2단계: 결과물은 CustomGame 하나. 목적은 "읽기 쉽게, 안전하게" 만들기.
 *  - 3단계: 같은 단계를 받아서 결과물을 여러 종류로 만든다.
 *           서버 설정 객체가 될 수도, 디스코드 메시지가 될 수도 있다.
 *
 * 그래서 메서드가 this를 돌려주지 않는다(체이닝 없음). 순서는 Director가 정해서 부른다.
 *
 * 결과를 꺼내는 getResult()는 여기 두지 않고 구현 클래스마다 둔다.
 * 결과물 타입이 빌더마다 다르기 때문이다 (ServerRoomConfig, String ...).
 */
public interface RoomBuilder {

    void title(String title);

    void map(Map map);

    void mode(Mode mode);

    void teamSize(int teamSize);

    void password(String password);

    void allowSpectators(boolean allow);
}

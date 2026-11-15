package com.example.builder.step3_gof_builder;

import com.example.builder.Map;
import com.example.builder.Mode;

/**
 * "주말 내전 방 만들기" 순서를 아는 곳. (GoF의 Director, 헤드 퍼스트의 constructPlanner())
 *
 * 어떤 빌더를 받았는지 모른다. 이 파일에는 Server도 Discord도 등장하지 않는다.
 * 그저 RoomBuilder의 단계를 정해진 순서로 부를 뿐이다.
 *
 * 주말 내전 규칙(예제용): 칼바람 나락, 무작위, 5 vs 5, 관전 허용. 비밀번호만 주마다 바뀐다.
 */
public class WeekendScrimDirector {

    public void construct(RoomBuilder builder, String password) {
        builder.title("주말 내전 하실 분");
        builder.map(Map.HOWLING_ABYSS);
        builder.mode(Mode.ALL_RANDOM);
        builder.teamSize(5);
        builder.password(password);
        builder.allowSpectators(true);
    }
}

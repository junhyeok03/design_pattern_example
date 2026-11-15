package com.example.builder.step1_nopattern;

import com.example.builder.Map;
import com.example.builder.Mode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * [버그 증명 테스트] 이 파일의 테스트가 "통과"한다는 것은 버그가 있다는 뜻이다. 2단계에서 고친다.
 */
class CustomGameTest {

    @Test
    void 버그증명_관전과_봇_자리를_바꿔_써도_컴파일되고_다른_방이_만들어진다() {
        // 의도: 관전 허용 O, 봇 추가 X  →  실수로 (false, true) 순서로 넘김
        CustomGame room = new CustomGame("내전 하실 분", Map.HOWLING_ABYSS, Mode.ALL_RANDOM, 5, null, false, true);

        assertThat(room.isAllowSpectators()).isFalse();
        assertThat(room.isAddBots()).isTrue();
    }

    @Test
    void 버그증명_setter_방식은_반쯤_된_방이_존재한다() {
        CustomGameBean room = new CustomGameBean();
        room.setTitle("내전 하실 분");

        assertThat(room.getMap()).isNull();
    }

    @Test
    void 버그증명_setMap을_깜빡해도_컴파일되고_맵을_쓰는_순간에_터진다() {
        CustomGameBean room = new CustomGameBean();
        room.setTitle("내전 하실 분");
        room.setMode(Mode.ALL_RANDOM);
        room.setTeamSize(5);
        // setMap()을 깜빡함. 여기까지 에러 없음

        assertThatThrownBy(() -> room.getMap().koreanName())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 버그증명_setter_방식은_만든_뒤에도_설정이_바뀐다() {
        CustomGameBean room = new CustomGameBean();
        room.setPassword("1234");

        room.setPassword(null);

        assertThat(room.getPassword()).isNull();
    }
}

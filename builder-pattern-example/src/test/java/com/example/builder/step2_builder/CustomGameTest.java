package com.example.builder.step2_builder;

import com.example.builder.Map;
import com.example.builder.Mode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomGameTest {

    @Test
    void 선택_값을_안_넣으면_기본값이_들어간다() {
        CustomGame room = CustomGame.builder("아무나 오세요").build();

        assertThat(room.getTitle()).isEqualTo("아무나 오세요");
        assertThat(room.getMap()).isEqualTo(Map.SUMMONERS_RIFT);
        assertThat(room.getMode()).isEqualTo(Mode.BLIND_PICK);
        assertThat(room.getTeamSize()).isEqualTo(5);
        assertThat(room.getPassword()).isNull();
        assertThat(room.isAllowSpectators()).isFalse();
        assertThat(room.isAddBots()).isFalse();
    }

    @Test
    void 값마다_이름이_붙어_있어서_관전과_봇이_섞이지_않는다() {
        CustomGame room = CustomGame.builder("내전 하실 분").allowSpectators(true).build();

        assertThat(room.isAllowSpectators()).isTrue();
        assertThat(room.isAddBots()).isFalse();
    }

    @Test
    void 팀_인원이_6명이면_build에서_막힌다() {
        assertThatThrownBy(() -> CustomGame.builder("6인 방").teamSize(6).build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("팀 인원");
    }

    @Test
    void 비밀번호가_2자면_build에서_막힌다() {
        assertThatThrownBy(() -> CustomGame.builder("짧은 비번").password("12").build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("비밀번호");
    }

    @Test
    void 칼바람_나락에서_일반_모드면_build에서_막힌다() {
        assertThatThrownBy(() -> CustomGame.builder("칼바람 일반").map(Map.HOWLING_ABYSS).mode(Mode.BLIND_PICK).build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("칼바람 나락");
    }

    /** 불변 객체: 모든 필드가 final이고, set으로 시작하는 메서드가 없다. */
    @Test
    void 완성된_방은_바꿀_수_없다() {
        Field[] fields = CustomGame.class.getDeclaredFields();
        Method[] methods = CustomGame.class.getDeclaredMethods();

        assertThat(fields).allMatch(f -> Modifier.isFinal(f.getModifiers()));
        assertThat(Arrays.stream(methods).map(Method::getName)).noneMatch(name -> name.startsWith("set"));
    }
}

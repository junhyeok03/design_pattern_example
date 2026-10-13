package com.example.theme.step2_factorymethod;

import com.example.theme.parts.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FactoryMethodTest {

    @Test
    void 하위_클래스가_고른_배경으로_그린다() {
        assertThat(new LightScreen().render("안녕하세요").background()).isEqualTo(Color.WHITE);
        assertThat(new DarkScreen().render("안녕하세요").background()).isEqualTo(Color.BLACK);
    }

    @Test
    void 공통_흐름은_부모가_한_번만_정의한다() {
        assertThatThrownBy(() -> new LightScreen().render(" "))
                .hasMessage("표시할 글자가 비어 있습니다.");
        assertThatThrownBy(() -> new DarkScreen().render(" "))
                .hasMessage("표시할 글자가 비어 있습니다.");
    }

    @Test
    void 모든_테마가_같은_글자색을_쓰므로_다크_테마에서는_글자가_안_보인다() {
        assertThat(new LightScreen().render("안녕하세요").readable()).isTrue();
        assertThat(new DarkScreen().render("안녕하세요").readable()).isFalse();
    }
}

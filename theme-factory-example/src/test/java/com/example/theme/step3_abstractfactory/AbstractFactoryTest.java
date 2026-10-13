package com.example.theme.step3_abstractfactory;

import com.example.theme.Rendered;
import com.example.theme.parts.BlackBackground;
import com.example.theme.parts.BlackText;
import com.example.theme.parts.Color;
import com.example.theme.parts.WhiteBackground;
import com.example.theme.parts.WhiteText;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbstractFactoryTest {

    @Test
    void 공장마다_짝이_맞는_묶음을_만든다() {
        ThemeFactory light = new LightThemeFactory();
        ThemeFactory dark = new DarkThemeFactory();

        assertThat(light.createBackground()).isInstanceOf(WhiteBackground.class);
        assertThat(light.createText()).isInstanceOf(BlackText.class);
        assertThat(dark.createBackground()).isInstanceOf(BlackBackground.class);
        assertThat(dark.createText()).isInstanceOf(WhiteText.class);
    }

    @Test
    void 라이트_테마는_흰_배경에_검은_글자() {
        Rendered rendered = new ThemedScreen(new LightThemeFactory()).render("안녕하세요");

        assertThat(rendered.background()).isEqualTo(Color.WHITE);
        assertThat(rendered.text()).isEqualTo(Color.BLACK);
        assertThat(rendered.readable()).isTrue();
    }

    @Test
    void 다크_테마는_검은_배경에_흰_글자() {
        Rendered rendered = new ThemedScreen(new DarkThemeFactory()).render("안녕하세요");

        assertThat(rendered.background()).isEqualTo(Color.BLACK);
        assertThat(rendered.text()).isEqualTo(Color.WHITE);
        assertThat(rendered.readable()).isTrue();
    }

    @Test
    void 짝이_안_맞는_조합은_글자가_안_보인다() {
        // ThemedScreen을 거치지 않고 직접 섞으면 이런 일이 생긴다.
        Rendered mixed = new Rendered(new BlackBackground().color(), new BlackText().color(), "안녕하세요");

        assertThat(mixed.readable()).isFalse();
    }

    @Test
    void 테마에_맞는_공장을_고른다() {
        assertThat(Theme.LIGHT.factory()).isInstanceOf(LightThemeFactory.class);
        assertThat(Theme.DARK.factory()).isInstanceOf(DarkThemeFactory.class);
    }

    @Test
    void 표시할_글자가_없으면_그리지_않는다() {
        ThemedScreen screen = new ThemedScreen(new DarkThemeFactory());

        assertThatThrownBy(() -> screen.render(""))
                .hasMessage("표시할 글자가 비어 있습니다.");
    }
}

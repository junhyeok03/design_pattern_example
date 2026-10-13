package com.example.theme.step1_nopattern;

import com.example.theme.parts.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IfElseScreenTest {

    private final IfElseScreen screen = new IfElseScreen();

    @Test
    void 테마_이름에_따라_다른_배경을_그린다() {
        assertThat(screen.render("LIGHT", "안녕하세요").background()).isEqualTo(Color.WHITE);
        assertThat(screen.render("DARK", "안녕하세요").background()).isEqualTo(Color.BLACK);
    }

    @Test
    void 테마_이름에_오타가_나면_실행해_봐야_안다() {
        assertThatThrownBy(() -> screen.render("DAKR", "안녕하세요"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

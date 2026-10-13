package com.example.theme.step3_abstractfactory;

import com.example.theme.Rendered;
import com.example.theme.parts.Background;
import com.example.theme.parts.Text;

/**
 * 테마 공장을 받아 화면을 그린다.
 * 배경과 글자를 따로 받지 않고 공장 하나만 받으므로, 짝이 안 맞는 조합을 만들 방법이 없다.
 * 지금 어떤 테마인지 모르고, 알 필요도 없다.
 */
public class ThemedScreen {

    private final Background background;
    private final Text text;

    public ThemedScreen(ThemeFactory factory) {
        this.background = factory.createBackground();
        this.text = factory.createText();
    }

    public Rendered render(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("표시할 글자가 비어 있습니다.");
        }
        return new Rendered(background.color(), text.color(), message);
    }
}

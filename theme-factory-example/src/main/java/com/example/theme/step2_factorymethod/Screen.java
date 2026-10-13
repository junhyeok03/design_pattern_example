package com.example.theme.step2_factorymethod;

import com.example.theme.Rendered;
import com.example.theme.parts.Background;
import com.example.theme.parts.BlackText;
import com.example.theme.parts.Text;

/**
 * 2단계: Factory Method 패턴의 Creator.
 *
 * 화면을 그리는 흐름(render)은 여기서 한 번만 정의한다.
 * 흐름 중에 필요한 배경을 "무엇으로 만들지"는 하위 클래스가 createBackground에서 정한다.
 */
public abstract class Screen {

    public final Rendered render(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("표시할 글자가 비어 있습니다.");
        }

        Background background = createBackground();    // 무슨 배경을 쓸지는 하위 클래스가 정한다
        Text text = new BlackText();                   // 글자는 모든 테마 공통
        return new Rendered(background.color(), text.color(), message);
    }

    /** Factory Method: 하위 클래스가 어떤 배경을 만들지 결정한다. */
    protected abstract Background createBackground();
}

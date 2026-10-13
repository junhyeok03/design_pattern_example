package com.example.theme.step1_nopattern;

import com.example.theme.Rendered;
import com.example.theme.parts.Background;
import com.example.theme.parts.BlackBackground;
import com.example.theme.parts.BlackText;
import com.example.theme.parts.Text;
import com.example.theme.parts.WhiteBackground;

/**
 * 1단계: 패턴 없이 if로 테마를 나눈 버전.
 *
 * 문제점
 *  - 테마(고대비 모드 등)를 추가할 때마다 이 메서드를 고쳐야 한다.
 *  - "무엇을 만들지" 고르는 코드와 "화면을 그리는 흐름"이 한 메서드에 섞여 있다.
 *  - 테마 이름을 문자열로 받아 오타가 나도 실행해 봐야 안다.
 */
public class IfElseScreen {

    public Rendered render(String theme, String message) {
        Text text = new BlackText();    // 글자는 모든 테마 공통

        Background background;
        if (theme.equals("LIGHT")) {
            background = new WhiteBackground();
        } else if (theme.equals("DARK")) {
            background = new BlackBackground();
        } else {
            throw new IllegalArgumentException("지원하지 않는 테마입니다: " + theme);
        }
        return new Rendered(background.color(), text.color(), message);
    }
}

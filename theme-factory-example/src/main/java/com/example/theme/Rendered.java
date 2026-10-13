package com.example.theme;

import com.example.theme.parts.Color;

/** 화면을 그린 결과. 예제에서는 진짜 화면 대신 이 값을 돌려준다. */
public record Rendered(Color background, Color text, String message) {

    /** 배경과 글자 색이 같으면 글자가 보이지 않는다. */
    public boolean readable() {
        return background != text;
    }

    @Override
    public String toString() {
        return "[배경 " + background.label() + " / 글자 " + text.label() + "] " + message
                + (readable() ? "  → 잘 보임" : "  → 글자가 안 보임!");
    }
}

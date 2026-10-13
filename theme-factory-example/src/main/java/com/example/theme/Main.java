package com.example.theme;

import com.example.theme.parts.BlackBackground;
import com.example.theme.parts.BlackText;
import com.example.theme.parts.Color;
import com.example.theme.step1_nopattern.IfElseScreen;
import com.example.theme.step2_factorymethod.DarkScreen;
import com.example.theme.step2_factorymethod.LightScreen;
import com.example.theme.step3_abstractfactory.Theme;
import com.example.theme.step3_abstractfactory.ThemedScreen;

public class Main {

    static final String MESSAGE = "안녕하세요";

    public static void main(String[] args) {
        System.out.println("===== 1단계: if로 테마 나누기 =====");
        show(new IfElseScreen().render("LIGHT", MESSAGE));
        try {
            new IfElseScreen().render("DAKR", MESSAGE);
        } catch (IllegalArgumentException e) {
            System.out.println("테마 이름 오타 → 실행해 봐야 안다: " + e.getMessage());
        }

        System.out.println();
        System.out.println("===== 2단계: Factory Method =====");
        show(new LightScreen().render(MESSAGE));
        show(new DarkScreen().render(MESSAGE));
        System.out.println("배경만 바꾸고 글자는 모든 테마가 같은 검정을 쓰기 때문이다. 테마마다 글자색도 달라야 한다.");

        System.out.println();
        System.out.println("===== 3단계: Abstract Factory =====");
        show(new ThemedScreen(Theme.LIGHT.factory()).render(MESSAGE));
        show(new ThemedScreen(Theme.DARK.factory()).render(MESSAGE));

        System.out.println();
        System.out.println("===== 짝이 안 맞으면? (다크 배경 + 라이트 글자) =====");
        show(new Rendered(new BlackBackground().color(), new BlackText().color(), MESSAGE));
        System.out.println("↑ ThemedScreen은 공장 하나만 받아서 이렇게 섞을 수 없다.");
    }

    /** 결과 설명과 함께, 터미널에 실제 색으로 칠해서 보여 준다. */
    private static void show(Rendered rendered) {
        System.out.println(rendered);
        System.out.println("    화면: " + paint(rendered));
    }

    private static String paint(Rendered rendered) {
        String bg = rendered.background() == Color.WHITE ? "\u001B[107m" : "\u001B[40m";
        String fg = rendered.text() == Color.WHITE ? "\u001B[97m" : "\u001B[30m";
        return bg + fg + "   " + rendered.message() + "   " + "\u001B[0m";
    }
}

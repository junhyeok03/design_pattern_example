package com.example.theme.step3_abstractfactory;

/**
 * 테마에 맞는 공장을 고르는 곳.
 * 분기가 사라지는 것은 아니다. 프로그램 곳곳에 흩어져 있던 분기가 이 한 곳으로 모인다.
 */
public enum Theme {
    LIGHT,
    DARK;

    public ThemeFactory factory() {
        return switch (this) {
            case LIGHT -> new LightThemeFactory();
            case DARK -> new DarkThemeFactory();
        };
    }
}

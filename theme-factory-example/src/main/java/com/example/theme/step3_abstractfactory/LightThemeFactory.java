package com.example.theme.step3_abstractfactory;

import com.example.theme.parts.Background;
import com.example.theme.parts.WhiteBackground;
import com.example.theme.parts.BlackText;
import com.example.theme.parts.Text;

public class LightThemeFactory implements ThemeFactory {

    // 라이트 테마 공장은 라이트 테마용 부품만 만든다

    @Override
    public Background createBackground() {
        return new WhiteBackground();
    }

    @Override
    public Text createText() {
        return new BlackText();
    }
}

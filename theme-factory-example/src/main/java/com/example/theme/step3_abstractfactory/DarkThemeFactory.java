package com.example.theme.step3_abstractfactory;

import com.example.theme.parts.Background;
import com.example.theme.parts.BlackBackground;
import com.example.theme.parts.WhiteText;
import com.example.theme.parts.Text;

public class DarkThemeFactory implements ThemeFactory {

    // 다크 테마 공장은 다크 테마용 부품만 만든다

    @Override
    public Background createBackground() {
        return new BlackBackground();
    }

    @Override
    public Text createText() {
        return new WhiteText();
    }
}

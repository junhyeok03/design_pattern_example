package com.example.theme.step2_factorymethod;

import com.example.theme.parts.Background;
import com.example.theme.parts.BlackBackground;

public class DarkScreen extends Screen {

    @Override
    protected Background createBackground() {
        return new BlackBackground();
    }
}

package com.example.theme.step2_factorymethod;

import com.example.theme.parts.Background;
import com.example.theme.parts.WhiteBackground;

public class LightScreen extends Screen {

    @Override
    protected Background createBackground() {
        return new WhiteBackground();
    }
}

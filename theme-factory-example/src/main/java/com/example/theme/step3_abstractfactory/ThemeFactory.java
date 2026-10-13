package com.example.theme.step3_abstractfactory;

import com.example.theme.parts.Background;
import com.example.theme.parts.Text;

/**
 * 3단계: Abstract Factory.
 *
 * 한 테마에 필요한 부품 묶음(배경 + 글자)을 함께 만든다.
 * 구현체마다 짝이 맞는 묶음만 만들기 때문에, 검은 배경에 검은 글자가 섞일 수 없다.
 */
public interface ThemeFactory {
    // "테마라면 배경과 글자를 둘 다 만들 수 있어야 한다"는 약속

    Background createBackground();

    Text createText();
}

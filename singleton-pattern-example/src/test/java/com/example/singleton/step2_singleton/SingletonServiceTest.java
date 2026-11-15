package com.example.singleton.step2_singleton;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class SingletonServiceTest {

    @Test
    void getInstance는_언제나_같은_객체를_돌려준다() {
        SingletonService singleton1 = SingletonService.getInstance();
        SingletonService singleton2 = SingletonService.getInstance();

        System.out.println("singleton1 = " + singleton1);
        System.out.println("singleton2 = " + singleton2);

        assertThat(singleton1).isSameAs(singleton2);
    }

    @Test
    void 생성자가_private이라_밖에서_new할_수_없다() throws Exception {
        Constructor<SingletonService> constructor = SingletonService.class.getDeclaredConstructor();

        // new SingletonService();  ← 컴파일 오류
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
    }
}

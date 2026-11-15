package com.example.singleton.step1_nocontainer;

import com.example.singleton.user.UserService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PureContainerTest {

    @Test
    void 스프링_없는_순수_DI_컨테이너는_부를_때마다_새_객체를_만든다() {
        AppConfig appConfig = new AppConfig();

        UserService userService1 = appConfig.userService();
        UserService userService2 = appConfig.userService();

        System.out.println("userService1 = " + userService1);
        System.out.println("userService2 = " + userService2);

        assertThat(userService1).isNotSameAs(userService2);
    }
}

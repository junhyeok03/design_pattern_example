package com.example.singleton.step1_nocontainer;

import com.example.singleton.user.MemoryUserRepository;
import com.example.singleton.user.UserService;

/**
 * 1단계: 스프링 없이 직접 만든 DI 컨테이너.
 * 객체를 달라고 할 때마다 new로 새로 만든다.
 * 요청이 초당 100번 오면 UserService도 초당 100개 생겼다가 버려진다.
 */
public class AppConfig {

    public UserService userService() {
        return new UserService(new MemoryUserRepository());
    }
}

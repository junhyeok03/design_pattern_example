package com.example.singleton.step2_singleton;

/**
 * 2단계: 싱글톤 패턴.
 *  1. 클래스가 로딩될 때 static 영역에 객체를 딱 하나 만든다.
 *  2. 생성자를 private으로 막아 밖에서 new를 못 하게 한다.
 *  3. 객체가 필요하면 getInstance()로만 꺼낸다.
 */
public class SingletonService {

    private static final SingletonService INSTANCE = new SingletonService();

    private SingletonService() {
    }

    public static SingletonService getInstance() {
        return INSTANCE;
    }

    public String logic() {
        return "싱글톤 객체의 로직 실행";
    }
}

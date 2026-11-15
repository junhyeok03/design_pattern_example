package com.example.singleton.step3_drawbacks;

/**
 * 같은 기능을 생성자 주입으로 바꾼 버전.
 * 발송기를 밖에서 넣어 주므로 테스트에서는 가짜를, 운영에서는 진짜를 넣을 수 있다.
 * 스프링 컨테이너가 하는 일이 바로 이것이다.
 */
public class InjectedReminderService {

    private final MessageGateway gateway;

    public InjectedReminderService(MessageGateway gateway) {
        this.gateway = gateway;
    }

    public void remind(String name) {
        gateway.send(name, "오늘 일기를 써 보세요");
    }
}

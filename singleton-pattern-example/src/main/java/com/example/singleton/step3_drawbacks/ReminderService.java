package com.example.singleton.step3_drawbacks;

/**
 * 싱글톤 패턴의 단점을 보여주는 클라이언트.
 *
 *  - 구체 클래스에 의존한다: SmsGateway.getInstance()를 코드에 박아 넣어서
 *    카카오톡 발송기로 바꾸거나 테스트용 가짜로 바꿀 방법이 없다.
 *  - 의존이 숨어 있다: 생성자만 봐서는 문자를 보내는 클래스인지 알 수 없다.
 */
public class ReminderService {

    public void remind(String name) {
        SmsGateway.getInstance().send(name, "오늘 일기를 써 보세요");
    }
}

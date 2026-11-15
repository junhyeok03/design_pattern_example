package com.example.singleton.step3_drawbacks;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SingletonDrawbackTest {

    @Test
    void 싱글톤에_직접_의존하면_서로_모르는_코드끼리_상태가_섞인다() {
        ReminderService morning = new ReminderService();
        ReminderService evening = new ReminderService();
        int before = SmsGateway.getInstance().sent().size();

        morning.remind("김순자");
        evening.remind("박영수");

        // 둘은 서로 모르는 객체인데, 보낸 기록은 전역 객체 하나에 함께 쌓인다.
        // 다른 테스트가 먼저 실행됐다면 그 기록도 섞여 있다. 그래서 before를 빼고 세야 한다.
        List<String> sent = SmsGateway.getInstance().sent();
        System.out.println("전역 발송 기록 = " + sent);
        assertThat(sent).hasSize(before + 2);
    }

    @Test
    void 생성자로_주입받으면_테스트용_가짜로_바꿀_수_있다() {
        FakeGateway fake = new FakeGateway();
        InjectedReminderService service = new InjectedReminderService(fake);

        service.remind("김순자");

        // 실제 문자 발송기를 건드리지 않고, 이 테스트가 보낸 것만 정확히 확인한다.
        assertThat(fake.sent()).containsExactly("김순자: 오늘 일기를 써 보세요");
    }

    @Test
    void 주입_방식은_운영에서_싱글톤_발송기를_그대로_넣을_수도_있다() {
        int before = SmsGateway.getInstance().sent().size();

        new InjectedReminderService(SmsGateway.getInstance()).remind("박영수");

        assertThat(SmsGateway.getInstance().sent()).hasSize(before + 1);
    }

    private static class FakeGateway implements MessageGateway {
        private final List<String> sent = new ArrayList<>();

        @Override
        public void send(String to, String text) {
            sent.add(to + ": " + text);
        }

        @Override
        public List<String> sent() {
            return sent;
        }
    }
}

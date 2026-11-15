package com.example.singleton.step5_stateful;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class StatefulServiceTest {

    @Test
    void 필드에_상태를_저장하면_다른_사용자가_덮어쓴다() {
        StatefulMoodService service = new StatefulMoodService();   // 싱글톤처럼 하나를 공유

        service.record("김순자", "최고에요");   // 요청 A
        service.record("박영수", "슬퍼요");     // 요청 B가 사이에 끼어듦

        String mood = service.lastMood();       // 요청 A가 자기 기분을 확인
        System.out.println("김순자가 확인한 기분 = " + mood);

        assertThat(mood).isEqualTo("슬퍼요");
    }

    @Test
    void 상태를_지역_변수로_돌려주면_섞이지_않는다() {
        StatelessMoodService service = new StatelessMoodService();

        String soonja = service.record("김순자", "최고에요");
        String youngsoo = service.record("박영수", "슬퍼요");

        assertThat(soonja).isEqualTo("최고에요");
        assertThat(youngsoo).isEqualTo("슬퍼요");
    }

    @RepeatedTest(5)
    void 사용자별로_잠그면_같은_사용자의_동시_요청에도_기록이_망가지지_않는다() throws Exception {
        SafeRecentHistory history = new SafeRecentHistory();
        ExecutorService pool = Executors.newFixedThreadPool(8);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> results = new ArrayList<>();

        for (int t = 0; t < 8; t++) {
            results.add(pool.submit(() -> {
                start.await();
                for (int i = 0; i < 2_000; i++) {
                    history.remember(1, i % 20);
                }
                return null;
            }));
        }
        start.countDown();
        for (Future<?> result : results) {
            result.get(30, TimeUnit.SECONDS);   // 예외가 났다면 여기서 다시 던져져 테스트가 실패한다
        }
        pool.shutdown();

        assertThat(history.history(1)).hasSize(SafeRecentHistory.LIMIT).doesNotContainNull();
    }
}

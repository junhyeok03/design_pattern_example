package com.example.singleton;

import com.example.singleton.step1_nocontainer.AppConfig;
import com.example.singleton.step2_singleton.SingletonService;
import com.example.singleton.step5_stateful.SafeRecentHistory;
import com.example.singleton.step5_stateful.StatefulMoodService;
import com.example.singleton.step5_stateful.UnsafeRecentHistory;
import com.example.singleton.user.UserService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.IntSupplier;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("===== 1. 순수 DI 컨테이너: 부를 때마다 새 객체 =====");
        AppConfig appConfig = new AppConfig();
        UserService userService1 = appConfig.userService();
        UserService userService2 = appConfig.userService();
        System.out.println("userService1 = " + userService1);
        System.out.println("userService2 = " + userService2);
        System.out.println("같은 객체인가? " + (userService1 == userService2));

        System.out.println();
        System.out.println("===== 2. 싱글톤 패턴: 언제나 같은 객체 =====");
        SingletonService singleton1 = SingletonService.getInstance();
        SingletonService singleton2 = SingletonService.getInstance();
        System.out.println("singleton1 = " + singleton1);
        System.out.println("singleton2 = " + singleton2);
        System.out.println("같은 객체인가? " + (singleton1 == singleton2));

        System.out.println();
        System.out.println("===== 5-1. 상태를 가진 싱글톤: 다른 사용자가 덮어쓴다 =====");
        StatefulMoodService moodService = new StatefulMoodService();   // 싱글톤처럼 하나만 공유한다고 가정
        moodService.record("김순자", "최고에요");
        moodService.record("박영수", "슬퍼요");
        System.out.println("김순자가 확인한 자기 기분 = " + moodService.lastMood() + "  ← 박영수의 기분이 나온다");

        System.out.println();
        System.out.println("===== 5-2. 같은 사용자의 요청 8개가 동시에 2,000번씩 기록할 때 (20회 반복) =====");
        runConcurrently("Unsafe (QuizService 방식)", (userId, quizId) -> UNSAFE.get().remember(userId, quizId),
                () -> UNSAFE.set(new UnsafeRecentHistory()), () -> UNSAFE.get().history(1).size());
        runConcurrently("Safe   (AiRateLimiter 방식)", (userId, quizId) -> SAFE.get().remember(userId, quizId),
                () -> SAFE.set(new SafeRecentHistory()), () -> SAFE.get().history(1).size());
    }

    private static final AtomicReference<UnsafeRecentHistory> UNSAFE = new AtomicReference<>();
    private static final AtomicReference<SafeRecentHistory> SAFE = new AtomicReference<>();

    /** 같은 사용자(1번)로 8개 스레드가 동시에 기록하고, 예외 수와 크기 제한(10) 위반 횟수를 센다. */
    private static void runConcurrently(String label, BiConsumer<Integer, Integer> remember,
                                        Runnable reset, IntSupplier finalSize) throws Exception {
        int rounds = 20, threads = 8, perThread = 2_000;
        AtomicInteger exceptions = new AtomicInteger();
        int brokenRounds = 0;
        for (int round = 0; round < rounds; round++) {
            reset.run();
            ExecutorService pool = Executors.newFixedThreadPool(threads);
            CountDownLatch start = new CountDownLatch(1);
            for (int t = 0; t < threads; t++) {
                pool.submit(() -> {
                    try {
                        start.await();
                    } catch (InterruptedException e) {
                        return;
                    }
                    for (int i = 0; i < perThread; i++) {
                        try {
                            remember.accept(1, i % 20);
                        } catch (RuntimeException e) {
                            exceptions.incrementAndGet();
                        }
                    }
                });
            }
            start.countDown();
            pool.shutdown();
            pool.awaitTermination(30, TimeUnit.SECONDS);
            try {
                if (finalSize.getAsInt() != 10) brokenRounds++;
            } catch (RuntimeException e) {
                brokenRounds++;   // 손상돼서 꺼내는 것조차 실패
            }
        }
        System.out.printf("%s → 예외 %d건, 최종 기록이 10개가 아닌 회차 %d/%d%n",
                label, exceptions.get(), brokenRounds, rounds);
    }
}

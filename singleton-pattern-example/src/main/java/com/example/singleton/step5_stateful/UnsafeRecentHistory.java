package com.example.singleton.step5_stateful;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Memora QuizService의 "최근 출제 기록"과 같은 구조. 동시 요청에 안전하지 않다.
 *
 * 바깥 맵(ConcurrentHashMap)은 안전하지만, 사용자별 ArrayDeque는 동기화 없이 쓴다.
 * 같은 사용자의 요청이 동시에 오면 두 스레드가 같은 ArrayDeque를 동시에 고친다.
 */
public class UnsafeRecentHistory {

    public static final int LIMIT = 10;

    private final Map<Integer, Deque<Integer>> recent = new ConcurrentHashMap<>();

    public void remember(int userId, int quizId) {
        Deque<Integer> history = recent.computeIfAbsent(userId, k -> new ArrayDeque<>());
        history.contains(quizId);
        history.addLast(quizId);
        if (history.size() > LIMIT) {     // 확인과 꺼내기 사이에 다른 스레드가 끼어들 수 있다
            history.pollFirst();
        }
    }

    public List<Integer> history(int userId) {
        return List.copyOf(recent.getOrDefault(userId, new ArrayDeque<>()));
    }
}

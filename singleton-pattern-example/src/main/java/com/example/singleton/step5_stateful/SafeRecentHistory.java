package com.example.singleton.step5_stateful;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Memora AiRateLimiter와 같은 방식으로 고친 버전.
 * 사용자별 ArrayDeque를 잠근 상태에서만 읽고 고치므로, 같은 사용자의 요청은 한 번에 하나씩 처리된다.
 */
public class SafeRecentHistory {

    public static final int LIMIT = 10;

    private final Map<Integer, Deque<Integer>> recent = new ConcurrentHashMap<>();

    public void remember(int userId, int quizId) {
        Deque<Integer> history = recent.computeIfAbsent(userId, k -> new ArrayDeque<>());
        synchronized (history) {
            history.contains(quizId);
            history.addLast(quizId);
            if (history.size() > LIMIT) {
                history.pollFirst();
            }
        }
    }

    public List<Integer> history(int userId) {
        Deque<Integer> history = recent.get(userId);
        if (history == null) {
            return List.of();
        }
        synchronized (history) {
            return List.copyOf(history);
        }
    }
}

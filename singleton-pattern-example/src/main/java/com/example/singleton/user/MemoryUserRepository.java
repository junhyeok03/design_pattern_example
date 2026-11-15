package com.example.singleton.user;

import java.util.Map;

public class MemoryUserRepository implements UserRepository {

    private final Map<Integer, String> names = Map.of(1, "김순자", 2, "박영수");

    @Override
    public String findName(int userId) {
        return names.getOrDefault(userId, "알 수 없음");
    }
}

package com.example.prototype.step4_registry;

import com.example.prototype.step3_deepclone.Minion;

import java.util.HashMap;
import java.util.Map;

/**
 * 미니언 원본 보관함. 헤드 퍼스트 디자인 패턴의 MonsterRegistry에 해당한다.
 *
 * 종류 이름으로 원본을 맡겨 두고, 꺼낼 때마다 clone()해서 준다.
 * 원본은 클래스가 아니라 객체라서, 게임 도중에도 새 종류를 등록할 수 있다.
 */
public class MinionRegistry {

    private final Map<String, Minion> prototypes = new HashMap<>();

    public void register(String type, Minion prototype) {
        prototypes.put(type, prototype);
    }

    public Minion create(String type) {
        Minion prototype = prototypes.get(type);
        if (prototype == null) {
            throw new IllegalArgumentException("등록되지 않은 미니언입니다: " + type);
        }
        return prototype.clone();
    }
}

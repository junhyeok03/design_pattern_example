package com.example.prototype.step1_nopattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 1단계 넥서스: 웨이브마다 종류별 if로 미니언을 new 한다.
 * <p>
 * 문제점
 * 1. 미니언 한 마리마다 Model을 새로 로드한다. 7마리 웨이브 하나에 약 7초.
 * 2. 미니언 종류가 늘면(슈퍼 미니언 등) 이 파일의 if도 늘어난다.
 */
public class Nexus {

    private static final List<String> WAVE = List.of(
            "근거리", "근거리", "근거리", "원거리", "원거리", "원거리", "대포");

    public List<Minion> spawnWave() {
        List<Minion> wave = new ArrayList<>();
        for (String type : WAVE) {
            wave.add(create(type));
        }
        return wave;
    }

    private Minion create(String type) {
        if (type.equals("근거리")) {
            return new MeleeMinion();
        } else if (type.equals("원거리")) {
            return new CasterMinion();
        } else if (type.equals("대포")) {
            return new SiegeMinion();
        }
        throw new IllegalArgumentException("모르는 미니언입니다: " + type);
    }
}

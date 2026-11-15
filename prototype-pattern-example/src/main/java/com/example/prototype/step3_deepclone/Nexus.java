package com.example.prototype.step3_deepclone;

import java.util.List;

/**
 * 3단계 넥서스 (2단계와 같다): 게임 시작 때 종류별 원본을 한 번만 만들고, 웨이브마다 clone()한다.
 * Model 로딩은 원본 3마리를 만들 때 3번뿐이고, 웨이브 하나는 거의 0초다.
 */
public class Nexus {

    private final Minion melee = new MeleeMinion();
    private final Minion caster = new CasterMinion();
    private final Minion siege = new SiegeMinion();

    public List<Minion> spawnWave() {
        return List.of(
                melee.clone(), melee.clone(), melee.clone(),
                caster.clone(), caster.clone(), caster.clone(),
                siege.clone());
    }
}

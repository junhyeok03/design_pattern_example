package com.example.prototype.step4_registry;

import com.example.prototype.step3_deepclone.CasterMinion;
import com.example.prototype.step3_deepclone.MeleeMinion;
import com.example.prototype.step3_deepclone.Minion;
import com.example.prototype.step3_deepclone.SiegeMinion;

import java.util.ArrayList;
import java.util.List;

/**
 * 4단계 넥서스: 미니언 원본을 MinionRegistry에 맡기고, 웨이브 구성은 종류 이름으로만 관리한다.
 * 미니언은 3단계(깊은 복사) 것을 그대로 쓴다.
 *
 * 게임 도중에 종류가 느는 두 가지 이벤트
 *  - 억제기 파괴 : 이미 있는 클래스(SuperMinion)의 원본을 늦게 등록한다
 *  - 바론 버프   : 클래스 없이, 기존 원본을 복제해 고친 "객체"를 새 종류로 등록한다
 */
public class Nexus {

    private static final String SUPER = "슈퍼";
    private static final String BUFFED = "강화 ";
    private static final int BARON_ATTACK_BONUS = 50;

    private final MinionRegistry registry = new MinionRegistry();
    private final List<String> waveComposition = new ArrayList<>(List.of(
            "근거리", "근거리", "근거리", "원거리", "원거리", "원거리", "대포"));
    private boolean inhibitorDestroyed = false;
    private boolean baronBuffed = false;

    public Nexus() {
        registry.register("근거리", new MeleeMinion());
        registry.register("원거리", new CasterMinion());
        registry.register("대포", new SiegeMinion());
    }

    /** "억제기 파괴" 이벤트. 이때 처음으로 슈퍼 미니언 원본을 만들어 등록한다. */
    public void onInhibitorDestroyed() {
        if (inhibitorDestroyed) {
            return;
        }
        registry.register(SUPER, new SuperMinion());
        waveComposition.add(SUPER);
        inhibitorDestroyed = true;
    }

    /**
     * "바론 버프" 이벤트. 지금 웨이브에 있는 종류마다 "강화 OO" 종류를 새로 만든다.
     *
     * 새 클래스를 만들지 않는다. 기존 원본을 복제해서 공격력만 올린 뒤, 그 객체를 새 원본으로 등록한다.
     * new도 구체 클래스 이름도 없고, 모델도 다시 로드하지 않는다(복제본은 원본의 모델을 공유).
     */
    public void onBaronBuff() {
        if (baronBuffed) {
            return;
        }
        for (String type : waveComposition.stream().distinct().toList()) {
            Minion buffed = registry.create(type);    // 기존 원본의 복제본
            buffed.increaseAttack(BARON_ATTACK_BONUS);
            registry.register(BUFFED + type, buffed);
        }
        waveComposition.replaceAll(type -> BUFFED + type);
        baronBuffed = true;
    }

    /** 웨이브 내보내기. new도, 구체 클래스 이름도 없다. 종류 이름만 안다. */
    public List<Minion> spawnWave() {
        return waveComposition.stream().map(registry::create).toList();
    }
}

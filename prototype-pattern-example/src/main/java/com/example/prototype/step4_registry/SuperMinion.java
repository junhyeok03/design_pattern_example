package com.example.prototype.step4_registry;

import com.example.prototype.step3_deepclone.Minion;

/**
 * 억제기가 부서진 뒤에야 등장하는 미니언.
 * 헤드 퍼스트 디자인 패턴의 DynamicPlayerGeneratedMonster처럼, 게임 도중에 레지스트리에 새로 등록된다.
 */
public class SuperMinion extends Minion {

    public SuperMinion() {
        super("슈퍼 미니언", 1500, 190);
    }
}

package com.example.prototype.step1_nopattern;

import com.example.prototype.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * 1단계 미니언. 복제할 줄 모른다. 필요할 때마다 new로 새로 만든다.
 * 그때마다 생성자에서 Model을 로드하니 한 마리에 1초씩 걸린다.
 */
public abstract class Minion {

    private final String name;
    private final int hp;
    private final int attack;
    private final List<String> statusEffects = new ArrayList<>();    // 둔화, 화상 등
    private final Model model;

    protected Minion(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.model = new Model(name);    // 1초
    }

    public void applyEffect(String effect) {
        statusEffects.add(effect);
    }

    public List<String> statusEffects() {
        return List.copyOf(statusEffects);
    }

    public Model model() {
        return model;
    }

    @Override
    public String toString() {
        return "%s[체력 %d, 공격력 %d, 상태이상 %s]".formatted(name, hp, attack, statusEffects);
    }
}

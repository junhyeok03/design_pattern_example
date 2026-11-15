package com.example.prototype.step3_deepclone;

import com.example.prototype.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * 3단계 미니언: 2단계와 같지만 clone()에서 상태이상 리스트를 새로 만든다 (깊은 복사).
 *
 * 모든 필드를 깊게 복사하지는 않는다.
 *  - statusEffects : 바뀌는 리스트 → 복제본마다 새로 만든다
 *  - model         : 바뀌지 않는 객체 → 그대로 공유한다 (다시 만들면 1초씩 걸린다)
 */
public abstract class Minion implements Cloneable {

    private final String name;
    private final int hp;
    private int attack;    // 4단계 바론 버프에서 올린다
    private List<String> statusEffects = new ArrayList<>();    // clone()에서 새 리스트로 바꿔 넣어야 해서 final을 뗐다
    private final Model model;

    protected Minion(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.model = new Model(name);    // 원본을 만들 때 한 번만 1초
    }

    @Override
    public Minion clone() {
        try {
            Minion copy = (Minion) super.clone();
            copy.statusEffects = new ArrayList<>(statusEffects);    // ✅ 상태이상 리스트는 새로 만든다 (깊은 복사)
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloneable을 구현했으니 일어날 수 없다", e);
        }
    }

    public void applyEffect(String effect) {
        statusEffects.add(effect);
    }

    /** 4단계에서 기존 원본의 복제본을 강화해, 새 클래스 없이 새 종류의 원본을 만들 때 쓴다. */
    public void increaseAttack(int amount) {
        attack += amount;
    }

    public int attack() {
        return attack;
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

package com.example.prototype.step2_shallowclone;

import com.example.prototype.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * 2단계 미니언: Cloneable을 구현하고 clone()으로 자기를 복제한다.
 *
 * super.clone()은 생성자를 거치지 않으니 Model을 다시 로드하지 않는다(빠르다).
 * 대신 필드를 "그대로" 복사하는 얕은 복사라서, 상태이상 리스트도 주소만 복사된다.
 */
public abstract class Minion implements Cloneable {

    private final String name;
    private final int hp;
    private final int attack;
    private final List<String> statusEffects = new ArrayList<>();
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
            return (Minion) super.clone();    // ⚠ statusEffects는 리스트 "주소"만 복사된다 (얕은 복사)
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloneable을 구현했으니 일어날 수 없다", e);
        }
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

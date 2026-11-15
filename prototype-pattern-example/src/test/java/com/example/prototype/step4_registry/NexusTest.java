package com.example.prototype.step4_registry;

import com.example.prototype.Model;
import com.example.prototype.step3_deepclone.Minion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NexusTest {

    @BeforeAll
    static void 모델_로딩을_짧게() {
        Model.setLoadMillis(10);
    }

    @Test
    void 억제기가_부서지기_전에는_슈퍼_미니언이_없다() {
        List<Minion> wave = new Nexus().spawnWave();

        assertThat(wave).hasSize(7).noneMatch(SuperMinion.class::isInstance);
    }

    @Test
    void 억제기가_부서진_뒤_웨이브부터_슈퍼_미니언이_섞여_나온다() {
        Nexus nexus = new Nexus();

        nexus.onInhibitorDestroyed();
        List<Minion> wave = nexus.spawnWave();

        assertThat(wave).hasSize(8).filteredOn(SuperMinion.class::isInstance).hasSize(1);
    }

    @Test
    void 바론_버프_뒤_웨이브는_새_클래스_없이_공격력만_오른_미니언으로_나온다() {
        Nexus nexus = new Nexus();
        List<Minion> before = nexus.spawnWave();

        nexus.onBaronBuff();
        List<Minion> after = nexus.spawnWave();

        assertThat(after).extracting(Object::getClass)
                .containsExactlyElementsOf(before.stream().map(Object::getClass).toList());    // 클래스는 그대로
        for (int i = 0; i < before.size(); i++) {
            assertThat(after.get(i).attack()).isEqualTo(before.get(i).attack() + 50);
            assertThat(after.get(i).model()).isSameAs(before.get(i).model());    // 모델도 다시 로드하지 않았다
        }
    }

    @Test
    void 바론_버프_뒤에_억제기가_부서져도_슈퍼_미니언은_한_마리만_추가된다() {
        Nexus nexus = new Nexus();

        nexus.onBaronBuff();
        nexus.onInhibitorDestroyed();
        nexus.onInhibitorDestroyed();

        assertThat(nexus.spawnWave()).filteredOn(SuperMinion.class::isInstance).hasSize(1);
    }

    @Test
    void 복제해서_고친_새_원본을_등록해도_기존_원본은_그대로다() {
        MinionRegistry registry = new MinionRegistry();
        registry.register("슈퍼", new SuperMinion());

        Minion buffed = registry.create("슈퍼");
        buffed.increaseAttack(50);
        registry.register("강화 슈퍼", buffed);

        assertThat(registry.create("슈퍼").attack()).isEqualTo(190);
        assertThat(registry.create("강화 슈퍼").attack()).isEqualTo(240);
    }

    @Test
    void 레지스트리는_꺼낼_때마다_새_복제본을_준다() {
        MinionRegistry registry = new MinionRegistry();
        registry.register("슈퍼", new SuperMinion());

        Minion a = registry.create("슈퍼");
        a.applyEffect("화상");

        assertThat(registry.create("슈퍼")).isNotSameAs(a);
        assertThat(registry.create("슈퍼").statusEffects()).isEmpty();
        assertThatThrownBy(() -> registry.create("드래곤"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

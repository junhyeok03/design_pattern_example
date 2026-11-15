package com.example.prototype.step1_nopattern;

import com.example.prototype.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NexusTest {

    @BeforeAll
    static void 모델_로딩을_짧게() {
        Model.setLoadMillis(10);
    }

    @Test
    void 웨이브는_근거리3_원거리3_대포1() {
        List<Minion> wave = new Nexus().spawnWave();

        assertThat(wave).extracting(Object::getClass).containsExactly(
                MeleeMinion.class, MeleeMinion.class, MeleeMinion.class,
                CasterMinion.class, CasterMinion.class, CasterMinion.class,
                SiegeMinion.class);
    }

    @Test
    void 같은_종류인데도_미니언마다_모델을_새로_로드한다() {
        List<Minion> wave = new Nexus().spawnWave();

        assertThat(wave.get(0).model()).isNotSameAs(wave.get(1).model());
    }
}

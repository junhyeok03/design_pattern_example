package com.example.prototype.step3_deepclone;

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
    void 둔화는_맞은_한_마리에게만_걸린다() {
        Nexus nexus = new Nexus();
        List<Minion> wave = nexus.spawnWave();

        wave.get(0).applyEffect("둔화");

        assertThat(wave.get(0).statusEffects()).containsExactly("둔화");
        assertThat(wave.subList(1, wave.size())).allMatch(m -> m.statusEffects().isEmpty());
        assertThat(nexus.spawnWave()).allMatch(m -> m.statusEffects().isEmpty());
    }

    @Test
    void 바뀌지_않는_모델은_여전히_공유한다() {
        List<Minion> wave = new Nexus().spawnWave();

        assertThat(wave.get(0).model()).isSameAs(wave.get(1).model());
    }
}

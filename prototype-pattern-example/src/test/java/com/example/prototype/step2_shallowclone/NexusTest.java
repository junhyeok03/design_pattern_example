package com.example.prototype.step2_shallowclone;

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
    void 복제본은_원본의_모델을_함께_쓴다_다시_로드하지_않는다() {
        Nexus nexus = new Nexus();

        Minion first = nexus.spawnWave().get(0);
        Minion nextWave = nexus.spawnWave().get(0);

        assertThat(first).isNotSameAs(nextWave);
        assertThat(first.model()).isSameAs(nextWave.model());
    }

    /**
     * [버그 증명 테스트] 이 테스트가 "통과"한다는 것은 버그가 있다는 뜻이다.
     * 얕은 복사라서 같은 종류의 복제본이 모두 원본의 상태이상 리스트를 함께 쓴다. 3단계에서 고친다.
     */
    @Test
    void 버그증명_한_마리에게_건_둔화가_같은_종류_전체와_다음_웨이브까지_퍼진다() {
        Nexus nexus = new Nexus();
        List<Minion> wave = nexus.spawnWave();

        wave.get(0).applyEffect("둔화");    // 애쉬가 근거리 미니언 한 마리에게 둔화

        assertThat(wave.subList(0, 3)).allMatch(m -> m.statusEffects().contains("둔화"));    // 근거리 3마리 모두
        assertThat(nexus.spawnWave().get(0).statusEffects()).contains("둔화");             // 다음 웨이브도 나오자마자
        assertThat(wave.get(3).statusEffects()).isEmpty();    // 원거리는 원본이 달라서 멀쩡하다
    }
}

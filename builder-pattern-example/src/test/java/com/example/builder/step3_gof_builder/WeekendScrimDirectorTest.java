package com.example.builder.step3_gof_builder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WeekendScrimDirectorTest {

    private final WeekendScrimDirector director = new WeekendScrimDirector();

    @Test
    void 서버_빌더를_넣으면_서버_설정_객체가_나온다() {
        ServerConfigBuilder builder = new ServerConfigBuilder();

        director.construct(builder, "lol2026");

        assertThat(builder.getResult()).isEqualTo(new ServerRoomConfig(
                "주말 내전 하실 분", "HOWLING_ABYSS", "ALL_RANDOM", 10, "lol2026", true));
    }

    @Test
    void 디스코드_빌더를_넣으면_초대_메시지가_나온다() {
        DiscordInviteBuilder builder = new DiscordInviteBuilder();

        director.construct(builder, "lol2026");

        assertThat(builder.getResult()).isEqualTo("""
                [내전 모집] 주말 내전 하실 분
                맵: 칼바람 나락 / 모드: 무작위 / 5 vs 5
                비밀번호: DM 주세요 / 관전 가능
                """);
    }

    @Test
    void 디스코드_메시지에는_비밀번호_원문이_없다() {
        DiscordInviteBuilder builder = new DiscordInviteBuilder();

        director.construct(builder, "lol2026");

        assertThat(builder.getResult()).doesNotContain("lol2026");
    }
}

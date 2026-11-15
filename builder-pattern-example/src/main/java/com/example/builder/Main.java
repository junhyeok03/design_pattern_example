package com.example.builder;

import com.example.builder.step1_nopattern.CustomGameBean;
import com.example.builder.step2_builder.CustomGame;
import com.example.builder.step3_gof_builder.DiscordInviteBuilder;
import com.example.builder.step3_gof_builder.ServerConfigBuilder;
import com.example.builder.step3_gof_builder.WeekendScrimDirector;

/** 커스텀 게임 방을 1단계 → 2단계 → 3단계 순서로 만들어 본다. */
public class Main {

    public static void main(String[] args) {
        step1();
        step2();
        step3();
    }

    static void step1() {
        System.out.println("===== 1단계 A: 생성자에 전부 넘기기 =====");
        // 의도: 관전 허용 O, 봇 추가 X
        // 실수: 마지막 두 boolean의 자리를 바꿔 썼다. 컴파일은 된다.
        var room = new com.example.builder.step1_nopattern.CustomGame(
                "내전 하실 분", Map.HOWLING_ABYSS, Mode.ALL_RANDOM, 5, null, false, true);
        System.out.println(room);
        System.out.println("⚠ 관전을 켜려다 봇을 켰다. 에러는 하나도 나지 않았다.");

        System.out.println();
        System.out.println("===== 1단계 B: setter로 하나씩 넣기 =====");
        CustomGameBean bean = new CustomGameBean();
        bean.setTitle("내전 하실 분");
        System.out.println("이름만 넣은 상태: " + bean);
        System.out.println("⚠ 맵이 null인 \"반쯤 된 방\"이 이미 존재한다.");

        bean.setMode(Mode.ALL_RANDOM);
        bean.setTeamSize(5);
        // setMap()을 깜빡한 채로 방을 로비에 넘겼다. 컴파일 에러는 없다.
        try {
            showInLobby(bean);
        } catch (NullPointerException e) {
            System.out.println("💥 NullPointerException: setMap()을 깜빡한 곳이 아니라, 맵을 쓰는 로비에서 터졌다.");
        }

        bean.setMap(Map.HOWLING_ABYSS);
        bean.setPassword("1234");
        bean.setPassword(null);     // 다른 코드 어딘가에서
        System.out.println("완성 뒤 누군가 고친 상태: " + bean);
        System.out.println("⚠ 방이 만들어진 뒤에도 비밀번호가 사라질 수 있다.");
    }

    /** 로비 화면. 방을 만든 코드와는 멀리 떨어진 다른 파일이라고 생각하자. */
    static void showInLobby(CustomGameBean room) {
        System.out.println("로비: " + room.getMap().koreanName() + " 방이 열렸습니다");
    }

    static void step2() {
        System.out.println();
        System.out.println("===== 2단계: 빌더 (이펙티브 자바) =====");
        CustomGame room = CustomGame.builder("내전 하실 분")
                .map(Map.HOWLING_ABYSS)
                .mode(Mode.ALL_RANDOM)
                .allowSpectators(true)      // 이름이 붙어 있어서 봇과 헷갈릴 수 없다
                .build();
        System.out.println("완성: " + room);
        System.out.println("기본값만: " + CustomGame.builder("아무나 오세요").build());

        System.out.println("--- build()에서 규칙 검사 ---");
        tryBuild(CustomGame.builder("6인 방").teamSize(6));
        tryBuild(CustomGame.builder("짧은 비번").password("12"));
        tryBuild(CustomGame.builder("칼바람 일반").map(Map.HOWLING_ABYSS).mode(Mode.BLIND_PICK));
    }

    static void tryBuild(CustomGame.Builder builder) {
        try {
            builder.build();
        } catch (IllegalStateException e) {
            System.out.println("⛔ build() 거부: " + e.getMessage());
        }
    }

    static void step3() {
        System.out.println();
        System.out.println("===== 3단계: GoF 빌더 (같은 Director, 다른 결과물) =====");
        WeekendScrimDirector director = new WeekendScrimDirector();

        ServerConfigBuilder server = new ServerConfigBuilder();
        director.construct(server, "lol2026");
        System.out.println("① 서버 빌더 → " + server.getResult());

        DiscordInviteBuilder discord = new DiscordInviteBuilder();
        director.construct(discord, "lol2026");
        System.out.println("② 디스코드 빌더 →");
        System.out.print(discord.getResult());

        System.out.println("→ director.construct()는 두 번 모두 같은 코드. 빌더만 바꿔 넣었다.");
    }
}

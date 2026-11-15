package com.example.builder.step3_gof_builder;

import com.example.builder.Map;
import com.example.builder.Mode;

/**
 * 같은 단계를 받아서 디스코드에 올릴 초대 메시지(String)를 만든다. (GoF의 ConcreteBuilder)
 *
 * 같은 password() 단계라도 서버 빌더는 원문을 저장하고, 이 빌더는 "DM 주세요"로 바꾼다.
 * 단계는 같고, 각 단계를 어떻게 처리할지는 빌더마다 다르다.
 */
public class DiscordInviteBuilder implements RoomBuilder {

    private String title;
    private String map;
    private String mode;
    private int teamSize;
    private String password = "없음";
    private String spectators;

    @Override
    public void title(String title) {
        this.title = title;
    }

    @Override
    public void map(Map map) {
        this.map = map.koreanName();            // 사람이 읽으니 한국어 이름으로
    }

    @Override
    public void mode(Mode mode) {
        this.mode = mode.koreanName();
    }

    @Override
    public void teamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    @Override
    public void password(String password) {
        if (password != null) {
            this.password = "DM 주세요";         // 공개 채널이므로 원문을 쓰지 않는다
        }
    }

    @Override
    public void allowSpectators(boolean allow) {
        this.spectators = allow ? "관전 가능" : "관전 불가";
    }

    public String getResult() {
        return """
                [내전 모집] %s
                맵: %s / 모드: %s / %d vs %d
                비밀번호: %s / %s
                """.formatted(title, map, mode, teamSize, teamSize, password, spectators);
    }
}

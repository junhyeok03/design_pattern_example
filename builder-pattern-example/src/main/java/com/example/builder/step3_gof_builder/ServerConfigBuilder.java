package com.example.builder.step3_gof_builder;

import com.example.builder.Map;
import com.example.builder.Mode;

/** 같은 단계를 받아서 게임 서버용 설정 객체(ServerRoomConfig)를 만든다. (GoF의 ConcreteBuilder) */
public class ServerConfigBuilder implements RoomBuilder {

    private String roomName;
    private String mapId;
    private String modeId;
    private int maxPlayers;
    private String password;
    private boolean spectatorSlotsOpen;

    @Override
    public void title(String title) {
        this.roomName = title;
    }

    @Override
    public void map(Map map) {
        this.mapId = map.name();                // 서버는 ID로 받는다: "HOWLING_ABYSS"
    }

    @Override
    public void mode(Mode mode) {
        this.modeId = mode.name();
    }

    @Override
    public void teamSize(int teamSize) {
        this.maxPlayers = teamSize * 2;         // 서버는 양 팀 합친 인원으로 받는다
    }

    @Override
    public void password(String password) {
        this.password = password;               // 서버는 원문이 있어야 입장을 검사할 수 있다
    }

    @Override
    public void allowSpectators(boolean allow) {
        this.spectatorSlotsOpen = allow;
    }

    public ServerRoomConfig getResult() {
        return new ServerRoomConfig(roomName, mapId, modeId, maxPlayers, password, spectatorSlotsOpen);
    }
}

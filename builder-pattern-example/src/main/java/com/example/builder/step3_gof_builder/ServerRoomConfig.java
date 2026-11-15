package com.example.builder.step3_gof_builder;

/**
 * 게임 서버에 넘길 방 설정. ServerConfigBuilder의 결과물이다.
 *
 * 서버는 사람이 아니라서 한국어 이름 대신 ID를 쓰고,
 * "5 vs 5" 대신 전체 최대 인원(10)을 쓰고, 비밀번호는 원문 그대로 받는다.
 */
public record ServerRoomConfig(
        String roomName,
        String mapId,
        String modeId,
        int maxPlayers,
        String password,
        boolean spectatorSlotsOpen) {
}

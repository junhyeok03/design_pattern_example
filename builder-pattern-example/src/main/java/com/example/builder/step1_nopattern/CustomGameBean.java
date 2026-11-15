package com.example.builder.step1_nopattern;

import com.example.builder.Map;
import com.example.builder.Mode;

/**
 * 1단계 방법 B: 빈 객체를 만들고 setter로 하나씩 넣기. (자바빈즈 방식)
 *
 * room.setAllowSpectators(true) 처럼 이름이 붙어 있어서 방법 A보다 읽기는 쉽다.
 *
 * 문제점
 *  1. new CustomGameBean() 직후에는 이름도 맵도 없는 "반쯤 된 방"이다.
 *     setter를 다 부르기 전에 누가 이 방을 쓰면 null이 튀어나온다.
 *  2. 필드가 final일 수 없다. 방이 만들어진 뒤에도 누구나 setter로 설정을 바꿀 수 있다.
 *  3. "칼바람 나락은 무작위만" 같은 규칙을 어디서 검사할지 애매하다.
 *     setMap()에서? setMode()에서? 둘 중 무엇을 먼저 부를지도 모르는데.
 */
public class CustomGameBean {

    private String title;
    private Map map;
    private Mode mode;
    private int teamSize;
    private String password;
    private boolean allowSpectators;
    private boolean addBots;

    public void setTitle(String title) { this.title = title; }
    public void setMap(Map map) { this.map = map; }
    public void setMode(Mode mode) { this.mode = mode; }
    public void setTeamSize(int teamSize) { this.teamSize = teamSize; }
    public void setPassword(String password) { this.password = password; }
    public void setAllowSpectators(boolean allowSpectators) { this.allowSpectators = allowSpectators; }
    public void setAddBots(boolean addBots) { this.addBots = addBots; }

    public Map getMap() { return map; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return "[%s] 맵=%s / 모드=%s / 인원=%d / 비밀번호=%s / 관전=%s / 봇=%s".formatted(
                title, map, mode, teamSize, password, allowSpectators, addBots);
    }
}

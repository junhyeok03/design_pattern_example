package com.example.builder.step2_builder;

import com.example.builder.Player;
import com.example.builder.Team;

public class TextReportBuilder implements ReportBuilder {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public void buildHeader(int minutes) {
        sb.append("경기 시간: %d분%n".formatted(minutes));
    }

    @Override
    public void buildMvp(Player mvp) {
        sb.append("[MVP] %s (%s, KDA %.1f)%n".formatted(mvp.champion(), mvp.score(), mvp.kda()));
    }

    @Override
    public void buildTeam(String result, Team team) {
        sb.append("%s - %s%n".formatted(result, team.name()));
        for (Player p : team.players()) {
            sb.append("  %s %s%n".formatted(p.champion(), p.score()));
        }
    }

    public String getResult() {
        return sb.toString();
    }
}

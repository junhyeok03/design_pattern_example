package com.example.builder.step2_builder;

import com.example.builder.Match;
import com.example.builder.Player;

import java.util.Comparator;

/**
 * 리포트를 조립하는 순서와 규칙을 아는 곳. (GoF의 Director)
 *
 * 순서와 MVP 규칙은 이 파일 한 곳에만 있다. 여기를 고치면 모든 형식이 함께 바뀐다.
 * 어떤 형식인지는 모른다. Text도 Markdown도 이 파일에 등장하지 않는다.
 */
public class MatchReportDirector {

    public void construct(Match match, ReportBuilder builder) {
        builder.buildHeader(match.minutes());
        builder.buildMvp(mvpOf(match));    // 기획 요청: MVP는 헤더 바로 아래
        builder.buildTeam("승리", match.winner());
        builder.buildTeam("패배", match.loser());
    }

    private Player mvpOf(Match match) {
        return match.allPlayers().stream().max(Comparator.comparingDouble(Player::kda)).orElseThrow();
    }
}

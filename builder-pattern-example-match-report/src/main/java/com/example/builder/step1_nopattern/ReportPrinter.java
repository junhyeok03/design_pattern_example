package com.example.builder.step1_nopattern;

import com.example.builder.Match;
import com.example.builder.Player;
import com.example.builder.Team;

import java.util.Comparator;

/**
 * 1단계: 형식마다 메서드를 하나씩 따로 만든 리포트 출력기.
 *
 * 문제점
 *  1. "어떤 순서로, 무엇을 넣는가"(조립 과정)가 형식마다 복사돼 있다.
 *     기획이 "MVP를 헤더 바로 아래로 올려 주세요"라고 해서 toText()만 고치고 toMarkdown()은 깜빡했다.
 *  2. MVP 규칙(KDA 최고)도 형식마다 복사돼 있다.
 *  3. HTML 형식을 추가하면 순서와 규칙을 세 번째로 복사해야 한다.
 */
public class ReportPrinter {

    public String toText(Match match) {
        StringBuilder sb = new StringBuilder();
        sb.append("경기 시간: %d분%n".formatted(match.minutes()));

        Player mvp = match.allPlayers().stream().max(Comparator.comparingDouble(Player::kda)).orElseThrow();
        sb.append("[MVP] %s (%s, KDA %.1f)%n".formatted(mvp.champion(), mvp.score(), mvp.kda()));    // 기획 요청으로 위로 올림

        appendTextTeam(sb, "승리", match.winner());
        appendTextTeam(sb, "패배", match.loser());
        return sb.toString();
    }

    public String toMarkdown(Match match) {
        StringBuilder sb = new StringBuilder();
        sb.append("# 경기 결과 (%d분)%n".formatted(match.minutes()));
        appendMarkdownTeam(sb, "승리", match.winner());
        appendMarkdownTeam(sb, "패배", match.loser());

        Player mvp = match.allPlayers().stream().max(Comparator.comparingDouble(Player::kda)).orElseThrow();
        sb.append("**MVP: %s** (%s, KDA %.1f)%n".formatted(mvp.champion(), mvp.score(), mvp.kda()));    // ⚠ 여기는 옮기는 걸 깜빡했다
        return sb.toString();
    }

    private void appendTextTeam(StringBuilder sb, String result, Team team) {
        sb.append("%s - %s%n".formatted(result, team.name()));
        for (Player p : team.players()) {
            sb.append("  %s %s%n".formatted(p.champion(), p.score()));
        }
    }

    private void appendMarkdownTeam(StringBuilder sb, String result, Team team) {
        sb.append("## %s - %s%n".formatted(result, team.name()));
        sb.append("| 챔피언 | K/D/A |%n|---|---|%n".formatted());
        for (Player p : team.players()) {
            sb.append("| %s | %s |%n".formatted(p.champion(), p.score()));
        }
    }
}

package com.example.builder.step2_builder;

import com.example.builder.Match;
import com.example.builder.Player;
import com.example.builder.Team;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MatchReportDirectorTest {

    private final Match match = new Match(31,
            new Team("블루팀", List.of(new Player("아리", 7, 1, 5), new Player("리 신", 3, 2, 8))),
            new Team("레드팀", List.of(new Player("제드", 4, 5, 1), new Player("쓰레쉬", 0, 4, 6))));

    private final MatchReportDirector director = new MatchReportDirector();

    /** 형식과 상관없이 Director가 부르는 순서만 기록하는 가짜 Builder. */
    static class RecordingBuilder implements ReportBuilder {
        final List<String> calls = new ArrayList<>();

        public void buildHeader(int minutes) { calls.add("헤더"); }
        public void buildMvp(Player mvp) { calls.add("MVP " + mvp.champion()); }
        public void buildTeam(String result, Team team) { calls.add(result); }
    }

    @Test
    void 조립_순서와_MVP_규칙은_Director가_정한다() {
        RecordingBuilder builder = new RecordingBuilder();

        director.construct(match, builder);

        assertThat(builder.calls).containsExactly("헤더", "MVP 아리", "승리", "패배");
    }

    @Test
    void 텍스트와_마크다운_모두_MVP가_팀보다_위다() {
        TextReportBuilder text = new TextReportBuilder();
        MarkdownReportBuilder markdown = new MarkdownReportBuilder();

        director.construct(match, text);
        director.construct(match, markdown);

        for (String report : List.of(text.getResult(), markdown.getResult())) {
            assertThat(report.indexOf("MVP")).isLessThan(report.indexOf("승리"));
        }
    }
}

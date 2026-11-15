package com.example.builder.step1_nopattern;

import com.example.builder.Match;
import com.example.builder.Player;
import com.example.builder.Team;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReportPrinterTest {

    private final Match match = new Match(31,
            new Team("블루팀", List.of(new Player("아리", 7, 1, 5))),
            new Team("레드팀", List.of(new Player("제드", 4, 5, 1))));

    /**
     * [버그 증명 테스트] 이 테스트가 "통과"한다는 것은 버그가 있다는 뜻이다.
     * 순서가 형식마다 복사돼 있어서, 한쪽만 고치면 두 리포트의 순서가 달라진다. 2단계에서 고친다.
     */
    @Test
    void 버그증명_텍스트는_MVP가_팀보다_위인데_마크다운은_아래다() {
        ReportPrinter printer = new ReportPrinter();

        String text = printer.toText(match);
        String markdown = printer.toMarkdown(match);

        assertThat(text.indexOf("MVP")).isLessThan(text.indexOf("승리"));
        assertThat(markdown.indexOf("MVP")).isGreaterThan(markdown.indexOf("승리"));
    }
}

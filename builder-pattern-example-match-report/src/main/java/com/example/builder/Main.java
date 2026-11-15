package com.example.builder;

import com.example.builder.step1_nopattern.ReportPrinter;
import com.example.builder.step2_builder.MarkdownReportBuilder;
import com.example.builder.step2_builder.MatchReportDirector;
import com.example.builder.step2_builder.TextReportBuilder;

import java.util.List;

/** 경기 결과 하나를 텍스트와 마크다운 두 형식의 리포트로 만든다. */
public class Main {

    public static void main(String[] args) {
        Match match = sampleMatch();

        System.out.println("===== 1단계: 패턴 없이 (형식마다 순서를 따로 가짐) =====");
        ReportPrinter printer = new ReportPrinter();
        System.out.println("--- 텍스트 ---");
        System.out.print(printer.toText(match));
        System.out.println("--- 마크다운 ---");
        System.out.print(printer.toMarkdown(match));
        System.out.println("→ 텍스트는 MVP가 헤더 바로 아래인데, 마크다운은 맨 아래다. 한쪽만 고쳤다.");

        System.out.println();
        System.out.println("===== 2단계: Builder (순서는 Director, 형식은 Builder) =====");
        MatchReportDirector director = new MatchReportDirector();

        TextReportBuilder text = new TextReportBuilder();
        director.construct(match, text);
        System.out.println("--- 텍스트 ---");
        System.out.print(text.getResult());

        MarkdownReportBuilder markdown = new MarkdownReportBuilder();
        director.construct(match, markdown);
        System.out.println("--- 마크다운 ---");
        System.out.print(markdown.getResult());
        System.out.println("→ 같은 Director가 조립하니 두 형식의 순서가 같다.");
    }

    static Match sampleMatch() {
        Team blue = new Team("블루팀", List.of(
                new Player("아리", 7, 1, 5),
                new Player("리 신", 3, 2, 8)));
        Team red = new Team("레드팀", List.of(
                new Player("제드", 4, 5, 1),
                new Player("쓰레쉬", 0, 4, 6)));
        return new Match(31, blue, red);
    }
}

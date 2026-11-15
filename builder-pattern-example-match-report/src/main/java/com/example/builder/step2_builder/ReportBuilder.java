package com.example.builder.step2_builder;

import com.example.builder.Player;
import com.example.builder.Team;

/**
 * 리포트의 부품을 하나씩 만드는 단계들. (GoF의 Builder)
 *
 * "무엇을 어떤 모양으로 쓰는가"만 정한다. 순서는 모른다. 순서는 Director가 정한다.
 *
 * 결과를 꺼내는 getResult()는 여기 두지 않고 구현 클래스마다 둔다.
 * 형식에 따라 결과물의 타입이 다를 수 있기 때문이다 (문자열, HTML 문서 객체, 이미지 ...).
 */
public interface ReportBuilder {

    void buildHeader(int minutes);

    void buildMvp(Player mvp);

    void buildTeam(String result, Team team);
}

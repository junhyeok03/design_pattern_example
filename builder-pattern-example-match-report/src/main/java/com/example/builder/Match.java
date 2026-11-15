package com.example.builder;

import java.util.List;
import java.util.stream.Stream;

/** 경기 하나의 결과. 리포트를 만드는 재료다. */
public record Match(int minutes, Team winner, Team loser) {

    public List<Player> allPlayers() {
        return Stream.concat(winner.players().stream(), loser.players().stream()).toList();
    }
}

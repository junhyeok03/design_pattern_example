package com.example.weather.decorator;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/** 캐시 만료를 기다리지 않고 시험하기 위한 시계. */
class MutableClock extends Clock {

    private Instant now = Instant.parse("2026-01-01T00:00:00Z");

    void advance(Duration duration) {
        now = now.plus(duration);
    }

    @Override
    public Instant instant() {
        return now;
    }

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }
}

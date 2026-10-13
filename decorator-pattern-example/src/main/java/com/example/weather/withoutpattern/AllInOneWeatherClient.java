package com.example.weather.withoutpattern;

import com.example.weather.Weather;
import com.example.weather.WeatherApiException;
import com.example.weather.WeatherClient;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * 비교용: 데코레이터 없이 로깅·재시도·캐시를 한 클래스에 넣은 버전.
 *
 * 문제점
 *  - 기능을 하나 추가할 때마다 이 클래스를 고치고, 플래그와 if가 늘어난다.
 *  - 순서가 코드에 고정된다. "캐시 적중까지 로그로 남기기"처럼 순서를 바꾸려면 또 플래그가 필요하다.
 *  - 로깅만, 캐시만 따로 테스트하기 어렵다.
 */
public class AllInOneWeatherClient implements WeatherClient {

    private final WeatherClient api;
    private final boolean logging;
    private final int maxAttempts;
    private final Duration cacheTtl;   // null이면 캐시 사용 안 함
    private final Map<String, Weather> cache = new HashMap<>();
    private final Map<String, Instant> expiresAt = new HashMap<>();

    public AllInOneWeatherClient(WeatherClient api, boolean logging, int maxAttempts, Duration cacheTtl) {
        this.api = api;
        this.logging = logging;
        this.maxAttempts = maxAttempts;
        this.cacheTtl = cacheTtl;
    }

    @Override
    public Weather fetch(String city) {
        if (cacheTtl != null) {
            Instant expiry = expiresAt.get(city);
            if (expiry != null && Instant.now().isBefore(expiry)) {
                return cache.get(city);
            }
        }

        WeatherApiException lastFailure = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            long start = System.nanoTime();
            try {
                Weather weather = api.fetch(city);
                if (logging) {
                    System.out.println("[all-in-one] " + city + " → " + weather
                            + " (" + (System.nanoTime() - start) / 1_000_000 + "ms)");
                }
                if (cacheTtl != null) {
                    cache.put(city, weather);
                    expiresAt.put(city, Instant.now().plus(cacheTtl));
                }
                return weather;
            } catch (WeatherApiException e) {
                if (logging) {
                    System.out.println("[all-in-one] " + city + " → 실패: " + e.getMessage());
                }
                lastFailure = e;
            }
        }
        throw lastFailure;
    }
}

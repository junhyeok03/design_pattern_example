package com.example.weather.decorator;

import com.example.weather.Weather;
import com.example.weather.WeatherClient;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 같은 도시의 결과를 ttl 동안 재사용한다.
 * 실패는 예외로 그대로 전달되고 저장되지 않으므로, 일시적 장애가 캐시에 남지 않는다.
 */
public class CachingClient extends WeatherClientDecorator {

    private final Duration ttl;
    private final Clock clock;
    private final Map<String, CachedWeather> cache = new ConcurrentHashMap<>();

    public CachingClient(WeatherClient delegate, Duration ttl, Clock clock) {
        super(delegate);
        this.ttl = ttl;
        this.clock = clock;
    }

    public CachingClient(WeatherClient delegate, Duration ttl) {
        this(delegate, ttl, Clock.systemUTC());
    }

    @Override
    public Weather fetch(String city) {
        Instant now = clock.instant();
        CachedWeather cached = cache.get(city);
        if (cached != null && now.isBefore(cached.expiresAt())) {
            return cached.weather();
        }

        Weather weather = delegate.fetch(city);
        cache.put(city, new CachedWeather(weather, now.plus(ttl)));
        return weather;
    }

    private record CachedWeather(Weather weather, Instant expiresAt) {}
}

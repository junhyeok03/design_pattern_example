package com.example.weather.decorator;

import com.example.weather.Weather;
import com.example.weather.WeatherClient;

import java.util.function.Consumer;

/**
 * 요청마다 결과와 소요 시간을 한 줄로 기록한다.
 * 어느 층에 두느냐에 따라 "모든 요청"을 기록할지 "실제 API 호출"만 기록할지가 달라진다.
 */
public class LoggingClient extends WeatherClientDecorator {

    private final String name;
    private final Consumer<String> logger;

    public LoggingClient(WeatherClient delegate, String name, Consumer<String> logger) {
        super(delegate);
        this.name = name;
        this.logger = logger;
    }

    public LoggingClient(WeatherClient delegate, String name) {
        this(delegate, name, System.out::println);
    }

    @Override
    public Weather fetch(String city) {
        long start = System.nanoTime();
        try {
            Weather weather = delegate.fetch(city);
            logger.accept("[" + name + "] " + city + " → " + weather + " (" + elapsedMillis(start) + "ms)");
            return weather;
        } catch (RuntimeException e) {
            logger.accept("[" + name + "] " + city + " → 실패: " + e.getMessage() + " (" + elapsedMillis(start) + "ms)");
            throw e;
        }
    }

    private long elapsedMillis(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}

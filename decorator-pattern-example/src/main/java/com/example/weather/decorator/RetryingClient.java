package com.example.weather.decorator;

import com.example.weather.Weather;
import com.example.weather.WeatherApiException;
import com.example.weather.WeatherClient;

/**
 * 일시적 실패(WeatherApiException)일 때만 최대 maxAttempts번까지 다시 시도한다.
 * 잘못된 요청 같은 다른 예외는 다시 시도해도 결과가 같으므로 바로 던진다.
 */
public class RetryingClient extends WeatherClientDecorator {

    private final int maxAttempts;

    public RetryingClient(WeatherClient delegate, int maxAttempts) {
        super(delegate);
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts는 1 이상이어야 합니다.");
        }
        this.maxAttempts = maxAttempts;
    }

    @Override
    public Weather fetch(String city) {
        WeatherApiException lastFailure = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return delegate.fetch(city);
            } catch (WeatherApiException e) {
                lastFailure = e;
            }
        }
        throw lastFailure;
    }
}

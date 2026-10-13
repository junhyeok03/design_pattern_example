package com.example.weather;

/** 네트워크 오류처럼 다시 시도하면 성공할 수 있는 일시적 실패. */
public class WeatherApiException extends RuntimeException {

    public WeatherApiException(String message) {
        super(message);
    }
}

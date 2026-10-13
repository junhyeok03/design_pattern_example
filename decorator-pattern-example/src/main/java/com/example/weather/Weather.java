package com.example.weather;

/** 날씨 조회 결과. */
public record Weather(String city, int temperature, String condition) {

    @Override
    public String toString() {
        return city + " " + condition + " " + temperature + "°C";
    }
}

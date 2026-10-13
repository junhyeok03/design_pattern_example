package com.example.weather;

import com.example.weather.decorator.CachingClient;
import com.example.weather.decorator.LoggingClient;
import com.example.weather.decorator.RetryingClient;

import java.time.Duration;

/**
 * 같은 데코레이터를 다른 순서로 감쌌을 때 동작이 어떻게 달라지는지 보여준다.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== 1. 로그를 재시도 안쪽에: 시도마다 기록된다 ===");
        FakeWeatherApi api1 = new FakeWeatherApi();
        api1.failNext(2);
        WeatherClient client1 = new RetryingClient(new LoggingClient(api1, "api"), 3);
        client1.fetch("서울");

        System.out.println();
        System.out.println("=== 2. 로그를 재시도 바깥에: 요청당 한 번만 기록된다 ===");
        FakeWeatherApi api2 = new FakeWeatherApi();
        api2.failNext(2);
        WeatherClient client2 = new LoggingClient(new RetryingClient(api2, 3), "request");
        client2.fetch("서울");

        System.out.println();
        System.out.println("=== 3. 전체 조합: 캐시 → 재시도 → 로그 → API ===");
        FakeWeatherApi api3 = new FakeWeatherApi();
        api3.failNext(1);
        WeatherClient client3 =
                new CachingClient(
                        new RetryingClient(
                                new LoggingClient(api3, "api"),
                                3),
                        Duration.ofMinutes(10));
        System.out.println("첫 번째 요청: " + client3.fetch("부산"));
        System.out.println("두 번째 요청: " + client3.fetch("부산") + "  ← 캐시 적중, API 로그 없음");
        System.out.println("실제 API 호출 수: " + api3.callCount());
    }
}

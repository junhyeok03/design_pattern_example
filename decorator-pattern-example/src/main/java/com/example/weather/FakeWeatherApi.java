package com.example.weather;

/**
 * 데코레이터 패턴의 ConcreteComponent. 외부 날씨 API를 흉내 낸다.
 *
 * 테스트와 데모에서 결과를 예측할 수 있도록 실패 횟수를 직접 정할 수 있고,
 * 실제로 몇 번 호출됐는지 센다.
 */
public class FakeWeatherApi implements WeatherClient {

    private static final String[] CONDITIONS = {"맑음", "흐림", "비", "눈"};

    private int callCount;
    private int remainingFailures;

    @Override
    public Weather fetch(String city) {
        callCount++;

        // 잘못된 요청은 다시 시도해도 실패하므로 일시적 실패와 구분한다.
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("도시 이름이 비어 있습니다.");
        }
        if (remainingFailures > 0) {
            remainingFailures--;
            throw new WeatherApiException("날씨 서버가 응답하지 않습니다.");
        }

        int seed = Math.abs(city.hashCode());
        return new Weather(city, seed % 35 - 5, CONDITIONS[seed % CONDITIONS.length]);
    }

    /** 다음 times번의 호출을 일시적 실패로 만든다. */
    public void failNext(int times) {
        this.remainingFailures = times;
    }

    /** 실제 API까지 도달한 호출 수. */
    public int callCount() {
        return callCount;
    }
}

package com.example.weather.decorator;

import com.example.weather.FakeWeatherApi;
import com.example.weather.Weather;
import com.example.weather.WeatherApiException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoggingClientTest {

    private final FakeWeatherApi api = new FakeWeatherApi();
    private final List<String> logs = new ArrayList<>();
    private final LoggingClient client = new LoggingClient(api, "api", logs::add);

    @Test
    void 성공하면_결과를_기록하고_그대로_반환한다() {
        Weather weather = client.fetch("서울");

        assertThat(weather).isEqualTo(new FakeWeatherApi().fetch("서울"));
        assertThat(logs).singleElement().asString().startsWith("[api] 서울 → " + weather);
    }

    @Test
    void 실패하면_기록하고_예외를_다시_던진다() {
        api.failNext(1);

        assertThatThrownBy(() -> client.fetch("서울")).isInstanceOf(WeatherApiException.class);
        assertThat(logs).singleElement().asString().startsWith("[api] 서울 → 실패: 날씨 서버가 응답하지 않습니다.");
    }
}

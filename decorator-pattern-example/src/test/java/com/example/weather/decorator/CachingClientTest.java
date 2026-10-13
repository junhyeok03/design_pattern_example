package com.example.weather.decorator;

import com.example.weather.FakeWeatherApi;
import com.example.weather.WeatherApiException;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CachingClientTest {

    private final FakeWeatherApi api = new FakeWeatherApi();
    private final MutableClock clock = new MutableClock();
    private final CachingClient client = new CachingClient(api, Duration.ofMinutes(10), clock);

    @Test
    void 같은_도시는_두_번째부터_캐시에서_반환한다() {
        client.fetch("서울");
        client.fetch("서울");

        assertThat(api.callCount()).isEqualTo(1);
    }

    @Test
    void 도시마다_따로_캐시한다() {
        client.fetch("서울");
        client.fetch("부산");

        assertThat(api.callCount()).isEqualTo(2);
    }

    @Test
    void 유효시간이_지나면_다시_조회한다() {
        client.fetch("서울");
        clock.advance(Duration.ofMinutes(10));
        client.fetch("서울");

        assertThat(api.callCount()).isEqualTo(2);
    }

    @Test
    void 실패는_캐시하지_않는다() {
        api.failNext(1);

        assertThatThrownBy(() -> client.fetch("서울")).isInstanceOf(WeatherApiException.class);
        assertThat(client.fetch("서울").city()).isEqualTo("서울");
        assertThat(api.callCount()).isEqualTo(2);
    }
}

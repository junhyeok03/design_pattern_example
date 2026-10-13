package com.example.weather.decorator;

import com.example.weather.FakeWeatherApi;
import com.example.weather.WeatherApiException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RetryingClientTest {

    private final FakeWeatherApi api = new FakeWeatherApi();
    private final RetryingClient client = new RetryingClient(api, 3);

    @Test
    void 허용_횟수_안에서_성공하면_결과를_반환한다() {
        api.failNext(2);

        assertThat(client.fetch("서울").city()).isEqualTo("서울");
        assertThat(api.callCount()).isEqualTo(3);
    }

    @Test
    void 허용_횟수를_넘으면_마지막_실패를_던진다() {
        api.failNext(3);

        assertThatThrownBy(() -> client.fetch("서울")).isInstanceOf(WeatherApiException.class);
        assertThat(api.callCount()).isEqualTo(3);
    }

    @Test
    void 잘못된_요청은_다시_시도하지_않는다() {
        assertThatThrownBy(() -> client.fetch(" ")).isInstanceOf(IllegalArgumentException.class);
        assertThat(api.callCount()).isEqualTo(1);
    }

    @Test
    void 시도_횟수는_1_이상이어야_한다() {
        assertThatThrownBy(() -> new RetryingClient(api, 0)).isInstanceOf(IllegalArgumentException.class);
    }
}

package com.example.weather.decorator;

import com.example.weather.FakeWeatherApi;
import com.example.weather.WeatherClient;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 같은 데코레이터라도 감싸는 순서에 따라 동작이 달라진다.
 * 순서는 "무엇을 관찰하고 싶은가"에 따라 쓰는 쪽이 정한다.
 */
class DecoratorOrderTest {

    private final FakeWeatherApi api = new FakeWeatherApi();
    private final List<String> logs = new ArrayList<>();
    private final MutableClock clock = new MutableClock();

    @Test
    void 로그가_재시도_안쪽이면_시도마다_기록된다() {
        api.failNext(2);
        WeatherClient client = new RetryingClient(new LoggingClient(api, "api", logs::add), 3);

        client.fetch("서울");

        assertThat(logs).hasSize(3);
        assertThat(logs.subList(0, 2)).allMatch(line -> line.contains("실패"));
    }

    @Test
    void 로그가_재시도_바깥이면_요청당_한_번만_기록된다() {
        api.failNext(2);
        WeatherClient client = new LoggingClient(new RetryingClient(api, 3), "request", logs::add);

        client.fetch("서울");

        assertThat(logs).singleElement().asString().doesNotContain("실패");
        assertThat(api.callCount()).isEqualTo(3);
    }

    @Test
    void 로그가_캐시_바깥이면_캐시_적중도_기록된다() {
        WeatherClient client = new LoggingClient(
                new CachingClient(api, Duration.ofMinutes(10), clock), "request", logs::add);

        client.fetch("서울");
        client.fetch("서울");

        assertThat(logs).hasSize(2);
        assertThat(api.callCount()).isEqualTo(1);
    }

    @Test
    void 로그가_캐시_안쪽이면_실제_API_호출만_기록된다() {
        WeatherClient client = new CachingClient(
                new LoggingClient(api, "api", logs::add), Duration.ofMinutes(10), clock);

        client.fetch("서울");
        client.fetch("서울");

        assertThat(logs).hasSize(1);
        assertThat(api.callCount()).isEqualTo(1);
    }

    @Test
    void 여러_겹을_감싸도_쓰는_쪽에서는_같은_WeatherClient다() {
        api.failNext(1);
        WeatherClient client =
                new CachingClient(
                        new RetryingClient(
                                new LoggingClient(api, "api", logs::add),
                                3),
                        Duration.ofMinutes(10), clock);

        assertThat(client.fetch("부산").city()).isEqualTo("부산");
        assertThat(client.fetch("부산").city()).isEqualTo("부산");
        assertThat(api.callCount()).isEqualTo(2);   // 실패 1 + 성공 1, 두 번째 요청은 캐시
        assertThat(logs).hasSize(2);
    }
}

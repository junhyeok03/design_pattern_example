package com.example.weather.decorator;

import com.example.weather.Weather;
import com.example.weather.WeatherClient;

import java.util.Objects;

/**
 * 데코레이터 패턴의 Decorator.
 * 같은 인터페이스를 구현하면서, 안쪽의 WeatherClient를 감싸 호출을 위임한다.
 * 하위 데코레이터는 fetch를 재정의해 위임 전후에 기능을 덧붙인다.
 */
public abstract class WeatherClientDecorator implements WeatherClient {

    protected final WeatherClient delegate;

    protected WeatherClientDecorator(WeatherClient delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public Weather fetch(String city) {
        return delegate.fetch(city);
    }
}

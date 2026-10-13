package com.example.weather;

/**
 * 데코레이터 패턴의 Component.
 * 실제 API 클라이언트와 모든 데코레이터가 이 인터페이스를 구현하므로,
 * 쓰는 쪽은 몇 겹으로 감싸져 있는지 알 필요가 없다.
 */
public interface WeatherClient {
    Weather fetch(String city);
}

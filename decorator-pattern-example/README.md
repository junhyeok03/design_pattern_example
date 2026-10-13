# 데코레이터 패턴 예제: 외부 API 클라이언트 꾸미기

외부 날씨 API 클라이언트에 **로깅 · 재시도 · 캐시**를 한 겹씩 씌우는 예제입니다.
원래 클라이언트와 쓰는 쪽 코드를 고치지 않고 기능을 덧붙이고, 감싸는 순서로 동작을 조절합니다.

```java
WeatherClient client =
        new CachingClient(          // 같은 요청이면 캐시에서 반환
                new RetryingClient(     // 일시적 실패면 최대 3번 시도
                        new LoggingClient(  // 실제 API 호출과 소요 시간 기록
                                new FakeWeatherApi(), "api"),
                        3),
                Duration.ofMinutes(10));

client.fetch("서울");   // 쓰는 쪽은 몇 겹인지 모른다
```

## 실행

```bash
./gradlew test   # 테스트
./gradlew run    # 순서별 동작 데모
```

Java 17, Gradle 9.6.1 (wrapper 포함), JUnit 5, AssertJ

## 구조

| 패턴의 역할 | 이 예제 |
| --- | --- |
| Component | `WeatherClient` 인터페이스 |
| ConcreteComponent | `FakeWeatherApi` (실패 횟수를 조절할 수 있는 가짜 API) |
| Decorator | `WeatherClientDecorator` (안쪽 클라이언트를 들고 위임) |
| ConcreteDecorator | `LoggingClient`, `RetryingClient`, `CachingClient` |

```
src/main/java/com/example/weather
├─ WeatherClient, Weather, WeatherApiException, FakeWeatherApi
├─ decorator/        WeatherClientDecorator, LoggingClient, RetryingClient, CachingClient
├─ withoutpattern/   AllInOneWeatherClient  (비교용: 한 클래스에 다 넣은 버전)
└─ Main              순서별 동작 데모
```

## 핵심: 감싸는 순서가 동작을 바꾼다

같은 데코레이터라도 어느 층에 두느냐에 따라 결과가 달라집니다. `DecoratorOrderTest`에서 검증합니다.

| 조합 (바깥 → 안쪽) | 결과 |
| --- | --- |
| `Retrying(Logging(api))` | 시도마다 로그. 실패 2번 + 성공 1번 = **3줄** |
| `Logging(Retrying(api))` | 요청당 로그. 재시도는 감춰지고 **1줄** |
| `Logging(Caching(api))` | 캐시 적중까지 **모든 요청** 기록 |
| `Caching(Logging(api))` | **실제 API 호출만** 기록 |

순서는 "무엇을 관찰하고 싶은가"에 따라 쓰는 쪽이 정합니다. 장애 분석에는 시도마다 남는 로그가, 사용량 집계에는 요청당 로그가 필요합니다.

`./gradlew run` 출력:

```
=== 1. 로그를 재시도 안쪽에: 시도마다 기록된다 ===
[api] 서울 → 실패: 날씨 서버가 응답하지 않습니다. (0ms)
[api] 서울 → 실패: 날씨 서버가 응답하지 않습니다. (0ms)
[api] 서울 → 서울 맑음 18°C (2ms)

=== 2. 로그를 재시도 바깥에: 요청당 한 번만 기록된다 ===
[request] 서울 → 서울 맑음 18°C (0ms)

=== 3. 전체 조합: 캐시 → 재시도 → 로그 → API ===
[api] 부산 → 실패: 날씨 서버가 응답하지 않습니다. (0ms)
[api] 부산 → 부산 맑음 0°C (0ms)
첫 번째 요청: 부산 맑음 0°C
두 번째 요청: 부산 맑음 0°C  ← 캐시 적중, API 로그 없음
실제 API 호출 수: 2
```

## 데코레이터 없이 만들면

`withoutpattern/AllInOneWeatherClient`는 같은 기능을 플래그와 if로 한 클래스에 넣은 버전입니다.

- 기능을 추가할 때마다 이 클래스를 고쳐야 하고, 플래그와 분기가 늘어납니다.
- 순서가 코드에 고정됩니다. "캐시 적중도 로그로 남기기"를 하려면 플래그가 또 필요합니다.
- 로깅만, 캐시만 따로 테스트하기 어렵습니다.

## 왜 상속이 아니라 감싸기인가

상속으로 하면 `LoggingRetryingCachingWeatherClient`처럼 조합마다 클래스가 필요하고, 기능이 n개면 조합은 2ⁿ개로 늘어납니다.
감싸기는 기능마다 클래스 하나씩만 두고, 조합과 순서는 객체를 만들 때 정합니다.

## 설계에서 신경 쓴 부분

- **재시도는 일시적 실패만**: `WeatherApiException`만 다시 시도합니다. 빈 도시 이름 같은 잘못된 요청(`IllegalArgumentException`)은 다시 해도 같으므로 바로 던집니다.
- **실패는 캐시하지 않음**: 예외는 저장되지 않고 그대로 전달되므로, 일시적 장애가 캐시에 남지 않습니다.
- **시간은 `Clock`으로 주입**: 캐시 만료를 실제로 기다리지 않고 테스트합니다(`MutableClock`).
- **로그 출력은 `Consumer<String>`으로 주입**: 테스트에서 로그 줄 수를 셀 수 있습니다.

## 실무에서 만나는 데코레이터

- **Java I/O**: `new BufferedReader(new InputStreamReader(new FileInputStream(file)))`도 같은 구조입니다.
- **Spring AOP**: `@Cacheable`, `@Transactional`, `@Retryable`은 Spring이 런타임에 프록시로 만들어 주는 데코레이터입니다.
  그래서 같은 클래스 안에서 자기 메서드를 호출하면(`this.method()`) 프록시를 거치지 않아 적용되지 않습니다.
- **Resilience4j**: `Decorators.ofSupplier(...).withRetry(...).withCircuitBreaker(...)`처럼 이름부터 데코레이터입니다.

## 한계

- `CachingClient`는 크기 제한이 없습니다. 실제로는 Caffeine 같은 캐시 라이브러리로 크기·만료 정책을 관리합니다.
- `RetryingClient`는 시도 사이 대기(backoff)가 없습니다. 실제 외부 API라면 지수 백오프가 필요합니다.
- 층이 많아지면 호출 흐름을 추적하기 어려워집니다. 조립 코드를 한곳(팩토리나 설정 클래스)에 모아두는 것이 좋습니다.

# 싱글톤 패턴 예제

싱글톤이 왜 필요한지, 직접 만들면 무엇이 불편한지, 스프링이 어떻게 해결하는지, 그리고 싱글톤을 쓸 때 무엇을 조심해야 하는지 순서대로 봅니다.

| 단계 | 위치 | 내용 |
| --- | --- | --- |
| 1 | `step1_nocontainer` | 순수 DI 컨테이너는 부를 때마다 새 객체를 만든다 |
| 2 | `step2_singleton` | 싱글톤 패턴: 객체를 하나만 만들어 공유한다 |
| 3 | `step3_drawbacks` | 싱글톤 패턴의 단점: 구체 클래스 의존, 숨은 의존, 상태 섞임 |
| 4 | Memora `BeanScopeTests` | 스프링 컨테이너는 싱글톤 패턴 없이도 객체를 하나로 관리한다 |
| 5 | `step5_stateful` + Memora | 싱글톤은 공유되므로 요청마다 바뀌는 필드를 두면 안 된다 |

## 실행

```bash
./gradlew test   # 테스트 (실행 결과가 콘솔에 함께 출력됨)
./gradlew run    # 단계별 데모
```

Java 17, Gradle 9.6.1 (wrapper 포함), JUnit 5, AssertJ

## 1. 부를 때마다 새 객체

```java
public UserService userService() {
    return new UserService(new MemoryUserRepository());
}
```
```
userService1 = com.example.singleton.user.UserService@8bcc55f
userService2 = com.example.singleton.user.UserService@14dad5dc
같은 객체인가? false
```

웹 서버는 요청을 동시에 많이 받습니다. 요청마다 서비스 객체를 새로 만들면 만들고 버리는 비용이 계속 듭니다.
그런데 `UserService`는 요청마다 달라지는 값이 없으니 하나만 만들어 함께 써도 됩니다.

## 2. 싱글톤 패턴

```java
private static final SingletonService INSTANCE = new SingletonService();
private SingletonService() { }                       // 밖에서 new 금지
public static SingletonService getInstance() { return INSTANCE; }
```
```
singleton1 = com.example.singleton.step2_singleton.SingletonService@15db9742
singleton2 = com.example.singleton.step2_singleton.SingletonService@15db9742
같은 객체인가? true
```

## 3. 싱글톤 패턴의 단점

`ReminderService`는 `SmsGateway.getInstance()`를 코드 안에서 직접 부릅니다.

- **구체 클래스에 의존**: 카카오톡 발송기나 테스트용 가짜로 바꿀 방법이 없습니다.
- **숨은 의존**: 생성자만 봐서는 문자를 보내는 클래스인지 알 수 없습니다.
- **상태 섞임**: 서로 모르는 두 `ReminderService`가 보낸 기록이 전역 객체 하나에 쌓입니다. 테스트끼리도 기록이 섞여서, 테스트에서 "몇 개 보냈는지"를 셀 때 이전 기록을 빼고 세야 합니다.
- **private 생성자**: 상속해서 동작을 바꿀 수 없습니다.

`InjectedReminderService`처럼 생성자로 발송기를 받으면 테스트에서는 가짜를, 운영에서는 싱글톤 발송기를 그대로 넣을 수 있습니다.
**객체는 하나만 유지하면서 의존은 밖에서 넣어 주는 것**, 이게 스프링 컨테이너가 하는 일입니다.

## 4. 스프링 컨테이너 (Memora)

Memora의 `src/test/java/com/memora/server/singleton/BeanScopeTests.java`에서 확인합니다.

```
userService1 = com.memora.server.service.UserService@14b849b9
userService2 = com.memora.server.service.UserService@14b849b9     ← 같은 객체
prototype1   = ...BeanScopeTests$PrototypeSample@50d4b0eb
prototype2   = ...BeanScopeTests$PrototypeSample@5d408b09         ← 다른 객체
```

- `UserService`에는 `private` 생성자도 `getInstance()`도 없는데, 컨테이너가 하나만 만들어 관리합니다.
- `@Scope("prototype")`을 붙이면 꺼낼 때마다 새로 만듭니다.
- `UserService`는 `@Transactional` 때문에 컨테이너가 원본을 감싼 프록시를 보관합니다. 데코레이터 예제의 "감싸기"와 같은 원리입니다.

## 5. 싱글톤의 주의점: 상태를 갖지 말 것

### 5-1. 필드에 값을 저장하면 다른 사용자가 덮어쓴다

```java
moodService.record("김순자", "최고에요");
moodService.record("박영수", "슬퍼요");
moodService.lastMood();   // "슬퍼요"  ← 김순자가 자기 기분을 확인했는데 박영수의 기분이 나온다
```

`StatelessMoodService`처럼 필드에 저장하지 않고 결과를 바로 돌려주면 섞이지 않습니다.

### 5-2. 실제 서비스 코드에서 찾은 사례

Memora의 싱글턴 빈 중 요청마다 바뀌는 필드는 두 곳뿐이었습니다. 둘 다 `ConcurrentHashMap<사용자ID, ArrayDeque>` 구조입니다.

| | Memora 원본 | 이 예제 | 같은 사용자의 동시 요청 |
| --- | --- | --- | --- |
| 최근 출제 기록 | `QuizService.recentQuizzes` | `UnsafeRecentHistory` | ❌ `ArrayDeque`를 잠그지 않고 고친다 |
| AI 호출 기록 | `AiRateLimiter.userCalls` | `SafeRecentHistory` | ✅ 사용자별 `synchronized` 블록 안에서만 고친다 |

`./gradlew run` 결과 (8개 스레드가 같은 사용자로 2,000번씩, 20회 반복):
```
Unsafe (QuizService 방식) → 예외 57358건, 최종 기록이 10개가 아닌 회차 15/20
Safe   (AiRateLimiter 방식) → 예외 0건, 최종 기록이 10개가 아닌 회차 0/20
```

숫자는 실행할 때마다 달라집니다. 바깥 맵이 `ConcurrentHashMap`이어도 **안에 든 `ArrayDeque`까지 안전해지지는 않습니다.**
실제 서비스에서는 이 실험처럼 요청이 몰리지 않으므로 발생 확률은 훨씬 낮습니다. 다만 같은 사용자가 버튼을 연타하면 일어날 수 있습니다.

`Unsafe` 쪽은 결과가 매번 달라지는 불안정한 테스트가 되므로 테스트 대신 데모(`Main`)로만 보여줍니다.
`Safe` 쪽은 결과가 항상 같으므로 테스트(`StatefulServiceTest`)로 확인합니다.

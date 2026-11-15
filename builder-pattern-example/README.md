# Builder 패턴 예제: 커스텀 게임 방 만들기

> 리그 오브 레전드의 **실제 구현이 아니라**, 커스텀 게임 설정 화면을 예제로 빌려 온 것입니다.
> 맵과 모드 조합, 인원 같은 규칙도 실제 게임과 다르게 단순하게 지어냈습니다.
> 지난 Prototype 예제(미니언 웨이브)에 이어지는 시리즈입니다.

## 서브웨이로 먼저 보기

- 빵, 치즈, 야채, 소스를 **하나씩 고르고 마지막에 샌드위치를 받는 것** = 빌더 (2단계)
- 같은 주문을 **매장 직원이 들으면 샌드위치, 배달 앱이 들으면 영수증** = GoF 빌더 (3단계)

"빌더"라는 이름 아래 사실은 **목적이 다른 두 가지**가 있습니다. 이 예제는 둘을 나눠서 봅니다.

방 이름은 필수이고, 맵, 모드, 팀 인원, 비밀번호, 관전 허용, 봇 추가는 선택입니다.

## 단계

| 단계 | 패키지 | 장면 |
|---|---|---|
| 1 | `step1_nopattern` | **A. 생성자**: `new CustomGame("내전 하실 분", ..., 5, null, false, true)`. 관전과 봇의 자리를 바꿔 써도 컴파일되고, 봇이 켜진 방이 조용히 만들어진다. **B. setter**: 이름만 넣은 "반쯤 된 방"이 존재하고, 다 만든 뒤에도 비밀번호가 사라질 수 있다 |
| 2 | `step2_builder` | `CustomGame.builder("내전 하실 분").map(...).allowSpectators(true).build()`. 값마다 이름이 붙고, 안 고른 건 기본값, `build()`에서 규칙을 한 번에 검사, 완성된 방은 불변 |
| 3 | `step3_gof_builder` | `WeekendScrimDirector`가 같은 순서로 단계를 부른다. 빌더만 바꿔 넣으면 서버 설정 객체와 디스코드 초대 메시지가 나온다 |

## 핵심 코드

```java
// 1단계: 마지막 두 boolean이 각각 무엇인지 보이지 않는다. 바꿔 써도 컴파일된다
new CustomGame("내전 하실 분", Map.HOWLING_ABYSS, Mode.ALL_RANDOM, 5, null, false, true);

// 2단계: 필수는 builder()의 인자로, 선택은 이름 붙은 메서드로, 검사는 build()에서
CustomGame.builder("내전 하실 분")
        .map(Map.HOWLING_ABYSS)
        .mode(Mode.ALL_RANDOM)
        .allowSpectators(true)
        .build();

// 3단계: Director 코드는 그대로, 빌더만 바꿔 넣는다
director.construct(new ServerConfigBuilder(), "lol2026");   // → ServerRoomConfig 객체
director.construct(new DiscordInviteBuilder(), "lol2026");  // → "[내전 모집] ..." 메시지
```

## 헤드 퍼스트 빌더(휴가 계획표)와 비교

| 헤드 퍼스트 | 이 예제 (3단계) | 하는 일 |
|---|---|---|
| `Client` | `WeekendScrimDirector` (+ 그걸 부르는 `Main`) | 어떤 순서로 만들지 안다 |
| `constructPlanner()` | `WeekendScrimDirector.construct()` | 단계를 순서대로 부른다 |
| `AbstractBuilder` | `RoomBuilder` | 단계 목록 (`title()`, `map()`, `mode()` ...) |
| `VacationBuilder` | `ServerConfigBuilder`, `DiscordInviteBuilder` | 각 단계를 자기 방식으로 처리한다 |
| `getVacationPlanner()` | 각 빌더의 `getResult()` | 완성품을 꺼낸다 |

책과 다른 점이 하나 있습니다. 책은 결과를 꺼내는 메서드를 `AbstractBuilder`에 두었지만, 이 예제는 **구현 클래스마다** 둡니다.
결과물 타입이 빌더마다 다르기 때문입니다(`ServerRoomConfig`, `String`). GoF 원서의 RTF 변환기 예제도 같은 이유로 이렇게 합니다.

## 2단계와 3단계, 같은 "빌더"지만 목적이 다르다

| | 2단계 (이펙티브 자바 아이템 2) | 3단계 (GoF / 헤드 퍼스트) |
|---|---|---|
| 푸는 문제 | 선택 값이 많은 객체를 **읽기 쉽고 안전하게** 만들기 | 같은 조립 순서로 **다른 결과물** 만들기 |
| 결과물 | `CustomGame` 하나 | 서버 설정 객체, 디스코드 메시지 ... 여러 종류 |
| 순서는 누가 | 호출하는 쪽이 자유롭게 (`.map().mode()`든 `.mode().map()`이든) | Director가 정한다 |
| 생김새 | 메서드 체이닝 + `build()` | 인터페이스 + 구현 여러 개 + Director |
| 실무에서 보는 빈도 | 아주 흔함 | 드묾 |

## 알아 둘 점 (과한 부분은 솔직하게)

- **설정이 2~3개뿐이면 2단계 빌더는 과합니다.** 생성자 하나로 충분합니다. 빌더는 `Builder` 클래스만큼 코드가 두 배가 됩니다.
- **결과물이 한 종류뿐이면 3단계 GoF 빌더는 과합니다.** Director와 인터페이스를 나눌 이유가 없습니다.
- **2단계는 "실수를 줄이는" 것이지 "막는" 것은 아닙니다.** `teamSize(6)`은 컴파일되고, `build()`를 실행해야 알 수 있습니다. 1단계와 달리 **조용히 틀리지 않고 큰 소리로 실패한다**는 점이 다릅니다.
- **3단계에는 규칙 검사가 없습니다.** GoF 빌더의 목적은 검사가 아니라 표현 분리이기 때문입니다. 필요하면 `ServerConfigBuilder.getResult()` 안에서 2단계 빌더를 써서 검사까지 받을 수 있습니다.
- **3단계 빌더는 한 번 쓰고 버립니다.** 같은 빌더로 `construct()`를 두 번 부르면 앞의 값이 덮입니다.

## 실무에서는

- **Lombok `@Builder`**: 2단계의 `Builder` 클래스를 자동으로 만들어 줍니다. 다만 기본 `builder()`는 인자가 없어서, "방 이름은 필수" 같은 강제는 직접 챙겨야 합니다.
- **`HttpRequest.newBuilder()`** (자바 11+): `.uri(...).header(...).GET().build()`. 2단계와 같은 모양입니다.
- **`StringBuilder`**: `append()`로 조금씩 쌓고 `toString()`으로 완성합니다. 이름과 "쌓고 마지막에 꺼낸다"는 느낌은 같지만, 검사도 Director도 없는 단순한 문자열 도구입니다.

## 실행

```bash
./gradlew run     # 1단계 → 2단계 → 3단계 데모
./gradlew test    # 테스트
```

# Builder 패턴 예제 (GoF): 경기 결과 리포트

> 리그 오브 레전드의 **실제 구현이 아니라**, 경기 결과라는 소재를 예제로 빌려 온 것입니다.

경기 결과 하나를 **같은 순서로 조립**하되, 결과물은 텍스트와 마크다운처럼 **형식만 다르게** 만듭니다.
GoF Builder의 "만드는 과정과 결과물의 표현을 분리한다"를 보여 주는 것이 목표입니다.

| GoF | 이 예제 |
|---|---|
| Director | `MatchReportDirector`: 조립 순서와 MVP 규칙을 앎 |
| Builder | `ReportBuilder`: `buildHeader`, `buildMvp`, `buildTeam` |
| ConcreteBuilder | `TextReportBuilder`, `MarkdownReportBuilder` |
| Product | 각 Builder의 `getResult()` 결과 |

## 단계

| 단계 | 패키지 | 장면 |
|---|---|---|
| 1 | `step1_nopattern` | `toText()`, `toMarkdown()`이 순서와 MVP 규칙을 각자 가짐. 기획 요청으로 MVP를 위로 올리면서 텍스트만 고치고 마크다운은 깜빡함 |
| 2 | `step2_builder` | 순서와 규칙은 Director 한 곳에만 있음. 형식은 Builder가 맡음. 두 형식의 순서가 항상 같음 |

## 알아 둘 점

- **`getResult()`는 인터페이스에 없습니다.** 형식마다 결과물 타입이 다를 수 있어서(문자열, HTML 문서 객체, 이미지 ...) 구현 클래스마다 둡니다.
- **새 형식(HTML 등)은 Builder 클래스 하나만 추가**하면 됩니다. Director는 그대로입니다.
- **Director를 바꾸면** 같은 Builder로 다른 구성(예: 헤더와 MVP만 있는 요약 리포트)도 만들 수 있습니다. 조립 과정과 표현이 서로 독립적으로 바뀝니다.
- **이펙티브 자바의 빌더(`Xxx.builder().a(1).b(2).build()`)와는 다릅니다.** 그쪽은 매개변수가 많은 객체를 읽기 쉽게 만드는 문법이고, GoF Builder는 조립 과정과 표현을 분리하는 구조입니다. 이름만 같습니다.
- **형식이 하나뿐이면 과합니다.** Director와 Builder를 나눌 이유가 없습니다.

## 실행

```bash
./gradlew run
./gradlew test
```

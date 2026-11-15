# Prototype 패턴 예제: 미니언 웨이브

> 리그 오브 레전드의 **실제 구현이 아니라**, 미니언 웨이브라는 게임 규칙을 예제로 빌려 온 것입니다.
> 체력, 공격력 같은 수치도 실제 게임과 다릅니다.

넥서스는 웨이브마다 근거리 3, 원거리 3, 대포 1마리를 내보냅니다.
억제기가 부서지면 게임 도중에 슈퍼 미니언이 새로 추가됩니다.

헤드 퍼스트 디자인 패턴 14장의 Prototype 시나리오(몬스터 레지스트리)를 이 상황에 옮겼고,
책처럼 자바의 `clone()`으로 복제합니다.

| 책 | 이 예제 |
|---|---|
| `Monster` | `Minion` (`Cloneable`, `public Minion clone()`) |
| `WellKnownMonster` | `MeleeMinion`, `CasterMinion`, `SiegeMinion` |
| `DynamicPlayerGeneratedMonster` | `SuperMinion` (억제기가 부서진 뒤에 등록), 바론 버프로 만든 "강화 미니언" (클래스 없이 실행 중에 생성) |
| `MonsterRegistry` | `MinionRegistry` |

Prototype을 쓰는 이유는 두 가지이고, 단계마다 보여 주는 것이 다릅니다.

- **만드는 비용이 크다** → 만들어 둔 것을 복제한다 (1~3단계)
- **무엇을 만들지가 실행 중에 정해진다** → 만드는 쪽이 구체 클래스를 몰라도 된다 (4단계)

## 단계

| 단계 | 패키지 | 장면 |
|---|---|---|
| 1 | `step1_nopattern` | 웨이브마다 종류별 `if`로 `new`. 모델 로딩 1초 × 7마리 = 웨이브 하나에 약 7초 |
| 2 | `step2_shallowclone` | 원본을 `clone()`. 웨이브 하나는 거의 0초. 그런데 한 마리에게 건 둔화가 같은 종류 전체와 다음 웨이브까지 퍼진다 |
| 3 | `step3_deepclone` | `clone()`에서 상태이상 리스트를 새로 만든다. 둔화는 맞은 한 마리만 |
| 4 | `step4_registry` | `MinionRegistry`. 억제기 파괴 후 슈퍼 미니언 원본을 등록하면, 다음 웨이브부터 섞여 나온다. 바론 버프가 오면 기존 원본을 복제해 공격력만 올린 객체를 "강화 OO"로 등록한다 (새 클래스 없음) |

## 핵심 코드

```java
// 2단계: super.clone()은 생성자를 거치지 않는다 → 모델을 다시 로드하지 않아 빠르다
//        대신 필드를 그대로 복사한다 → 상태이상 리스트를 원본과 함께 쓴다 (얕은 복사)
return (Minion) super.clone();

// 3단계: 바뀌는 것만 새로 만든다. 바뀌지 않는 Model은 그대로 공유한다
Minion copy = (Minion) super.clone();
copy.statusEffects = new ArrayList<>(statusEffects);
return copy;

// 4단계: 웨이브를 내보내는 쪽에는 new도, 구체 클래스 이름도 없다
return waveComposition.stream().map(registry::create).toList();

// 4단계 바론 버프: 원본을 복제해 고친 객체가 새 원본이 된다 (클래스는 늘지 않는다)
Minion buffed = registry.create(type);
buffed.increaseAttack(BARON_ATTACK_BONUS);
registry.register(BUFFED + type, buffed);
```

## 알아 둘 점

- **2단계 버그는 "같은 종류" 안에서 퍼집니다.** 근거리 미니언들은 근거리 원본의 리스트를 함께 쓰고, 원거리 미니언은 원거리 원본의 리스트를 씁니다. 그래서 근거리 한 마리에게 건 둔화가 원거리에는 퍼지지 않습니다.
- **깊은 복사가 "전부 새로 만들기"는 아닙니다.** `Model`은 바뀌지 않으니 공유하는 게 맞습니다. 이것까지 새로 만들면 다시 1초씩 걸립니다.
- **`clone()`의 비용:** 3단계에서 `statusEffects`에 새 리스트를 넣으려고 `final`을 뗐습니다. `super.clone()` 뒤에는 `final` 필드에 값을 다시 넣을 수 없기 때문입니다. 체크 예외인 `CloneNotSupportedException`도 `clone()` 안에서 처리합니다. 이펙티브 자바(아이템 13)가 복사 생성자를 권하는 이유입니다.
- **`clone()`의 장점:** `super.clone()`이 실제 클래스의 필드를 전부 복사하므로, 하위 클래스(`MeleeMinion` 등)에는 복제 코드가 한 줄도 없습니다.

## 실행

```bash
./gradlew run     # 데모 (모델 로딩을 실제로 1초씩 기다려서 17초쯤 걸림)
./gradlew test    # 테스트 (모델 로딩을 10ms로 줄여서 실행)
```

# 커맨드 패턴 ex02 설명 정리

> ex02(텍스트 에디터 undo/redo) 하나만으로 커맨드 패턴을 설명하기 위한 정리 노트.
> 블로그 본문은 `blog-command-pattern.txt`에 따로 있다.

---

## 1. ex02 하나로 가도 되는가

**결론: ex02 하나로 충분하고, ex01보다 낫다.**

### ex02가 나은 이유

- 패턴의 네 역할이 코드에 다 있고, 주석으로도 표시되어 있다.
  - Client: `Main`
  - Invoker: `TextEditorInvoker`
  - Command: `Command`, `InsertTextCommand`, `DeleteTextCommand`
  - Receiver: `TextEditor`
- 입문자가 가장 많이 하는 질문은 "그냥 `light.turnOn()` 부르면 되지 왜 객체로 감싸?"다. ex01만으로는 여기에 답하기 어렵다. ex02는 undo/redo로 바로 답을 보여준다. 요청을 객체로 만들었기 때문에 저장하고, 되돌리고, 다시 실행할 수 있다.

### ex01을 빼면 잃는 것과 보완 방법

- 잃는 것: `execute()` 하나만 있는 가장 단순한 형태에서 시작하는 단계.
- 보완 방법: ex02를 두 단계로 나눠 설명한다.
  1. `execute()`와 `InsertTextCommand`만 먼저 보여준다.
  2. 그다음 `undo()`와 두 개의 스택을 더한다.

---

## 2. 실행 결과

```
Current text: Hello, World!
After undo: Hello, 
After redo: Hello, World!
After delete: World!
Final text: Hello, World!
```

(`After undo: Hello, ` 줄은 끝에 공백이 하나 있다.)

---

## 3. 왜 이게 커맨드 패턴인가

클래스 이름에 Command가 붙어 있다고 커맨드 패턴이 되는 것은 아니다. 아래 세 가지 조건으로 판단한다.

1. 요청(메서드 호출)이 객체가 되었다.
2. 요청을 보내는 쪽은 그 요청이 실제로 무슨 일을 하는지 모른다.
3. 그래서 요청을 저장하고, 미뤘다가 실행하고, 되돌릴 수 있다.

### 3-1. 요청이 객체가 되었다

패턴이 없다면:

```java
editor.insertText("Hello, ", 0);   // 호출하는 순간 실행되고 끝, 아무 기록도 남지 않음
```

ex02에서는:

```java
Command insertHello = new InsertTextCommand(editor, "Hello, ", 0);
```

| 메서드 호출의 구성 | 커맨드 객체의 필드 |
|---|---|
| 누구에게 | `editor` |
| 무엇을 | `text` |
| 어디에 | `position` |

객체를 만드는 순간에는 아무 일도 일어나지 않는다. 재료만 저장해 두고, 실행은 `execute()`를 부를 때 한다. **호출을 변수에 담을 수 있는 값으로 바꾼 것**이 핵심이다.

### 3-2. Invoker는 내용을 모른다

```java
public void executeCommand(Command command) {
    command.execute();          // 넣기인지 지우기인지 모른다
    undoStack.push(command);
    redoStack.clear();
}
```

- `TextEditorInvoker` 코드에는 `TextEditor`도 `InsertTextCommand`도 등장하지 않는다.
- Invoker가 아는 것은 `Command` 인터페이스의 `execute()`와 `undo()`뿐이다.
- 그래서 `ReplaceTextCommand`처럼 새 커맨드를 추가해도 Invoker는 한 줄도 바뀌지 않는다.

### 3-3. 객체가 되었기 때문에 저장·지연·취소가 가능하다

- **저장**: 객체라서 `undoStack.push(command)`로 쌓을 수 있다.
- **취소**: 각 커맨드는 되돌리는 방법(`undo()`)과 되돌리는 데 필요한 정보를 함께 들고 있다.
  - `InsertTextCommand`: 넣은 글자를 알고 있으므로 그만큼 지운다.
  - `DeleteTextCommand`: 지운 글자를 `deletedText`에 기억해 두었다가 다시 넣는다.
- **재실행**: redo는 새 객체를 만들지 않고, 저장해 둔 같은 객체의 `execute()`를 한 번 더 부른다.

### 3-4. 네 가지 역할 대응표

| GoF 역할 | ex02 클래스 | 이것만 알면 된다 | 식당 비유 |
|---|---|---|---|
| Command | `Command` 인터페이스 | `execute()`와 `undo()`라는 약속 | 주문서 양식 |
| ConcreteCommand | `InsertTextCommand`, `DeleteTextCommand` | 요청 하나와 그것을 되돌리는 방법 | 실제 주문서 |
| Receiver | `TextEditor` | 실제 일(글자 넣기·지우기). undo는 전혀 모름 | 요리사 |
| Invoker | `TextEditorInvoker` | 실행 시점과 기록. 요청의 내용은 모름 | 종업원 |
| Client | `Main` | 커맨드를 조립해서 Invoker에 넘김 | 손님 |

### 3-5. 헷갈리기 쉬운 지점

- **undo가 있어야만 커맨드 패턴인 것은 아니다.** ex01처럼 `execute()` 하나만 있어도 커맨드 패턴이다. undo는 이 패턴으로 할 수 있는 대표적인 일이다.
- **Stack 두 개는 패턴의 일부가 아니다.** Invoker 내부의 구현 방식일 뿐이다. 큐에 쌓아 순서대로 처리하거나 로그에 남겼다가 다시 실행해도 커맨드 패턴이다.
- **`Main`이 `editor.getContent()`를 직접 부르는 것은 위반이 아니다.** 상태를 바꾸지 않는 조회라서 커맨드로 감쌀 필요가 없다. 반대로 `insertText()`처럼 상태를 바꾸는 호출을 직접 하면 undo 기록이 어긋난다.

**한 줄 요약:** `editor.insertText(...)`라는 호출을 `InsertTextCommand` 객체로 바꿨고, Invoker가 그 객체의 내용을 모른 채 실행·저장·취소를 하기 때문에 커맨드 패턴이다.

---

## 4. 스택 변화 추적

스택은 왼쪽이 바닥, 오른쪽이 맨 위.

| 단계 | 호출 | 에디터 본문 | undoStack | redoStack |
|---|---|---|---|---|
| 1 | `executeCommand(insertHello)` | `Hello, ` | [insertHello] | [] |
| 2 | `executeCommand(insertWorld)` | `Hello, World!` | [insertHello, insertWorld] | [] |
| 3 | `undo()` → `insertWorld.undo()` | `Hello, ` | [insertHello] | [insertWorld] |
| 4 | `redo()` → `insertWorld.execute()` | `Hello, World!` | [insertHello, insertWorld] | [] |
| 5 | `executeCommand(deleteCommand)` | `World!` | [insertHello, insertWorld, deleteCommand] | [] |
| 6 | `undo()` → `deleteCommand.undo()` | `Hello, World!` | [insertHello, insertWorld] | [deleteCommand] |

- 3단계: `insertWorld.undo()`는 7번 자리부터 `"World!"` 길이인 6글자를 지운다.
- 4단계: 새 객체를 만들지 않고 같은 `insertWorld` 객체를 재사용한다.
- `insertWorld`의 위치 `7`은 `"Hello, "`의 길이(`H e l l o , 공백`)다.

---

## 5. `After delete`는 뭔가

`Main.java:23-25`에서 지우기 커맨드를 실행한 직후의 본문이다.

```java
Command deleteCommand = new DeleteTextCommand(editor, 0, 7);
invoker.executeCommand(deleteCommand);
System.out.println("After delete: " + editor.getContent());
```

1. **직전 상태**: 바로 앞 redo로 본문은 `Hello, World!`.
2. **커맨드 생성**: "0번 자리부터 7글자를 지워라"라는 요청 객체. 이 순간 생성자가 `"Hello, "`를 `deletedText`에 저장한다.
3. **실행**: `"Hello, "`가 지워져 `World!`만 남는다. → `After delete: World!`
4. **마지막 undo**: 저장해 둔 `"Hello, "`를 0번 자리에 다시 넣는다. → `Final text: Hello, World!`

```
Hello, World!
└─────┘          ← 0번부터 7글자 삭제
       World!    ← After delete
```

### 예제에 이 부분이 있는 이유

- **다른 종류의 커맨드도 Invoker가 그대로 처리한다.** `executeCommand`와 `undo` 코드는 넣기든 지우기든 똑같다.
- **지우기는 되돌리려면 지운 글자를 기억해야 한다.** 그래서 `deletedText`가 필요하다.
- **undo가 지우기도 되돌린다.** `Final text`가 그 결과다.

참고: 이 시점에 `redoStack.clear()`도 실행되지만, 직전 redo로 redoStack이 이미 비어 있어서 결과에 영향은 없다.

---

## 6. 설명 순서 (발표용)

원칙: **"하고 싶은 것 → 막히는 지점 → 해결책"** 순서로, 클래스는 필요해질 때 하나씩 꺼낸다.
(블로그 글은 Receiver부터 쌓아 올리는 순서라 다르다. 글로 읽을 때는 괜찮지만 말로 설명하면 지루해지기 쉽다.)

| 단계 | 주제 | 보여줄 것 | 핵심 메시지 |
|---|---|---|---|
| 1 | 목표 | Main 실행 결과 5줄 | 오늘 만들 것은 Ctrl+Z, Ctrl+Y다 |
| 2 | 막히는 지점 | `editor.insertText("Hello, ", 0);` 한 줄 | 메서드 호출은 실행되고 사라진다. 쌓아 둘 수도 되돌릴 수도 없다 |
| 3 | Receiver | `TextEditor` (30초) | 실제 일을 하는 평범한 클래스. undo 코드가 없다 |
| 4 | 핵심 | `Command`, `InsertTextCommand` | 호출을 객체로 만든다. **여기서 패턴 정의를 말한다** |
| 5 | Invoker | `executeCommand` → `undo`/`redo` | Invoker는 내용을 모른다. 커맨드는 두 스택을 오간다 |
| 6 | 확장 | `DeleteTextCommand`의 `deletedText` | Invoker는 안 바뀐다(OCP). 커맨드는 되돌릴 정보도 담는다 |
| 7 | 정리 | 네 역할 대응표 | 처음 본 Main이 이제 줄마다 읽힌다 |
| 8 | 주의할 점 | 아래 7장 | 시간에 맞춰 골라서 |

### 단계별 메모

- **1단계 질문**: "프로그램은 방금 한 일을 어떻게 기억하고, 어떻게 거꾸로 실행할까?"
- **2단계는 빼지 않는다.** "왜 패턴이 필요한가"에 대한 답이다.
- **4단계**: 생성자는 재료를 저장만 하고 실행은 `execute()`에서 한다는 점, `undo()`가 간단한 이유(넣은 글자를 알고 있어서)를 짚는다. `execute()`만 먼저 보여주고 `undo()`를 나중에 붙이면 "undo = 커맨드 패턴"이라는 오해를 막을 수 있다.
- **5단계**: 스택 변화 표 1~4행을 띄운다. `redoStack.clear()` 질문이 나오면 워드프로세서 예시("가 → 나 → 되돌리기 → 다" 입력 후 Ctrl+Y)로 설명한다. 새 작업을 하면 역사가 갈라지므로 옛 redo 기록은 버린다.
- **6단계**: 스택 변화 표 5~6행을 띄운다.

### 상황별 조정

- **시간이 짧다면**: 2단계와 8단계 일부만 줄인다. 4·5단계는 패턴의 본체라 줄이지 않는다.
- **청중이 패턴에 익숙하다면**: 7단계(역할표)를 맨 앞에 두고 코드로 확인하는 순서도 괜찮다. 입문자에게는 추천하지 않는다.

---

## 7. 주의할 점

### 7-1. 되돌리기용 정보는 "실행할 때" 저장해야 한다 (가장 중요)

`DeleteTextCommand`는 지울 글자를 **생성자에서** 저장한다(`DeleteTextCommand.java:13`). Main에서는 만들자마자 실행해서 문제가 없지만, 미리 만들어 두고 본문이 바뀐 뒤 실행하면 버그가 난다.

```java
TextEditor editor = new TextEditor();
TextEditorInvoker invoker = new TextEditorInvoker();
invoker.executeCommand(new InsertTextCommand(editor, "Hello, World!", 0));

Command delete = new DeleteTextCommand(editor, 0, 7);   // 이 순간 "Hello, "를 기억

invoker.executeCommand(new InsertTextCommand(editor, "Hi ", 0));  // 실행 전에 본문 변경
invoker.executeCommand(delete);
invoker.undo();
```

실제 실행 결과:

```
delete 전: Hi Hello, World!
delete 후: o, World!
undo 후 : Hello, o, World!
```

- 기억한 글자는 `"Hello, "`인데 실제로 지운 글자는 `"Hi Hell"`이다.
- undo는 기억한 `"Hello, "`를 넣으므로 `"Hi Hell"`은 영영 사라진다.

개선안: 지울 글자를 `execute()` 안에서, 지우기 직전에 저장한다.

```java
public class DeleteTextCommand implements Command {
    private TextEditor editor;
    private int position;
    private int length;
    private String deletedText;   // execute() 할 때 채운다

    public DeleteTextCommand(TextEditor editor, int position, int length) {
        this.editor = editor;
        this.position = position;
        this.length = length;
    }

    @Override
    public void execute() {
        this.deletedText = editor.getTextSubstring(position, position + length);
        editor.deleteText(position, length);
    }

    @Override
    public void undo() {
        editor.insertText(deletedText, position);
    }
}
```

개선안으로 같은 시나리오를 실행하면 `undo 후 : Hi Hello, World!`로 정확히 돌아온다(실행 확인함).

> 원칙: 되돌리기에 필요한 상태는 커맨드를 만들 때가 아니라 실행할 때 저장한다.
> "미뤘다가 실행할 수 있다"는 장점을 실제로 쓰는 순간 드러나는 함정이다.

### 7-2. 모든 변경은 Invoker를 거쳐야 한다

- 이 예제의 커맨드는 위치 기반이다. undo가 정확하려면 되돌리는 시점의 본문이 실행 직후의 본문과 같아야 한다.
- 누군가 `editor.insertText()`를 직접 부르면 스택은 그 변경을 모르므로, 이후 undo가 엉뚱한 위치를 건드린다.

### 7-3. 잘못된 위치를 넣으면 예외가 난다

본문 길이가 16인데 `new DeleteTextCommand(editor, 0, 100)`을 만들면:

```
java.lang.StringIndexOutOfBoundsException: start 0, end 100, length 16
```

범위 검사가 없다. 실제로 쓴다면 검사나 보정이 필요하다.

### 7-4. Stack보다 Deque를 권장한다

- Java 공식 문서는 `java.util.Stack` 대신 `Deque` 구현체를 쓰라고 권장한다.
- `Stack`은 불필요한 동기화가 있고, `Vector`를 상속해 스택에 맞지 않는 메서드까지 열려 있다.

```java
private Deque<Command> undoStack = new ArrayDeque<>();
private Deque<Command> redoStack = new ArrayDeque<>();
```

`push`, `pop`, `isEmpty`, `clear`는 이름이 같아서 나머지 코드는 그대로 둬도 된다. 설명용으로는 `Stack`이 직관적이라 짧게 언급만 해도 충분하다.

### 7-5. 클래스가 늘어난다

작업 하나마다 클래스 하나가 생긴다. undo나 작업 기록 같은 요구 없이 단순히 호출을 감싸려고 쓰면 코드만 복잡해진다. 이 패턴은 **요청을 저장하고, 미루고, 되돌려야 할 때** 쓴다.

---

## 8. 관련 파일

| 파일 | 내용 |
|---|---|
| `ex02/*.java` | 예제 원본 (수정하지 않음) |
| `blog-command-pattern.txt` | 티스토리 붙여넣기용 블로그 글 (입문자 대상, 주의할 점 포함) |
| `command-pattern-ex02-notes.md` | 이 문서 |

### 남은 결정 사항

- `ex01/` 폴더를 실제로 지울지 (현재는 글에서만 제외했고 파일은 그대로 둠)
- `ex02/DeleteTextCommand.java`를 개선안으로 고칠지 (현재는 글에만 개선안을 실었음)
- 블로그 글 구성을 6장의 발표 순서에 맞출지
- 블로그 글의 `[작성자 메모]` 4곳 채우기

### 참고 자료

- Gamma, Helm, Johnson, Vlissides, 『Design Patterns: Elements of Reusable Object-Oriented Software』 (Command)
- 에릭 프리먼, 엘리자베스 롭슨, 『헤드 퍼스트 디자인 패턴』 6장
- Java SE 17 API, `java.util.Stack`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Stack.html

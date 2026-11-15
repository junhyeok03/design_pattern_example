package org._18_memento.ex02;

// Client
class Editor {
    private final Document document;
    private final DocumentHistory history;

    public Editor() {
        this.document = new Document();
        this.history = new DocumentHistory();
    }

    public void write(String text) {
        history.push(document.save()); // 1. 바꾸기 전 상태를 저장
        document.write(text);          // 2. 그다음 변경
    }

    // 되돌리기
    public void undo() {
        DocumentMemento memento = history.pop(); // 가장 최근 사진 꺼내기
        if (memento != null) {                   // 비어 있으면 아무것도 못함
            document.restore(memento);
        }
    }

    public String getContent() {
        return document.getContent();
    }
}
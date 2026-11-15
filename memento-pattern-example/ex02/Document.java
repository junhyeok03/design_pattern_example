package org._18_memento.ex02;

// Originator
class Document {
    private StringBuilder content;

    public Document() {
        this.content = new StringBuilder();
    }

    public void write(String text) {
        content.append(text);
    }

    public String getContent() {
        return content.toString();
    }

    public DocumentMemento save() {
        return new DocumentMemento(content.toString());
    }

    public void restore(DocumentMemento memento) {
        this.content = new StringBuilder(memento.getContent()); // 사진에 저장된 String으로
                                                                // 새 StringBuilder를 만들어서 내용을 통째로 교체
    }
}
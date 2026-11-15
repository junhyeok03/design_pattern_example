package org._17_command.ex02_before;

import java.util.Stack;

// 커맨드 패턴 적용 전: 편집 기능과 실행 기록 관리를 TextEditor 혼자 맡는다
public class TextEditor {
    private StringBuilder content;

    // 작업 하나의 정보가 스택 세 개에 흩어져 있다
    private Stack<String> undoTypes = new Stack<>();     // "INSERT" 또는 "DELETE"
    private Stack<String> undoTexts = new Stack<>();     // 넣은 글자 또는 지운 글자
    private Stack<Integer> undoPositions = new Stack<>();

    // redo를 위해 똑같은 스택 세 개가 또 필요하다
    private Stack<String> redoTypes = new Stack<>();
    private Stack<String> redoTexts = new Stack<>();
    private Stack<Integer> redoPositions = new Stack<>();

    public TextEditor() {
        this.content = new StringBuilder();
    }

    public void insertText(String text, int position) {
        content.insert(position, text);

        undoTypes.push("INSERT");
        undoTexts.push(text);
        undoPositions.push(position);
        clearRedo();
    }

    public void deleteText(int position, int length) {
        String deletedText = content.substring(position, position + length);
        content.delete(position, position + length);

        undoTypes.push("DELETE");
        undoTexts.push(deletedText);
        undoPositions.push(position);
        clearRedo();
    }

    public void undo() {
        if (undoTypes.isEmpty()) {
            return;
        }
        String type = undoTypes.pop();
        String text = undoTexts.pop();
        int position = undoPositions.pop();

        // 작업 종류마다 되돌리는 방법을 여기서 직접 분기한다
        if (type.equals("INSERT")) {
            content.delete(position, position + text.length());
        } else if (type.equals("DELETE")) {
            content.insert(position, text);
        }
        // 새 기능(굵게, 바꾸기, 붙여넣기...)이 생길 때마다 else if 가 늘어난다

        redoTypes.push(type);
        redoTexts.push(text);
        redoPositions.push(position);
    }

    public void redo() {
        if (redoTypes.isEmpty()) {
            return;
        }
        String type = redoTypes.pop();
        String text = redoTexts.pop();
        int position = redoPositions.pop();

        // undo 와 똑같은 분기를 반대로 한 번 더 써야 한다
        // insertText()/deleteText()를 다시 부르면 redo 기록이 지워지므로 직접 처리한다
        if (type.equals("INSERT")) {
            content.insert(position, text);
        } else if (type.equals("DELETE")) {
            content.delete(position, position + text.length());
        }

        undoTypes.push(type);
        undoTexts.push(text);
        undoPositions.push(position);
    }

    private void clearRedo() {
        redoTypes.clear();
        redoTexts.clear();
        redoPositions.clear();
    }

    public String getContent() {
        return content.toString();
    }
}

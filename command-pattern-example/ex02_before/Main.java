package org._17_command.ex02_before;

// Client code (ex02 Main 과 같은 시나리오)
public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();

        editor.insertText("Hello, ", 0);
        editor.insertText("World!", 7);

        System.out.println("Current text: " + editor.getContent());

        editor.undo();
        System.out.println("After undo: " + editor.getContent());

        editor.redo();
        System.out.println("After redo: " + editor.getContent());

        editor.deleteText(0, 7);
        System.out.println("After delete: " + editor.getContent());

        editor.undo();
        System.out.println("Final text: " + editor.getContent());
    }
}

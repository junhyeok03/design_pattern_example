package com.example.theme.parts;

public enum Color {
    WHITE("흰색"),
    BLACK("검정");

    private final String label;

    Color(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}

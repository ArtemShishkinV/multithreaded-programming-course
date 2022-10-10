package com.shishkin.views.types;

public enum MenuOption {
    NONE("none"),
    START("start"),
    STOP("stop"),
    AWAIT("await"),
    EXIT("exit");

    private static final String FORMAT_TO_STRING = "- %s.%n";

    private final String value;

    MenuOption(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format(FORMAT_TO_STRING, this.value);
    }

    public MenuOption findByValue(String value) {
        for (MenuOption menuOption : MenuOption.values()) {
            if (menuOption.getValue().equals(value)) {
                return menuOption;
            }
        }
        return NONE;
    }
}

package org.example.common;

public enum ErrorMessage {
    INVALID_NUMBER_SCOPE("올바르지 않은 숫자 범위입니다."),
    ALREADY_SELECTED_TEAM("이미 선택된 팀입니다."),
    IS_NULL_OR_IS_EMPTY("빈 값은 입력할 수 없습니다."),
    INVALID_NUMBER_FORMAT("잘못된 숫자 형식입니다."),
    INVALID_PARAMETER("잘못된 매개변수입니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
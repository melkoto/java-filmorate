package ru.yandex.practicum.enums;

public enum FriendshipStatusEnum {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    private final String value;

    FriendshipStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package ru.yandex.practicum.utils;

public class IdCreator {
    private static int id = 1;

    public static int getId() {
        return id++;
    }
}

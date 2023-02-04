package ru.yandex.practicum.enums;

public enum GenreEnum {
    ACTION("Боевик"),
    CARTOON("Мультфильм"),
    COMEDY("Комедия"),
    DOCUMENTARY("Документальный"),
    DRAMA("Драма"),
    THRILLER("Триллер");

    private final String genre;

    GenreEnum(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }
}

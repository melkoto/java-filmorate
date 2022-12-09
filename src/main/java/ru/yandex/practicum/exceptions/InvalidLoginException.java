package ru.yandex.practicum.exceptions;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super();
    }

    public InvalidLoginException(String message) {
        super(message);
    }

    public InvalidLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLoginException(Throwable cause) {
        super(cause);
    }

    protected InvalidLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

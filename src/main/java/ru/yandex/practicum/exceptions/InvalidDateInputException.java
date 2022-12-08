package ru.yandex.practicum.exceptions;

public class InvalidDateInputException extends RuntimeException {
    public InvalidDateInputException() {
        super();
    }

    public InvalidDateInputException(String message) {
        super(message);
    }

    public InvalidDateInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateInputException(Throwable cause) {
        super(cause);
    }

    protected InvalidDateInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

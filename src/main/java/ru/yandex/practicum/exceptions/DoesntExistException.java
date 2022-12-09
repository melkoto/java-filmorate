package ru.yandex.practicum.exceptions;

public class DoesntExistException extends RuntimeException {
    public DoesntExistException() {
        super();
    }

    public DoesntExistException(String message) {
        super(message);
    }

    public DoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoesntExistException(Throwable cause) {
        super(cause);
    }

    protected DoesntExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

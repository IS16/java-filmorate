package ru.yandex.practicum.filmorate.exception;

public class RateNotFoundException extends RuntimeException {
    public RateNotFoundException(String message) {
        super(message);
    }
}

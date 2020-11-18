package com.ynz.jpa.cache.exception;


public class DuplicatedElementException extends RuntimeException {
    public DuplicatedElementException(String s) {
        super(s);
    }

    public DuplicatedElementException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DuplicatedElementException(Throwable throwable) {
        super(throwable);
    }
}

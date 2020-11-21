package com.ynz.jpa.cache.mapper;

public interface Persistable<T, R> {
    R convert(T source);

    T invert(R entity);
}

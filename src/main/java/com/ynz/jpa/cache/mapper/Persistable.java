package com.ynz.jpa.cache.mapper;

import java.util.Collection;
import java.util.List;

public interface Persistable<T, R> {
    R convert(T source);

    List<R> convert(Collection<T> sources);

    T invert(R entity);

    List<T> invert(Collection<R> entities);
}

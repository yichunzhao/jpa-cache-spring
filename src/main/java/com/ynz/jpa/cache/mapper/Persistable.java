package com.ynz.jpa.cache.mapper;

import java.util.Collection;
import java.util.List;

public interface Persistable<D, E> {
    E convert(D dto);

    List<E> convert(Collection<D> dtos);

    D invert(E entity);

    List<D> invert(Collection<E> entities);
}

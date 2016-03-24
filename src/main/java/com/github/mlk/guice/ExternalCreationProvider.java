package com.github.mlk.guice;

import com.google.inject.Provider;

import java.util.function.Function;

class ExternalCreationProvider<T> implements Provider<T> {
    private final Function<Class<?>, ?> supplier;
    private final Class<? super T> clazz;

    ExternalCreationProvider(Function<Class<?>, ?> supplier, Class<? super T> clazz) {
        this.supplier = supplier;
        this.clazz = clazz;
    }

    @Override
    public T get() {
        Object item = supplier.apply(clazz);
        return (T)clazz.cast(item);
    }
}

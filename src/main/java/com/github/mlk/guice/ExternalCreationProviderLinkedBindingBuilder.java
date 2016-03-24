package com.github.mlk.guice;

import com.github.mlk.guice.passthrough.PassThroughLinkedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;

import java.util.function.Function;

class ExternalCreationProviderLinkedBindingBuilder<T> extends PassThroughLinkedBindingBuilder<T> {
    private final Function<Class<?>, ?> supplier;
    private final Class<? super T> clazz;

    ExternalCreationProviderLinkedBindingBuilder(LinkedBindingBuilder<T> base, Function<Class<?>, ?> supplier, Class<? super T> clazz) {
        super(base);
        this.supplier = supplier;
        this.clazz = clazz;
    }

    /** Causes the current item to be created magically (via the Function). */
    public ScopedBindingBuilder byMagic() {
        return toProvider(new ExternalCreationProvider<>(supplier, clazz));
    }
}

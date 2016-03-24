package com.github.mlk.guice;

import com.github.mlk.guice.passthrough.PassThroughAnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;

import java.lang.annotation.Annotation;
import java.util.function.Function;

class ExternalCreationAnnotatedBindingBuilder<T> extends PassThroughAnnotatedBindingBuilder<T> {
    private final Function<Class<?>, ?> supplier;
    private final Class<? super T> clazz;

    ExternalCreationAnnotatedBindingBuilder(AnnotatedBindingBuilder base, Function<Class<?>, ?> supplier, Class<? super T> clazz) {
        super(base);
        this.supplier = supplier;
        this.clazz = clazz;
    }

    @Override
    public ExternalCreationProviderLinkedBindingBuilder<T> annotatedWith(Class<? extends Annotation> annotationType) {
        return new ExternalCreationProviderLinkedBindingBuilder<>(super.annotatedWith(annotationType), supplier, clazz);
    }

    @Override
    public ExternalCreationProviderLinkedBindingBuilder<T> annotatedWith(Annotation annotation) {
        return new ExternalCreationProviderLinkedBindingBuilder<>(super.annotatedWith(annotation), supplier, clazz);
    }

    /** Causes the current item to be created magically (via the Function). */
    public ScopedBindingBuilder byMagic() {
        return toProvider(new ExternalCreationProvider<>(supplier, clazz));
    }
}

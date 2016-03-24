package com.github.mlk.guice.passthrough;

import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class PassThroughAnnotatedBindingBuilder<T> implements AnnotatedBindingBuilder<T> {
    private final AnnotatedBindingBuilder<T> base;

    public PassThroughAnnotatedBindingBuilder(AnnotatedBindingBuilder<T> base) {
        this.base = base;
    }

    @Override
    public LinkedBindingBuilder<T> annotatedWith(Class<? extends Annotation> annotationType) {
        return base.annotatedWith(annotationType);
    }

    @Override
    public LinkedBindingBuilder<T> annotatedWith(Annotation annotation) {
        return base.annotatedWith(annotation);
    }

    @Override
    public ScopedBindingBuilder to(Class<? extends T> implementation) {
        return base.to(implementation);
    }

    @Override
    public ScopedBindingBuilder to(TypeLiteral<? extends T> implementation) {
        return base.to(implementation);
    }

    @Override
    public ScopedBindingBuilder to(Key<? extends T> targetKey) {
        return base.to(targetKey);
    }

    @Override
    public void toInstance(T instance) {
        base.toInstance(instance);
    }

    @Override
    public ScopedBindingBuilder toProvider(com.google.inject.Provider<? extends T> provider) {
        return base.toProvider(provider);
    }

    @Override
    public ScopedBindingBuilder toProvider(Provider<? extends T> provider) {
        return base.toProvider(provider);
    }

    @Override
    public ScopedBindingBuilder toProvider(Class<? extends Provider<? extends T>> providerType) {
        return base.toProvider(providerType);
    }

    @Override
    public ScopedBindingBuilder toProvider(TypeLiteral<? extends Provider<? extends T>> providerType) {
        return base.toProvider(providerType);
    }

    @Override
    public ScopedBindingBuilder toProvider(Key<? extends Provider<? extends T>> providerKey) {
        return base.toProvider(providerKey);
    }

    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor) {
        return base.toConstructor(constructor);
    }

    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor, TypeLiteral<? extends S> type) {
        return base.toConstructor(constructor, type);
    }

    @Override
    public void in(Class<? extends Annotation> scopeAnnotation) {
        base.in(scopeAnnotation);
    }

    @Override
    public void in(Scope scope) {
        base.in(scope);
    }

    @Override
    public void asEagerSingleton() {
        base.asEagerSingleton();
    }
}

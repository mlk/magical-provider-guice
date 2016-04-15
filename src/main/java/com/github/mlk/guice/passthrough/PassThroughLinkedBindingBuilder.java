package com.github.mlk.guice.passthrough;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class PassThroughLinkedBindingBuilder<T> implements LinkedBindingBuilder<T> {
    private final LinkedBindingBuilder<T> base;

    public PassThroughLinkedBindingBuilder(LinkedBindingBuilder<T> base) {
        this.base = base;
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
    public ScopedBindingBuilder toProvider(Provider<? extends T> provider) {
        return base.toProvider(provider);
    }

    @Override
    public ScopedBindingBuilder toProvider(javax.inject.Provider<? extends T> provider) {
        return base.toProvider(provider);
    }

    @Override
    public ScopedBindingBuilder toProvider(Class<? extends javax.inject.Provider<? extends T>> providerType) {
        return base.toProvider(providerType);
    }

    @Override
    public ScopedBindingBuilder toProvider(TypeLiteral<? extends javax.inject.Provider<? extends T>> providerType) {
        return base.toProvider(providerType);
    }

    @Override
    public ScopedBindingBuilder toProvider(Key<? extends javax.inject.Provider<? extends T>> providerKey) {
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

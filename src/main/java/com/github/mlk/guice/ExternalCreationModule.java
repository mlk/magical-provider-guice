package com.github.mlk.guice;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;

import java.io.IOException;
import java.util.function.Function;

/** This module takes a function to create new instances and expands the bind methods to allow for a `byMagic()` call.
 */
public abstract class ExternalCreationModule extends AbstractModule {
    private final Function<Class<?>, ?> supplier;

    /** @param supplier A method of creating the internal objects. */
    public ExternalCreationModule(Function<Class<?>, ?> supplier) {
        this.supplier = supplier;
    }

    protected <T> ExternalCreationProviderLinkedBindingBuilder<T> bind(Key<T> key) {
        return new ExternalCreationProviderLinkedBindingBuilder<>(super.bind(key), supplier, key.getTypeLiteral().getRawType());
    }

    protected <T> ExternalCreationAnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
        return new ExternalCreationAnnotatedBindingBuilder<>(super.bind(typeLiteral), supplier, typeLiteral.getRawType());
    }

    protected <T> ExternalCreationAnnotatedBindingBuilder<T> bind(Class<T> clazz) {
        return new ExternalCreationAnnotatedBindingBuilder<>(super.bind(clazz), supplier, clazz);
    }

    /** Scans the package provided for classes and binds all of them to the function.
     *
     * @param classLoader The class loader to use.
     * @param packageToScan The package that will be scanned.
     */
    protected void scan(ClassLoader classLoader, String packageToScan) {
        try {
            ClassPath classPath = ClassPath.from(classLoader);
            ImmutableSet<ClassPath.ClassInfo> info = classPath.getTopLevelClasses(packageToScan);
            for(ClassPath.ClassInfo clazz : info) {
                bind(clazz.load()).byMagic();
            }
        } catch (IOException e) {
            throw new ProvisionException("Failed to scan package " + packageToScan, e);
        }
    }

    /** Scans the package provided for classes and binds all of them to the function.
     *
     * @param packageToScan The package that will be scanned.
     */
    protected void scan(String packageToScan) {
        scan(this.getClass().getClassLoader(), packageToScan);
    }
}


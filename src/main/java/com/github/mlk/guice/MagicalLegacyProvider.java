package com.github.mlk.guice;

import com.google.common.collect.ImmutableMap;
import com.google.inject.*;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MagicalLegacyProvider<T> implements Provider<T>, HasDependencies {
    private Injector injector;
    private final Key<?>[] keys;
    private final Constructor<? extends T> ctor;
    private static final Map<Class<?>, Class<?>> WAPPER_TO_PRIMATIVE
            = new ImmutableMap.Builder<Class<?>, Class<?>>()
            .put(Boolean.class, boolean.class)
            .put(Byte.class, byte.class)
            .put(Short.class, short.class)
            .put(Integer.class, int.class)
            .put(Long.class, long.class)
            .put(Float.class, float.class)
            .put(Double.class, double.class)
            .put(Character.class, char.class)
            .put(Void.class, void.class)
            .build();


    public MagicalLegacyProvider(Class<? extends T> clazz, Key<?>... keys) {
        this.keys = keys;

        Class<?>[] ctorTypes = new Class<?>[keys.length];
        for(int index = 0; index < ctorTypes.length; index++) {
            ctorTypes[index] = keys[index].getTypeLiteral().getRawType();

        }
        try {
            Constructor<?> ctor = null;
            for (Constructor<?> constructor : clazz.getConstructors()) {

                if (isCompatable(constructor, ctorTypes)) {
                    ctor = constructor;
                    break;
                }
            }
            if(ctor != null) {
                this.ctor = (Constructor<T>)ctor;
            } else {
                throw new ProvisionException("Unable to find a constructor matching the given types - " + Arrays.toString(keys));
            }

        } catch (Exception e) {
            throw new ProvisionException("Unable to find a constructor matching the given types - " + Arrays.toString(keys), e);
        }
    }

    private boolean isCompatable(Constructor<?> constructor, Class<?>[] ctorTypes) {
        if(constructor.getParameterCount() != ctorTypes.length) {
            return false;
        }
        boolean found = true;
        for(int i = 0; i<ctorTypes.length; i++) {
            if(!(constructor.getParameterTypes()[i].equals(ctorTypes[i]) || constructor.getParameterTypes()[i].equals(WAPPER_TO_PRIMATIVE.get(ctorTypes[i])))){
                found = false;
                break;
            }
        }
        return found;
    }

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }


    @Override
    public Set<Dependency<?>> getDependencies() {
        Set<Dependency<?>> dependencies = new HashSet<>();
        for(Key<?> key : keys) {
            dependencies.add(Dependency.get(key));
        }
        return dependencies;
    }

    @Override
    public T get() {
        Object[] ctorParams = new Object[keys.length];
        for(int index = 0; index < ctorParams.length; index++) {
            ctorParams[index] = injector.getInstance(keys[index]);
        }
	try {
		return ctor.newInstance(ctorParams);
	} catch(InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException ie) {
		throw new RuntimeException(ie);
	}
    }
}

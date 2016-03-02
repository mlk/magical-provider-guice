
import com.google.inject.*;
import com.google.inject.name.Names;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;

import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Constructor;

public class MagicProvider<T> implements Provider<T>, HasDependencies {
    private Injector injector;
    private final Key<?>[] keys;
    private final Constructor<? extends T> ctor;

    public MagicProvider(Class<? extends T> clazz, Key<?>... keys) {
        this.keys = keys;

        Class<?>[] ctorTypes = new Class<?>[keys.length];
        for(int index = 0; index < ctorTypes.length; index++) {
            ctorTypes[index] = keys[index].getTypeLiteral().getRawType();
        }
        try {
            ctor = clazz.getConstructor(ctorTypes);
        } catch (Exception e) {
            throw new ProvisionException("Unable to find a constructor matching the given types", e);
        }
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

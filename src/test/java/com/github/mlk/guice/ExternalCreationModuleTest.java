package com.github.mlk.guice;

import com.github.mlk.guice.external.A;
import com.github.mlk.guice.external.B;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public class ExternalCreationModuleTest {
    private final List<Class<?>> items = new ArrayList<>();

    @Before
    public void clearItems() {
        items.clear();
    }

    private Object recordCreation(Class<?> x) {
        items.add(x);
        try {
            return x.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void creationCallsExternalMethod() {
        Injector injector = Guice.createInjector(new ExternalCreationModule(this::recordCreation) {
            @Override
            protected void configure() {
                bind(Key.get(String.class)).byMagic();
            }
        });

        injector.getInstance(String.class);

        assertThat(items.size(), is(1));
    }

    @Test
    public void requestForSingletonHonoured() {
        Injector injector = Guice.createInjector(new ExternalCreationModule(this::recordCreation) {
            @Override
            protected void configure() {
                bind(Key.get(String.class)).byMagic().asEagerSingleton();
            }
        });

        String item1 = injector.getInstance(String.class);
        String item2 = injector.getInstance(String.class);

        assertSame(item1, item2);
        assertThat(items.size(), is(1));
    }

    @Test
    public void creationWithTypeLiteral() {
        Injector injector = Guice.createInjector(new ExternalCreationModule(this::recordCreation) {
            @Override
            protected void configure() {
                bind(new TypeLiteral<String>() {} ).byMagic();
            }
        });

        injector.getInstance(String.class);

        assertThat(items.size(), is(1));
    }

    @Test
    public void annotatedTypeLiteral() {
        Injector injector = Guice.createInjector(new ExternalCreationModule(this::recordCreation) {
            @Override
            protected void configure() {
                bind(new TypeLiteral<String>() {} ).annotatedWith(Names.named("MAGIC")).byMagic();
            }
        });

        injector.getInstance(Key.get(String.class, Names.named("MAGIC")));

        assertThat(items.size(), is(1));
    }

    @Test
    public void whenNotByMagicAnnotatedTypeLiteralReturnsNormal() {
        Injector injector = Guice.createInjector(new ExternalCreationModule(this::recordCreation) {
            @Override
            protected void configure() {
                bind(new TypeLiteral<String>() {} ).annotatedWith(Names.named("MAGIC")).byMagic();
                bind(new TypeLiteral<String>() {} ).annotatedWith(Names.named("NOT MAGIC")).toInstance("NOT MAGIC");
            }
        });

        assertThat(injector.getInstance(Key.get(String.class, Names.named("NOT MAGIC"))), is("NOT MAGIC"));

        assertThat(items.size(), is(0));
    }

    @Test
    public void annotatedTypeLiteralWithAnnotation() {
        Injector injector = Guice.createInjector(new ExternalCreationModule(this::recordCreation) {
            @Override
            protected void configure() {
                bind(new TypeLiteral<String>() {} ).annotatedWith(ExampleAnnotation.class).byMagic();
            }
        });

        injector.getInstance(Key.get(String.class, ExampleAnnotation.class));

        assertThat(items.size(), is(1));
    }

    @Test
    public void scanBindAllInPackage() {
        Injector injector = Guice.createInjector(new ExternalCreationModule(this::recordCreation) {
            @Override
            protected void configure() {
                scan("com.github.mlk.guice.external");
            }
        });

        injector.getInstance(A.class);
        injector.getInstance(B.class);

        assertThat(items.size(), is(2));
    }

    @Test
    public void scanFiltersOutClassB() {
        Injector injector = Guice.createInjector(new ExternalCreationModule(this::recordCreation) {
            @Override
            protected void configure() {
                scan("com.github.mlk.guice.external", (x) -> !x.getName().endsWith("B"));
            }
        });

        injector.getInstance(A.class);
        injector.getInstance(B.class);

        assertThat(items.size(), is(1));
    }

}


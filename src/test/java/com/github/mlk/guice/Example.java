package com.github.mlk.guice;

import com.google.inject.*;
import com.google.inject.name.Named;

import static com.google.inject.name.Names.named;

interface LegacyAction {
}

class ModernAction implements LegacyAction {
    @Inject
    ModernAction() {}
}

class LegacyService {
    public LegacyService(String host, int port, LegacyAction action) {
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        System.out.println("Action: " + action.getClass());

    }
}

public class Example {
    public static void main(String... argv) {
        Guice.createInjector(
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(String.class).annotatedWith(named("host")).toInstance("example.com");
                        bind(int.class).annotatedWith(named("port")).toInstance(8080);
                        bind(LegacyAction.class).to(ModernAction.class);

                        bind(LegacyService.class).toProvider(new MagicalLegacyProvider<>(LegacyService.class,
                                Key.get(String.class, named("host")), Key.get(int.class, named("port")), Key.get(LegacyAction.class)));
                    }
                }).getInstance(LegacyService.class);
    }
}

class ExampleWithProviderMethods {
    public static void main(String... argv) {
        Guice.createInjector(
                new AbstractModule() {
                    @Provides
                    public LegacyService create(@Named("host") String host, @Named("port") int port, LegacyAction action) {
                        return new LegacyService(host, port, action);
                    }

                    @Override
                    protected void configure() {
                        bind(String.class).annotatedWith(named("host")).toInstance("example.com");
                        bind(int.class).annotatedWith(named("port")).toInstance(8080);
                        bind(LegacyAction.class).to(ModernAction.class);
                    }
                }).getInstance(LegacyService.class);
    }
}

class ExampleWithConstructorBinding {
    public static void main(String... argv) {
        Guice.createInjector(
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(String.class).toInstance("example.com");
                        bind(int.class).toInstance(8080);
                        bind(LegacyAction.class).to(ModernAction.class);
                        try {
                            bind(LegacyService.class).toConstructor(LegacyService.class.getConstructor(String.class, int.class, LegacyAction.class));
                        } catch (NoSuchMethodException e) {
                            addError(e);
                        }
                    }
                }).getInstance(LegacyService.class);
    }
}
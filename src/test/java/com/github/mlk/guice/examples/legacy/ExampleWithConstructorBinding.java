package com.github.mlk.guice.examples.legacy;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

public class ExampleWithConstructorBinding {
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

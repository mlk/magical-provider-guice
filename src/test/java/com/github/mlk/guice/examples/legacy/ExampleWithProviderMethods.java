package com.github.mlk.guice.examples.legacy;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import static com.google.inject.name.Names.named;

public class ExampleWithProviderMethods {
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

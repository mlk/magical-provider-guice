package com.github.mlk.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.name.Names;
import com.google.inject.spi.Dependency;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Objects;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MagicalLegacyProviderTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenConstructorDoesNotExistThenThrowProvisionException() {
        thrown.expect(ProvisionException.class);
        thrown.expectMessage(containsString("Unable to find a constructor matching"));

        new MagicalLegacyProvider<>(LegacyService.class, Key.get(ExpectedException.class));
    }

    @Test
    public void whenTypesHaveBeenBoundThenCreateNewInstance() {
        LegacyService legacyService = Guice.createInjector(
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(String.class).annotatedWith(Names.named("host")).toInstance("GUICE-HOST");
                        bind(Integer.class).annotatedWith(Names.named("port")).toInstance(8080);

                        bind(LegacyService.class).toProvider(new MagicalLegacyProvider<>(LegacyService.class, Key.get(String.class, Names.named("host")), Key.get(Integer.class, Names.named("port"))));
                    }
                }).getInstance(LegacyService.class);
        assertThat(legacyService, is(new LegacyService("GUICE-HOST", 8080)));
    }

    @Test
    public void whenUsingPrimativeTypes() {
        LegacyService legacyService = Guice.createInjector(
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(int.class).annotatedWith(Names.named("port")).toInstance(8080);

                        bind(LegacyService.class).toProvider(new MagicalLegacyProvider<>(LegacyService.class, Key.get(int.class, Names.named("port"))));
                    }
                }).getInstance(LegacyService.class);
        assertThat(legacyService, is(new LegacyService("DEFAULT", 8080)));
    }

    @Test
    public void dependencies() {
        MagicalLegacyProvider<LegacyService> subject = new MagicalLegacyProvider<>(LegacyService.class, Key.get(int.class, Names.named("port")));
        Set<Dependency<?>> dependencies =  subject.getDependencies();
        assertThat(dependencies.size(), is(1));
        assertThat(dependencies.iterator().next(), is(Dependency.get(Key.get(int.class, Names.named("port")))));
    }

    static class LegacyService {
        private final String host;
        private final Integer port;

        public LegacyService(int port) {
            this.host = "DEFAULT";
            this.port = port;
        }

        public LegacyService(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public int hashCode() {
            return Objects.hash(host, port);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof LegacyService) {
                LegacyService other = (LegacyService)obj;
                return Objects.equals(host, other.host) && Objects.equals(port, other.port);
            }
            return false;
        }

        @Override
        public String toString() {
            return "LegacyService [host:" + host + ", port: " + port + "]";
        }
    }


}


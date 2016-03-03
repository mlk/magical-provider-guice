# magical-provider-guice

This is in response to a [post on Stack Overflow](http://stackoverflow.com/questions/35746834/pass-parameters-to-constructor-in-guice-with-no-modifications-to-the-interface-i/35747432#35747432). The OP wanted a method of creating legacy components without creating either [Provider](https://google.github.io/guice/api-docs/latest/javadoc/index.html?com/google/inject/Provider.html)s or using [Provides Methods](https://github.com/google/guice/wiki/ProvidesMethods).

For this we will assume that we have a legacy components that we can not change. For example:


```
interface LegacyAction {
}

class ModernAction implements LegacyAction {
    @Inject
    ModernAction() {}
}

```

Its dependencies (host, port and LegacyAction) are all known by Guice. We want to have Guice also manage LegacyService.

```
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
```

## Download

Maven: 
```
<dependency>
  <groupId>com.github.mlk</groupId>
  <artifactId>magical-provider-guice</artifactId>
  <version>1.0.0</version>
</dependency>
```


## Alternatives

### [Provider Methods](https://github.com/google/guice/wiki/ProvidesMethods)


```
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
```

This requires a tiny amount of additional typing, but gives you type safety. I'd recommend this in most cases!
 
### Constructor Binding


```
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
```

This works find if you have no [binding annotations](https://github.com/google/guice/wiki/BindingAnnotations). 
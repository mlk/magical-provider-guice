# magical-provider-guice

Helper Guice modules.

## Robotic Legs Module

This is to provide a simplified API around creating "[robot legs](https://github.com/google/guice/wiki/FrequentlyAskedQuestions#how-do-i-build-two-similar-but-slightly-different-trees-of-objects)", where you have two (or more) similar, but slightly differents trees of objects.

## External Creation Module

This is to support creation of a number of objects where something other than guice creates the objects. For example [Feign](https://github.com/Netflix/feign) or [JDBI](http://jdbi.org/) where the code is an interface with metadata and Feign or JDBI create the objects via [Proxy API](https://docs.oracle.com/javase/7/docs/api/java/lang/reflect/Proxy.html).   

For examples of this please look at [jdbi-guice](https://github.com/mlk/jdbi-guice) and [feign-guice](https://github.com/mlk/feign-guice).

## Legacy Module

This is in response to a [post on Stack Overflow](http://stackoverflow.com/questions/35746834/pass-parameters-to-constructor-in-guice-with-no-modifications-to-the-interface-i/35747432#35747432). The OP wanted a method of creating legacy components without creating either [Provider](https://google.github.io/guice/api-docs/latest/javadoc/index.html?com/google/inject/Provider.html)s or using [Provides Methods](https://github.com/google/guice/wiki/ProvidesMethods).

For this we will assume that we have a legacy components that we can not change. For example:



```
interface LegacyAction {
}

class LegacyService {
    public LegacyService(String host, int port, LegacyAction action) {
    }
}
```

Its dependencies (host, port and LegacyAction) are all known by Guice. We want to have Guice also manage LegacyService.

```
class ModernAction implements LegacyAction {
    @Inject
    ModernAction() {}
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
```


### Alternatives

#### [Provider Methods](https://github.com/google/guice/wiki/ProvidesMethods)

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
 
#### Constructor Binding

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

### Oddities

 * It does not know the difference between int and Integer and will bind to the first on it finds.

## Download

## Download

**Maven:**
```
<dependency>
  <groupId>com.github.mlk</groupId>
  <artifactId>magical-provider-guice</artifactId>
  <version>1.0.1</version>
</dependency>
```
**Apache Buildr**

```
'com.github.mlk:magical-provider-guice:jar:1.0.1'
```

**Apache Ivy**

```
<dependency org="com.github.mlk" name="magical-provider-guice" rev="1.0.1" />
```

**Groovy Grape**

```
@Grapes( 
@Grab(group='com.github.mlk', module='magical-provider-guice', version='1.0.1') 
)
```

**Gradle/Grails**

```
compile 'com.github.mlk:magical-provider-guice:1.0.1'
```

**Scala SBT**
```
libraryDependencies += "com.github.mlk" % "magical-provider-guice" % "1.0.1"
```

**Leiningen**

[com.github.mlk/magical-provider-guice "1.0.0"]

** Plain old download **

* [Jar File](http://search.maven.org/remotecontent?filepath=com/github/mlk/magical-provider-guice/1.0.1/magical-provider-guice-1.0.1.jar)

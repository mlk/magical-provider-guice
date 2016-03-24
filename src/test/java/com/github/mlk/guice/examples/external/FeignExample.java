package com.github.mlk.guice.examples.external;

import com.github.mlk.guice.ExternalCreationModule;
import com.github.mlk.guice.examples.external.api.Gists;
import com.github.mlk.guice.examples.external.api.GitHub;
import com.github.mlk.guice.examples.external.dto.Contributor;
import com.github.mlk.guice.examples.external.dto.Gist;
import com.google.inject.Guice;
import com.google.inject.Injector;
import feign.Feign;
import feign.gson.GsonDecoder;

import java.util.List;

public class FeignExample {

    public static void main(String... argv) {
         Feign.Builder builder = Feign.builder()
                .decoder(new GsonDecoder());

        Injector injector = Guice.createInjector(new ExternalCreationModule((x) -> builder.target(x, "https://api.github.com")) {
            @Override
            protected void configure() {
                scan("com.github.mlk.guice.examples.external.api");
            }
        });

        GitHub github = injector.getInstance(GitHub.class);
        Gists gists = injector.getInstance(Gists.class);

        // Fetch and print a list of the contributors to this library.
        List<Contributor> contributors = github.contributors("netflix", "feign");
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }

        // Print out the URLs for the public gists.
        List<Gist> publicGists = gists.getPublicGists();
        for(Gist gist : publicGists) {
            System.out.println(gist.url);
        }
    }

}



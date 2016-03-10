package com.github.mlk.guice.examples.legacy;

public class LegacyService {
    public LegacyService(String host, int port, LegacyAction action) {
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        System.out.println("Action: " + action.getClass());

    }
}

package com.github.mlk.guice.examples.robot;

import com.google.inject.Inject;

class Car {

    private final Engine engine;
    private final Transmission transmission;
    private final Driveline driveline;

    @Inject
    public Car(Engine engine, Transmission transmission, Driveline driveline) {
        this.engine = engine;
        this.transmission = transmission;
        this.driveline = driveline;
    }

    public Driveline getDriveline() {
        return driveline;
    }

    public Engine getEngine() {
        return engine;
    }

    public Transmission getTransmission() {
        return transmission;
    }
}

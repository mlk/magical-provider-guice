package com.github.mlk.guice.examples.robot;

import com.github.mlk.guice.RoboticLegsModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * This example is based on http://pastie.org/368348#
 */
public class RobotLegsProblem2 {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new RoboticLegsModule() {
            @Override
            protected void configure() {
                bind(Driveline.class).to(FrontWheelDrive.class);
                bind(Engine.class).to(DieselEngine.class);

                bindLeg(Car.class, Blue.class, Car.class, (b) -> b.bind(Transmission.class).to(AutomaticTransmission.class));
                bindLeg(Car.class, Red.class, Car.class, (b) -> b.bind(Transmission.class).to(ManualTransmission.class));
            }
        });

        Car blueCar = injector.getInstance(Key.get(Car.class, Blue.class));
        System.out.println("Blue car transmission: " + blueCar.getTransmission());

        Car redCar = injector.getInstance(Key.get(Car.class, Red.class));
        System.out.println("Red car transmission: " + redCar.getTransmission());

    }


}



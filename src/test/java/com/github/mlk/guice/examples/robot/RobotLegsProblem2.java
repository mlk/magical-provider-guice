package com.github.mlk.guice.examples.robot;

import com.github.mlk.guice.RoboticLegsModule;
import com.google.inject.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

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

@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER, METHOD})
@BindingAnnotation
@interface Blue {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER, METHOD})
@BindingAnnotation
@interface Red {
}

class Car {

    private final Engine engine;
    private final Transmission transmission;
    private final Driveline driveline;

    @Inject
    public Car(Engine engine, Transmission transmission, Driveline driveline) {
        this.engine = engine;
        this.transmission = transmission;
        this.driveline = driveline;;
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


interface Transmission {
}

class AutomaticTransmission implements Transmission {
}

class ManualTransmission implements Transmission {
}

interface Engine {
}

class DieselEngine implements Engine {
}

class PetrolEngine implements Engine {
}

interface Driveline {
}

class FourWheelDrive implements Driveline {
}

class FrontWheelDrive implements Driveline {
}

class RearWheelDrive implements Driveline {
}

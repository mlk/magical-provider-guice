package com.github.mlk.guice;

import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RoboticLegsModuleTest {
    @Test
    public void happyPath() {
        final Foot leftFoot = new Foot();
        final Foot rightFoot = new Foot();

        Robot result = Guice.createInjector(new RoboticLegsModule() {
            @Override
            protected void configure() {
                bindLeg(Leg.class, Left.class, BetterLeg.class, (b) -> b.bind(Foot.class).toInstance(leftFoot));
                bindLeg(Leg.class, Right.class, BetterLeg.class, (b) -> b.bind(Foot.class).toInstance(rightFoot));
            }
        }).getInstance(Robot.class);

        assertThat(result.leftLeg.foot, is(leftFoot));
        assertThat(result.rightLeg.foot, is(rightFoot));
    }

    @Test
    public void withNamedAnnotation() {
        final Foot leftFoot = new Foot();
        final Foot rightFoot = new Foot();

        NamedRobot result = Guice.createInjector(new RoboticLegsModule() {
            @Override
            protected void configure() {
                bindLeg(Leg.class, Names.named("left"), BetterLeg.class, (b) -> b.bind(Foot.class).toInstance(leftFoot));
                bindLeg(Leg.class, Names.named("right"), BetterLeg.class, (b) -> b.bind(Foot.class).toInstance(rightFoot));
            }
        }).getInstance(NamedRobot.class);

        assertThat(result.leftLeg.foot, is(leftFoot));
        assertThat(result.rightLeg.foot, is(rightFoot));
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
@interface Left {}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
@interface Right {}


class NamedRobot {
    final Leg leftLeg;
    final Leg rightLeg;

    @Inject
    NamedRobot(@Named("left") Leg leftLeg, @Named("right") Leg rightLeg) {
        this.leftLeg = leftLeg;
        this.rightLeg = rightLeg;
    }
}

class Robot {
    final Leg leftLeg;
    final Leg rightLeg;

    @Inject
    Robot(@Left Leg leftLeg, @Right Leg rightLeg) {
        this.leftLeg = leftLeg;
        this.rightLeg = rightLeg;
    }
}

class Foot {}

class Leg {
    final Foot foot;

    @Inject
    Leg(Foot foot) {
        this.foot = foot;
    }
}

class BetterLeg extends Leg {
    @Inject
    BetterLeg(Foot foot) {
        super(foot);
    }
}
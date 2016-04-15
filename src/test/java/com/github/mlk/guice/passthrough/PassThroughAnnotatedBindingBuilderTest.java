package com.github.mlk.guice.passthrough;

import com.google.inject.binder.AnnotatedBindingBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class PassThroughAnnotatedBindingBuilderTest {
    @Parameterized.Parameters
    public static Object[][] methods() {
        Method[] methods = AnnotatedBindingBuilder.class.getDeclaredMethods();
        Object[][] params = new Object[methods.length][1];
        for (int i = 0; i < methods.length; i++) {
            params[i][0] = methods[i];
        }
        return params;
    }

    private final Method methodToCheck;

    public PassThroughAnnotatedBindingBuilderTest(Method methodToCheck) {
        this.methodToCheck = methodToCheck;
    }

    @Test
    public void checkAllMethods() throws InvocationTargetException, IllegalAccessException {
        AnnotatedBindingBuilder<String> mocked = (AnnotatedBindingBuilder<String>) mock(AnnotatedBindingBuilder.class);

        PassThroughAnnotatedBindingBuilder<String> subject = new PassThroughAnnotatedBindingBuilder<>(mocked);

        Object[] params = new Object[methodToCheck.getParameterCount()];

        methodToCheck.invoke(subject, params);
        methodToCheck.invoke(verify(mocked), params);

    }
}
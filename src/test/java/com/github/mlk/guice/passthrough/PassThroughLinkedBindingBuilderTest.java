package com.github.mlk.guice.passthrough;


import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(Parameterized.class)
public class PassThroughLinkedBindingBuilderTest {
    @Parameterized.Parameters
    public static Object[][] methods() {
        Method[] methods = LinkedBindingBuilder.class.getDeclaredMethods();
        Object[][] params = new Object[methods.length][1];
        for (int i = 0; i < methods.length; i++) {
            params[i][0] = methods[i];
        }
        return params;
    }

    private final Method methodToCheck;

    public PassThroughLinkedBindingBuilderTest(Method methodToCheck) {
        this.methodToCheck = methodToCheck;
    }

    @Test
    public void checkAllMethods() throws InvocationTargetException, IllegalAccessException {
        LinkedBindingBuilder<String> mocked = (LinkedBindingBuilder<String>) mock(AnnotatedBindingBuilder.class);

        PassThroughLinkedBindingBuilder<String> subject = new PassThroughLinkedBindingBuilder<>(mocked);

        Object[] params = new Object[methodToCheck.getParameterCount()];

        methodToCheck.invoke(subject, params);
        methodToCheck.invoke(verify(mocked), params);
        verifyNoMoreInteractions(mocked);
    }
}
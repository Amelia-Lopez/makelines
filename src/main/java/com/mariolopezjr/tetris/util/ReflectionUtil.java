package com.mariolopezjr.tetris.util;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

public class ReflectionUtil {

    private static Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    @Inject
    private Injector injector;

    /**
     * Create a new instance of the specified class
     * @param klass Class<T>
     * @return T
     */
    public <T> T newInstance(final Class<T> klass) {
        return injector.getInstance(klass);
    }

    /**
     * Create a new instance of the specified class
     * @param className String
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T newInstance(final String className) {
        try {
            return (T) injector.getInstance(Class.forName(className));
        } catch (ClassNotFoundException e) {
            log.error("Invalid configuration", e);
            throw new Error("Unable to create new instance of class with name [" + className + "]", e);
        }
    }

    /**
     * Get the Color instance for the color specified in the string
     * @param colorName String
     * @return Color
     */
    public Color getColor(final String colorName) {
        return getField(colorName, Color.class);
    }

    /**
     * Get the KeyEvent instance for the key event specified in the string
     * @param keyEventName String
     * @return int
     */
    public int getKeyEvent(final String keyEventName) {
        try {
            return KeyEvent.class.getField(keyEventName.toUpperCase()).getInt(null);
        } catch (IllegalArgumentException | IllegalAccessException
                | NoSuchFieldException | SecurityException | NullPointerException e) {
            log.error("Invalid configuration.", e);
            throw new Error("Unable to get KeyEvent with name [" + keyEventName + "]", e);
        }
    }

    /**
     * Get a field of the specified class
     * @param fieldName String
     * @param klass Class<T>
     * @return T
     */
    @SuppressWarnings("unchecked")
    private <T> T getField(final String fieldName, final Class<T> klass) {
        T field;

        try {
            field = (T) klass.getField(fieldName.toUpperCase()).get(null);
        } catch (IllegalArgumentException | IllegalAccessException
                | NoSuchFieldException | SecurityException | NullPointerException e) {
            log.error("Invalid configuration.", e);
            throw new Error("Unable to get field with name [" + fieldName + "] for class with name [" + klass + "]", e);
        }

        return field;
    }
}

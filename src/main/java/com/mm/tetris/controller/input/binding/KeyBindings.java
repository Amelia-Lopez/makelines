package com.mm.tetris.controller.input.binding;

import com.google.inject.Injector;
import com.mm.tetris.controller.input.action.gameplay.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the current settings for key bindings
 */
@Singleton
public class KeyBindings {

    @Inject
    private Injector injector;

    /**
     * Map of key strokes to actions
     */
    Map<KeyStroke, GameplayAction> keyBindings;

    /**
     * Constructor
     */
    public KeyBindings() {
        // do nothing
    }

    /**
     * Initialize key bindings
     */
    public void init() {
        // todo: this should be populated using configuration and user preferences
        keyBindings = new HashMap<>();
        keyBindings.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), create(MoveLeftAction.class));
        keyBindings.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), create(MoveRightAction.class));
        keyBindings.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), create(SlowDropAction.class));
        keyBindings.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), create(RotateClockwiseAction.class));
        keyBindings.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), create(RotateCounterClockwiseAction.class));
    }

    /**
     * Get the currently set key bindings
     * @return Map<KeyStroke, GameplayAction>
     */
    public Map<KeyStroke, GameplayAction> getKeyBindings() {
        return keyBindings;
    }

    /**
     * Creates an instance of the specified class using guice
     * @param klass Class<T>
     * @param <T>
     * @return T
     */
    private <T> T create(Class<T> klass) {
        return injector.getInstance(klass);
    }

}

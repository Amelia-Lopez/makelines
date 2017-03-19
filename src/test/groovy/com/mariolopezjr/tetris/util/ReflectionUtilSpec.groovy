package com.mariolopezjr.tetris.util

import com.google.inject.Injector
import spock.lang.Specification
import spock.lang.Unroll

import java.awt.Color
import java.awt.event.KeyEvent

/**
 * Created by mlopez on 3/18/17.
 */
class ReflectionUtilSpec extends Specification {

    ReflectionUtil reflectionUtil

    def setup() {
        reflectionUtil = new ReflectionUtil()
    }

    def "newInstance(class) will call the injector to get an instance"() {
        given: "an instance of a class that the stubbed injector will return"
        String newInstanceFromInjector = new String("the fish flies at night")

        and: "the injector has been injected"
        reflectionUtil.@injector = Stub(Injector) {
            getInstance(String) >> newInstanceFromInjector
        }

        when: "newInstance(class) is called with a valid class"
        String newInstanceFromReflectionUtil = reflectionUtil.newInstance(String)

        then: "the returned instance is the same one the injector gave it"
        newInstanceFromReflectionUtil.is(newInstanceFromInjector)
    }

    def "newInstance(string) will call the injector to get an instance"() {
        given: "an instance of a class that the stubbed injector will return"
        String newInstanceFromInjector = new String("tra la la")

        and: "the injector has been injected"
        reflectionUtil.@injector = Stub(Injector) {
            getInstance(String) >> newInstanceFromInjector
        }

        when: "newInstance(string) is called with a valid class"
        String newInstanceFromReflectionUtil = reflectionUtil.newInstance("java.lang.String")

        then: "the returned instance is the same one the injector gave it"
        newInstanceFromReflectionUtil.is(newInstanceFromInjector)
    }

    def "newInstance(string) will throw an Error when a class could not be found for the specified class name"() {
        when: "newInstance(string) is called with an invalid class"
        reflectionUtil.newInstance("java.lang.JetFuel")

        then: "an Error is thrown"
        thrown(Error)
    }

    @Unroll
    def "getColor(#colorName) will return the expected color #expectedColor"() {
        when: "getColor(colorName) is called with a valid color name"
        def color = reflectionUtil.getColor(colorName)

        then: "an exception is not thrown"
        noExceptionThrown()

        and: "the correct color is returned"
        color == expectedColor

        where:
        colorName    | expectedColor
        "black"      | Color.BLACK
        "blue"       | Color.BLUE
        "green"      | Color.GREEN
        "light_gray" | Color.LIGHT_GRAY
        "magenta"    | Color.MAGENTA
        "orange"     | Color.ORANGE
        "pink"       | Color.PINK
        "red"        | Color.RED
        "white"      | Color.WHITE
        "yellow"     | Color.YELLOW
    }

    @Unroll
    def "getColor(#colorName) will throw an Error since the specified string is not a valid Color"() {
        when: "getColor(colorName) is called with an invalid color name"
        reflectionUtil.getColor(colorName)

        then: "an Error is thrown"
        thrown(Error)

        where:
        colorName << [null, "", "invalid"]
    }

    @Unroll
    def "getKeyEvent(#keyEventName) will return the expected KeyEvent #expectedKeyEvent"() {
        when: "getKeyEvent(keyEventName) is called with a valid key event"
        def keyEvent = reflectionUtil.getKeyEvent(keyEventName)

        then: "an exception is not thrown"
        noExceptionThrown()

        and: "the correct key event is returned"
        keyEvent == expectedKeyEvent

        where:
        keyEventName | expectedKeyEvent
        "VK_A"       | KeyEvent.VK_A
        "VK_H"       | KeyEvent.VK_H
        "VK_N"       | KeyEvent.VK_N
        "VK_O"       | KeyEvent.VK_O
        "VK_P"       | KeyEvent.VK_P
        "VK_Q"       | KeyEvent.VK_Q
        "VK_T"       | KeyEvent.VK_T
    }

    @Unroll
    def "getKeyEvent(#keyEventName) will throw an Error since the specified string is not a valid KeyEvent"() {
        when: "getKeyEvent(keyEventName) is called with an invalid key event"
        reflectionUtil.getKeyEvent(keyEventName)

        then: "an Error is thrown"
        thrown(Error)

        where:
        keyEventName << [null, "", "invalid"]
    }
}

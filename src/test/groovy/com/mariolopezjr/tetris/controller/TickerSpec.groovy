package com.mariolopezjr.tetris.controller

import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by mlopez on 3/16/17.
 */
class TickerSpec extends Specification {

    static class TickerListenerImpl implements TickListener {
        final def count = new AtomicInteger()

        @Override
        void tick() {
            count.incrementAndGet()
        }
    }

    def "should call tick() immediately after starting ticker"() {
        given: "the ticker interval is 10 seconds"
        def intervalInMillis = 10_000
        def ticker = new Ticker()
        def tickerListener = new TickerListenerImpl()
        ticker.setInterval(intervalInMillis).setTickListener(tickerListener)

        when: "the ticker is started"
        ticker.start()

        and: "test waits for 1 second"
        sleep(1_000)

        then: "the ticker listener's tick() was called once"
        tickerListener.count.get() == 1

        cleanup:
        ticker.stop()
    }

    def "should call tick() after the set interval"() {
        given: "the ticker interval is 1 second"
        def intervalInMillis = 1_000
        def ticker = new Ticker()
        def tickerListener = new TickerListenerImpl()
        ticker.setInterval(intervalInMillis).setTickListener(tickerListener)

        when: "the ticker is started"
        ticker.start()

        and: "test waits for 1.5 seconds"
        sleep(1_500)

        then: "the ticker listener's tick() was called twice, once immediately and once more after the interval"
        tickerListener.count.get() == 2

        cleanup:
        ticker.stop()
    }

    def "should call tick() twice more after the interval has passed twice"() {
        given: "the ticker interval is 1 second"
        def intervalInMillis = 1_000
        def ticker = new Ticker()
        def tickerListener = new TickerListenerImpl()
        ticker.setInterval(intervalInMillis).setTickListener(tickerListener)

        when: "the ticker is started"
        ticker.start()

        and: "test waits for 2.5 seconds"
        sleep(2_500)

        then: "the ticker listener's tick() was called three times in total, once immediately and then twice more"
        tickerListener.count.get() == 3

        cleanup:
        ticker.stop()
    }

    def "should never call tick() if ticker is never started"() {
        given: "the ticker interval is 1 second"
        def intervalInMillis = 1_000
        def ticker = new Ticker()
        def tickerListener = new TickerListenerImpl()
        ticker.setInterval(intervalInMillis).setTickListener(tickerListener)

        when: "test waits for 1.5 seconds"
        sleep(1_500)

        then: "the ticker listener's tick() is never called"
        tickerListener.count.get() == 0
    }

    def "should stop calling tick() after ticker is stopped"() {
        given: "the ticker interval is 1 second"
        def intervalInMillis = 1_000
        def ticker = new Ticker()
        def tickerListener = new TickerListenerImpl()
        ticker.setInterval(intervalInMillis).setTickListener(tickerListener)

        when: "the ticker is started"
        ticker.start()

        and: "test waits for 1.5 seconds"
        sleep(1_500)

        then: "the ticker listener's tick() was called twice"
        tickerListener.count.get() == 2

        when: "the ticker is stopped"
        ticker.stop()

        and: "the ticker listener's count is reset"
        tickerListener.count.set(0)

        and: "test waits for 2 seconds"
        sleep(2_000)

        then: "the ticker listener's tick() was not called again"
        tickerListener.count.get() == 0
    }
}

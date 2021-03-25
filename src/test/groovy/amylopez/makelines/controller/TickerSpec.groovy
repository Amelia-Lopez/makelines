package amylopez.makelines.controller

import spock.lang.Specification

/**
 * Created by alopez on 3/16/17.
 */
class TickerSpec extends Specification {

    Ticker ticker

    def tickerListener = Mock(TickListener)

    def setup() {
        ticker = new Ticker()
        ticker.tickListener = tickerListener
    }

    def cleanup() {
        ticker.stop()
    }

    def "should call tick() immediately after starting ticker"() {
        given: "the ticker interval is 10 seconds"
        ticker.interval = 10_000

        when: "the ticker is started"
        ticker.start()

        and: "test waits for 1 second"
        sleep(1_000)

        then: "the ticker listener's tick() was called once"
        1 * tickerListener.tick()
    }

    def "should call tick() after the set interval"() {
        given: "the ticker interval is 1 second"
        ticker.interval = 1_000

        when: "the ticker is started"
        ticker.start()

        and: "test waits for 1.5 seconds"
        sleep(1_500)

        then: "the ticker listener's tick() was called twice, once immediately and once more after the interval"
        2 * tickerListener.tick()
    }

    def "should call tick() twice more after the interval has passed twice"() {
        given: "the ticker interval is 1 second"
        ticker.interval = 1_000

        when: "the ticker is started"
        ticker.start()

        and: "test waits for 2.5 seconds"
        sleep(2_500)

        then: "the ticker listener's tick() was called three times in total, once immediately and then twice more"
        3 * tickerListener.tick()
    }

    def "should never call tick() if ticker is never started"() {
        given: "the ticker interval is 1 second"
        ticker.interval = 1_000

        when: "test waits for 1.5 seconds"
        sleep(1_500)

        then: "the ticker listener's tick() is never called"
        0 * tickerListener.tick()
    }

    def "should stop calling tick() after ticker is stopped"() {
        given: "the ticker interval is 1 second"
        ticker.interval = 1_000

        when: "the ticker is started"
        ticker.start()

        and: "test waits for 1.5 seconds"
        sleep(1_500)

        then: "the ticker listener's tick() was called twice"
        2 * tickerListener.tick()

        when: "the ticker is stopped"
        ticker.stop()

        and: "test waits for 2 seconds"
        sleep(2_000)

        then: "the ticker listener's tick() was not called again"
        0 * tickerListener.tick()
    }
}

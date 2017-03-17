package com.mariolopezjr.tetris.score.impl

import com.mariolopezjr.tetris.score.ScoreObserver
import org.apache.commons.configuration.Configuration
import spock.lang.Specification

/**
 * Created by mlopez on 3/16/17.
 */
class ScoreKeeperImplSpec extends Specification {

    ScoreKeeperImpl scoreKeeperImpl

    def scoreObserver = Mock(ScoreObserver)

    def defaultConfig = Stub(Configuration) {
        getInt("scoring/cleared_rows/@size") >> 1
        getInt("scoring/cleared_rows/rows[1]/@number") >> 1
        getInt("scoring/cleared_rows/rows[1]/@score") >> 100
        getInt("scoring/specials/@size") >> 3
        getString("scoring/specials/special[1]/@type") >> "entireBoard"
        getInt("scoring/specials/special[1]/@score") >> 10000
        getString("scoring/specials/special[2]/@type") >> "dropRow"
        getInt("scoring/specials/special[2]/@score") >> 1
        getString("scoring/specials/special[3]/@type") >> "rowSameColor"
        getInt("scoring/specials/special[3]/@score") >> 500
    }

    def setup() {
        scoreKeeperImpl = new ScoreKeeperImpl()
    }

    def "score board can be reset"() {
        when: "reset() is called"
        scoreKeeperImpl.reset()

        then: "no exception is thrown since no observers were added"
        noExceptionThrown()

        and: "the score board was reset"
        scoreKeeperImpl.level == 1
        scoreKeeperImpl.rows == 0
        scoreKeeperImpl.score == 0
    }

    def "observer is informed when score board is reset"() {
        given: "the observer is added"
        scoreKeeperImpl.addObserver(scoreObserver)

        when: "reset() is called"
        scoreKeeperImpl.reset()

        then: "the observer is informed"
        1 * scoreObserver.scoreUpdate([Level: 1, Rows: 0, Score: 0])
    }

    def "score board will not throw an exception when being initialized with a stubbed config"() {
        given: "score board is injected with the stubbed config"
        scoreKeeperImpl.@config = defaultConfig

        when: "initializing the score board"
        scoreKeeperImpl.init()

        then: "no exception is thrown"
        noExceptionThrown()
    }
}

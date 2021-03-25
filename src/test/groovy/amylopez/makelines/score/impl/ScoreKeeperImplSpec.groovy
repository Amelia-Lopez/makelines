package amylopez.makelines.score.impl


import org.apache.commons.configuration.Configuration
import org.spockframework.runtime.SpockAssertionError
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by alopez on 3/16/17.
 */
class ScoreKeeperImplSpec extends Specification {

    private static final Map<Integer, Integer> CLEAR_ROW_SCORING = [1: 100, 2: 300, 3: 600, 4: 1000]
    private static final int ENTIRE_BOARD_SCORING = 10000
    private static final int FAST_DROP_SCORING = 1

    ScoreKeeperImpl scoreKeeperImpl

    def scoreObserver = Mock(amylopez.makelines.score.ScoreObserver)

    def defaultConfig = Stub(Configuration) {
        getInt("scoring/cleared_rows/@size") >> CLEAR_ROW_SCORING.size()
        getInt("scoring/cleared_rows/rows[1]/@number") >> 1
        getInt("scoring/cleared_rows/rows[1]/@score") >> CLEAR_ROW_SCORING[1]
        getInt("scoring/cleared_rows/rows[2]/@number") >> 2
        getInt("scoring/cleared_rows/rows[2]/@score") >> CLEAR_ROW_SCORING[2]
        getInt("scoring/cleared_rows/rows[3]/@number") >> 3
        getInt("scoring/cleared_rows/rows[3]/@score") >> CLEAR_ROW_SCORING[3]
        getInt("scoring/cleared_rows/rows[4]/@number") >> 4
        getInt("scoring/cleared_rows/rows[4]/@score") >> CLEAR_ROW_SCORING[4]
        getInt("scoring/specials/@size") >> 3
        getString("scoring/specials/special[1]/@type") >> "entireBoard"
        getInt("scoring/specials/special[1]/@score") >> ENTIRE_BOARD_SCORING
        getString("scoring/specials/special[2]/@type") >> "dropRow"
        getInt("scoring/specials/special[2]/@score") >> FAST_DROP_SCORING
        getString("scoring/specials/special[3]/@type") >> "rowSameColor"
        getInt("scoring/specials/special[3]/@score") >> 500
    }

    def setup() {
        scoreKeeperImpl = new ScoreKeeperImpl()
    }

    def "score board can be reset"() {
        when: "reset() is called"
        scoreKeeperImpl.reset()

        then: "an exception is not thrown when trying to contact observers since none were added"
        noExceptionThrown()

        and: "the score board was reset"
        scoreKeeperImpl.level == 1
        scoreKeeperImpl.rows == 0
        scoreKeeperImpl.score == 0
    }

    def "observer is informed when score board is reset"() {
        given: "an observer is added"
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

        then: "an exception is not thrown"
        noExceptionThrown()
    }

    @Unroll
    def "clearing #rowNum row(s) will increase the row count by that amount"() {
        given: "score board is injected with the stubbed config and is initialized"
        scoreKeeperImpl.@config = defaultConfig
        scoreKeeperImpl.init()

        and: "the rows is initially zero"
        scoreKeeperImpl.@rows = 0

        when: "clearedRows(rowNum) is called"
        scoreKeeperImpl.clearedRows(rowNum)

        then: "the number of rows were increased to the correct amount"
        scoreKeeperImpl.rows == rowNum

        where:
        rowNum << (1..4)
    }

    @Unroll
    def "clearing #rowNum row(s) for a total of #totalRowNum row(s) cleared results in level #expectedLevel"() {
        given: "score board is injected with the stubbed config and is initialized"
        scoreKeeperImpl.@config = defaultConfig
        scoreKeeperImpl.init()

        and: "the score is initially set to the desired totalRowNum minus the rowNum about to be cleared"
        scoreKeeperImpl.@rows = totalRowNum - rowNum

        when: "clearedRows(rowNum) is called"
        scoreKeeperImpl.clearedRows(rowNum)

        then:
        scoreKeeperImpl.level == expectedLevel

        where:
        rowNum | totalRowNum | expectedLevel
        1      | 1           | 1
        2      | 2           | 1
        3      | 3           | 1
        4      | 4           | 1
        1      | 10          | 2
        2      | 21          | 3
        3      | 32          | 4
        4      | 93          | 10
        1      | 100         | 11
        4      | 222         | 23
        4      | 333         | 34
    }

    @Unroll
    def "clearing #rowNum row(s) will increase the score by the configured amount"() {
        given: "score board is injected with the stubbed config and is initialized"
        scoreKeeperImpl.@config = defaultConfig
        scoreKeeperImpl.init()

        and: "the score is initially zero"
        scoreKeeperImpl.@score = 0

        when: "clearedRows(rowNum) is called"
        scoreKeeperImpl.clearedRows(rowNum)

        then: "the score was increased to the correct amount"
        scoreKeeperImpl.score == CLEAR_ROW_SCORING[rowNum]

        where:
        rowNum << (1..4)
    }

    def "observer will be informed when a row is cleared"() {
        given: "score board is injected with the stubbed config and is initialized"
        scoreKeeperImpl.@config = defaultConfig
        scoreKeeperImpl.init()

        and: "an observer is added"
        scoreKeeperImpl.addObserver(scoreObserver)

        and: "the level, rows, and score are 1, 0, and 0 respectively"
        scoreKeeperImpl.with {
            level = 1
            rows = 0
            score = 0
        }

        when: "clearedRows(1) is called"
        scoreKeeperImpl.clearedRows(1)

        then: "the observer is informed of the cleared row"
        1 * scoreObserver.scoreUpdate([Level: 1, Rows: 1, Score: CLEAR_ROW_SCORING[1]])
    }

    def "when the entire board is cleared, the configured special scoring is applied to the score"() {
        given: "score board is injected with the stubbed config and is initialized"
        scoreKeeperImpl.@config = defaultConfig
        scoreKeeperImpl.init()

        and: "the score is initially zero"
        scoreKeeperImpl.@score = 0

        when: "clearedEntireBoard() is called"
        scoreKeeperImpl.clearedEntireBoard()

        then: "the score is increased by the configured amount"
        scoreKeeperImpl.score == ENTIRE_BOARD_SCORING
    }

    @FailsWith(value = SpockAssertionError, reason = "config should be refactored to prevent this potential failure")
    def "when the entire board is cleared and there is no configured special scoring for this event, the score is not changed"() {
        given: "an empty config that does not have the special scoring for clearing the entire board"
        def emptyConfig = Stub(Configuration) {
            getInt("scoring/cleared_rows/@size") >> 0
            getInt("scoring/specials/@size") >> 0
        }

        and: "score board is injected with the stubbed config and is initialized"
        scoreKeeperImpl.@config = emptyConfig
        scoreKeeperImpl.init()

        and: "the score is initially zero"
        scoreKeeperImpl.@score = 0

        when: "clearedEntireBoard() is called"
        scoreKeeperImpl.clearedEntireBoard()

        then: "an exception is not thrown"
        noExceptionThrown()

        and: "the score is still zero"
        scoreKeeperImpl.score == 0
    }

    def "when the tetromino in play is fast dropped by #dropHeight rows, the configured special scoring is used when updating the score"() {
        given: "score board is injected with the stubbed config and is initialized"
        scoreKeeperImpl.@config = defaultConfig
        scoreKeeperImpl.init()

        and: "the score is initially zero"
        scoreKeeperImpl.@score = 0

        when: "fastDrop(dropHeight) is called"
        scoreKeeperImpl.fastDrop(dropHeight)

        then:
        scoreKeeperImpl.score == FAST_DROP_SCORING * dropHeight

        where:
        dropHeight << (1..6)
    }

    @FailsWith(value = SpockAssertionError, reason = "config should be refactored to prevent this potential failure")
    def "when the tetromino in play is fast dropped and there is no configured special scoring for this event, the score is not changed"() {
        given: "an empty config that does not have the special scoring for fast dropping the tetromino"
        def emptyConfig = Stub(Configuration) {
            getInt("scoring/cleared_rows/@size") >> 0
            getInt("scoring/specials/@size") >> 0
        }

        and: "score board is injected with the stubbed config and is initialized"
        scoreKeeperImpl.@config = emptyConfig
        scoreKeeperImpl.init()

        and: "the score is initially zero"
        scoreKeeperImpl.@score = 0

        when: "fastDrop(1) is called"
        scoreKeeperImpl.fastDrop(1)

        then: "an exception is not thrown"
        noExceptionThrown()

        and: "the score is still zero"
        scoreKeeperImpl.score == 0
    }
}

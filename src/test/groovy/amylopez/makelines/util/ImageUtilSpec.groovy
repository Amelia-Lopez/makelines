package amylopez.makelines.util

import spock.lang.Specification

/**
 * Created by alopez on 3/15/17.
 */
class ImageUtilSpec extends Specification {
    private static final String IMAGE_FILE_NAME = "/swirl_blue.png"
    private static final int ACTUAL_WIDTH = 22
    private static final int ACTUAL_HEIGHT = 22

    def "should use the default dimensions of the image when they are not specified"() {
        when:
        def bufferedImage = new ImageUtil().getImageFromFile(IMAGE_FILE_NAME)

        then:
        bufferedImage.getWidth() == ACTUAL_WIDTH
        bufferedImage.getHeight() == ACTUAL_HEIGHT
    }

    def "should use the specified dimensions when they are specified"() {
        given:
        def width = 1000
        def height = 2000

        when:
        def bufferedImage = new ImageUtil().getImageFromFile(IMAGE_FILE_NAME, width, height)

        then:
        bufferedImage.getWidth() == width
        bufferedImage.getHeight() == height
    }

    def "should get an exception when negative width is specified"() {
        given:
        def width = -1
        def height = 100

        when:
        new ImageUtil().getImageFromFile(IMAGE_FILE_NAME, width, height)

        then:
        thrown Exception
    }

    def "should get an exception when negative height is specified"() {
        given:
        def width = 100
        def height = -1

        when:
        new ImageUtil().getImageFromFile(IMAGE_FILE_NAME, width, height)

        then:
        thrown Exception
    }

    def "should get an exception when a non-existent file is specified"() {
        given:
        def badFileName = "pandas"

        when:
        new ImageUtil().getImageFromFile(badFileName)

        then:
        thrown Exception
    }
}

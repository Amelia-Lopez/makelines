package com.mariolopezjr.tetris.util;

import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * Created by mlopez on 3/15/17.
 */
public class ImageUtilTest {
    private static final String IMAGE_FILE_NAME = "/swirl_blue.png";
    private static final int ACTUAL_WIDTH = 22;
    private static final int ACTUAL_HEIGHT = 22;

    @Test
    public void testGetImageFromFileWithoutDimensionsSpecified() throws Exception {
        BufferedImage bi = new ImageUtil().getImageFromFile(IMAGE_FILE_NAME);

        // then it should default to the actual dimensions of the image file
        assertEquals(ACTUAL_WIDTH, bi.getWidth());
        assertEquals(ACTUAL_HEIGHT, bi.getHeight());
    }

    @Test
    public void testGetImageFromFileWithDimensionsSpecified() throws Exception {
        int width = 1000;
        int height = 2000;

        BufferedImage bi = new ImageUtil().getImageFromFile(IMAGE_FILE_NAME, width, height);

        assertEquals(width, bi.getWidth());
        assertEquals(height, bi.getHeight());
    }

    @Test(expected = Exception.class)
    public void testGetImageFromFileWithBadDimensionsSpecified() throws Exception {
        int width = -1;
        int height = -2;

        new ImageUtil().getImageFromFile(IMAGE_FILE_NAME, width, height);
    }

    @Test(expected = Exception.class)
    public void testGetImageFromFileWithBadFileNameSpecified() throws Exception {
        new ImageUtil().getImageFromFile("nowhere");
    }
}

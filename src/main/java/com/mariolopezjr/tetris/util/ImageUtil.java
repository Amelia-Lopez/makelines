package com.mariolopezjr.tetris.util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {

    private static Logger log = LoggerFactory.getLogger(ImageUtil.class);


    /**
     * Load the specified image file into a BufferedImage
     * @param fileName String
     * @param width int the desired width, 0 to default to the image height
     * @param height int the desired height, 0 to default to the image width
     * @return BufferedImage
     */
    public BufferedImage getImageFromFile(String fileName, int width, int height) {
        log.debug("File name: " + fileName);
        BufferedImage image = null;

        try {
            BufferedImage rawImage = ImageIO.read(this.getClass().getResource(fileName));

            GraphicsConfiguration gc = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();
            image = gc.createCompatibleImage(
                    width == 0 ? rawImage.getWidth(null) : width,
                    height == 0 ? rawImage.getHeight(null) : height);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setPaint(new TexturePaint(
                    rawImage,
                    new Rectangle(0, 0, rawImage.getWidth(), rawImage.getHeight())));
            g.fillRect(0, 0, image.getWidth(), image.getHeight());

        } catch (IOException e) {
            log.error("Unable to load image: " + fileName, e);
            System.exit(1);
        }

        return image;
    }

    /**
     * Load the specified image file into a BufferedImage
     * @param fileName String
     * @return BufferedImage
     */
    public BufferedImage getImageFromFile(String fileName) {
        return getImageFromFile(fileName, 0, 0);
    }
}

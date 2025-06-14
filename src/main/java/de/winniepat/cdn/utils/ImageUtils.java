package de.winniepat.cdn.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static BufferedImage asBuffered(Image image) {
        if (image instanceof BufferedImage) return (BufferedImage) image;

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return bufferedImage;
    }
}

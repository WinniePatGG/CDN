package de.winniepat.cdn.renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class ImageRenderer {

    protected final ImageType imageType;

    public ImageRenderer(ImageType imageType) {
        this.imageType = imageType;
    }

    public BufferedImage render(File file, Object... args) throws IOException {
        return this.render(ImageIO.read(file), args);
    }

    public abstract BufferedImage render(Image input, Object... args) throws IOException;

    public final ImageType getImageType() {
        return imageType;
    }
}

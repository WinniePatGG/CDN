package de.winniepat.cdn.renderer.renderers;

import de.winniepat.cdn.renderer.ImageRenderer;
import de.winniepat.cdn.renderer.ImageType;
import de.winniepat.cdn.utils.ImageUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CapeRenderer extends ImageRenderer {

    public CapeRenderer() {
        super(ImageType.CAPES);
    }

    @Override
    public BufferedImage render(Image input, Object... args) throws IOException {
        BufferedImage image = ImageUtils.asBuffered(input);
        int width = image.getWidth();
        return Thumbnails.of(image)
                .outputQuality(1)
                .rendering(Rendering.QUALITY)
                .outputFormat("webp")
                .forceSize(width, width / 2)
                .asBufferedImage();
    }
}

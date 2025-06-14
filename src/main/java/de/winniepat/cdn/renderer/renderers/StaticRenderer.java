package de.winniepat.cdn.renderer.renderers;

import de.winniepat.cdn.renderer.ImageRenderer;
import de.winniepat.cdn.renderer.ImageType;
import de.winniepat.cdn.utils.ImageUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StaticRenderer extends ImageRenderer {
    public StaticRenderer() {
        super(ImageType.STATIC);
    }

    @Override
    public BufferedImage render(Image input, Object... args) throws IOException {
        BufferedImage image = ImageUtils.asBuffered(input);
        return Thumbnails.of(image)
                .outputQuality(1)
                .rendering(Rendering.QUALITY)
                .outputFormat("webp")
                .size(image.getWidth(), image.getHeight())
                .asBufferedImage();
    }
}

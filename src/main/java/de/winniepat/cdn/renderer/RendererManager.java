package de.winniepat.cdn.renderer;

import de.winniepat.cdn.renderer.renderers.CapeRenderer;
import de.winniepat.cdn.renderer.renderers.StaticRenderer;

import java.util.EnumMap;

public class RendererManager {

    private final EnumMap<ImageType, ImageRenderer> renderers = new EnumMap<>(ImageType.class);

    public RendererManager(){
        this.register(new CapeRenderer());
        this.register(new StaticRenderer());
    }

    public void register(ImageRenderer renderer) {
        if (isRegistered(renderer)) return;
        renderers.put(renderer.getImageType(), renderer);
    }

    public void unregister(ImageRenderer renderer) {
        this.unregister(renderer.getImageType());
    }

    public void unregister(ImageType imageType) {
        this.renderers.remove(imageType);
    }

    public boolean isRegistered(ImageRenderer renderer) {
        return this.isRegistered(renderer.getImageType());
    }

    public boolean isRegistered(ImageType imageType) {
        return this.renderers.containsKey(imageType);
    }

    public ImageRenderer retrieve(ImageType imageType) {
        return this.renderers.get(imageType);
    }

}

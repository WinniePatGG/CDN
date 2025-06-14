package de.winniepat.cdn;

import de.craftsblock.craftsnet.CraftsNet;
import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.addon.meta.annotations.Meta;
import de.craftsblock.craftsnet.builder.ActivateType;
import de.winniepat.cdn.renderer.RendererManager;

import java.io.File;
import java.io.IOException;

@Meta(name = "CDN")
public class CDN extends Addon {

    public static void main(String[] args) throws IOException {
        CraftsNet.create(CDN.class)
                .withAddonSystem(ActivateType.DISABLED)
                .withFileLogger(ActivateType.DISABLED)
                .withDebug(true)
                .withArgs(args)
                .build();
    }

    private RendererManager rendererManager;

    @Override
    public void onEnable() {
        this.rendererManager = new RendererManager();
        File live = new File(getDataFolder(), "/live");
        if (!live.exists()) live.mkdirs();
        routeRegistry().share("/v1/cdn/images", live);
    }

    public RendererManager getRendererManager() {
        return rendererManager;
    }
}

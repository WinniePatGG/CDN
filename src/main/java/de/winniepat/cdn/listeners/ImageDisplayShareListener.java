package de.winniepat.cdn.listeners;

import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.events.requests.shares.ShareRequestEvent;

@AutoRegister
public class ImageDisplayShareListener implements ListenerAdapter {

    @EventHandler
    public void handleImageDisplayShareRequest(ShareRequestEvent event) {
        String url = event.getHttpPath();
        String path = event.getFilePath();
        if (!url.matches("^/?v1/cdn/images/(?!meta)/?.*$")) return;
        if (path.matches("^.*\\.webp$")) return;

        event.setFilePath(path + ".webp");
    }
}

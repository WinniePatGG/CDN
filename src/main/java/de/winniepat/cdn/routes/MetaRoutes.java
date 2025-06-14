package de.winniepat.cdn.routes;

import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftscore.utils.id.Snowflake;
import de.craftsblock.craftsnet.api.http.*;
import de.craftsblock.craftsnet.api.http.annotations.RequestMethod;
import de.craftsblock.craftsnet.api.http.annotations.RequireBody;
import de.craftsblock.craftsnet.api.http.annotations.Route;
import de.craftsblock.craftsnet.api.http.body.bodies.MultipartFormBody;
import de.craftsblock.craftsnet.api.transformers.annotations.Transformer;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.winniepat.cdn.CDN;
import de.winniepat.cdn.renderer.ImageRenderer;
import de.winniepat.cdn.renderer.ImageType;
import de.winniepat.cdn.utils.FileNameSanitizeTransformer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

@AutoRegister
@Route("/v1/cdn/meta")
public class MetaRoutes implements RequestHandler {

    private final CDN cdn;

    public MetaRoutes(CDN cdn) {
        this.cdn = cdn;
    }

    @Route("/{category}/{name}/{file}")
    @RequestMethod(HttpMethod.GET)
    @Transformer(parameter = "file", transformer = FileNameSanitizeTransformer.class)
    public void handleInfo(Exchange exchange, String category, String name, String file) throws IOException {
        final Response response = exchange.response();

        Path path = getImagePath(category, name, file);
        if (Files.notExists(path)) {
            if (!response.headersSent()) response.setCode(404);
            response.print(Json.empty().set("status", "404").set("message", "Image not found"));
            return;
        }

        BasicFileAttributeView attributeView = Files.getFileAttributeView(path, BasicFileAttributeView.class);
        BasicFileAttributes attributes = attributeView.readAttributes();

        response.print(Json.empty().set("status", "200")
                .set("url", "/v1/cdn/images/" + category + "/" + name + "/" + file + ".webp")
                .set("attributes.size", attributes.size())
                .set("attributes.timestamps.creation", attributes.creationTime().toMillis())
                .set("attributes.timestamps.lastupdate", attributes.lastModifiedTime().toMillis()));
    }

    @Route("/{category}/{name}/{file}")
    @RequestMethod(HttpMethod.DELETE)
    @Transformer(parameter = "file", transformer = FileNameSanitizeTransformer.class)
    public void handleDelete(Exchange exchange, String category, String name, String file) throws IOException {
        final Response response = exchange.response();

        Path path = getImagePath(category, name, file);
        if (Files.notExists(path)) {
            if (!response.headersSent()) response.setCode(404);
            response.print(Json.empty().set("status", "404").set("message", "Image not found"));
            return;
        }

        Files.deleteIfExists(path);
        response.print(Json.empty().set("status", "200").set("message", "Image deleted"));
    }

    @Route("/{name}")
    @RequestMethod(HttpMethod.POST)
    @RequireBody(MultipartFormBody.class)
    public void handleCreate(Exchange exchange, String name) throws Exception {
        final Request request = exchange.request();
        final Response response = exchange.response();

        MultipartFormBody body = request.getBody().getAsMultipartFormBody();
        if (!body.hasField("image")) {
            response.print(Json.empty().set("status", "400").set("message", "No field named image found"));
            return;
        }

        MultipartFormBody.MultipartData field = body.getField("image");
        if (field.isEmpty()) {
            response.print(Json.empty().set("status", "400").set("message", "The field named image must not be empty"));
            return;
        }

        MultipartFormBody.MultipartItem item = field.first();
        if (!item.contentType().trim().matches("image/(?:jpe?g|png|webp)")) {
            response.print(Json.empty().set("status", "406").set("message", "The field named image must contain an image"));
            return;
        }

        if (!item.validateContentType()) {
            response.print(Json.empty().set("status", "406").set("message", "Failed the content type check"));
            return;
        }

        long id = Snowflake.generate();
        ImageType imageType = ImageType.parse(request.retrieveParam("t"));
        File destination = new File(cdn.getDataFolder(), "live/" + imageType.getTypeName().toLowerCase() + "/" + name + "/" + id + ".webp");

        if (destination.getParentFile() != null && !destination.getParentFile().exists())
            destination.getParentFile().mkdirs();
        destination.createNewFile();

        File source = item.getAsFile();
        ImageRenderer renderer = cdn.getRendererManager().retrieve(imageType);
        BufferedImage image = renderer.render(source);

        ImageIO.write(image, "webp", destination);

        response.print(Json.empty().set("status", "200").set("message", "Image uploaded successfully")
                .set("link", "/v1/cdn/images/" + imageType.getTypeName().toLowerCase() + "/" + name + "/" + id + ".webp"));
    }

    private Path getImagePath(String category, String name, String file) {
        Path relativePath = Path.of("live", category, name, file + ".webp");
        return cdn.getDataFolder().toPath().resolve(relativePath);
        }
    }

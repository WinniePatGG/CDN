package de.winniepat.cdn.utils;

import de.craftsblock.craftsnet.api.transformers.Transformable;

public class FileNameSanitizeTransformer implements Transformable<String> {

    @Override
    public String transform(String parameter) {
        return parameter.substring(0, parameter.lastIndexOf('.'));
    }
}

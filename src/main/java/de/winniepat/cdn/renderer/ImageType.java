package de.winniepat.cdn.renderer;

public enum ImageType {

    STATIC("static"),
    CAPES("capes");

    private final String typeName;

    ImageType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static ImageType parse(String typeName) {
        if (typeName == null) return STATIC;
        if (typeName.equalsIgnoreCase(ImageType.STATIC.typeName)) return STATIC;

        for (ImageType type : ImageType.values())
            if (!type.equals(ImageType.STATIC) && type.getTypeName().equalsIgnoreCase(typeName))
                return type;
        return STATIC;
    }
}

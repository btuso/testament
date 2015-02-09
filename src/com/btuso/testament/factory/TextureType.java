package com.btuso.testament.factory;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;

public enum TextureType {
    BACKGROUND(BitmapTextureFormat.RGB_565, TextureOptions.NEAREST),
    MOB(BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);

    private BitmapTextureFormat format;
    private TextureOptions options;

    private TextureType(BitmapTextureFormat format, TextureOptions options) {
        this.format = format;
        this.options = options;
    }

    public BitmapTextureFormat getFormat() {
        return format;
    }

    public TextureOptions getOptions() {
        return options;
    }
}

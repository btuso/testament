package com.btuso.testament.factory.gamescene;

import com.btuso.testament.factory.Resources;
import com.btuso.testament.factory.TextureType;

public enum GameSceneAssets {
    STAIRS(TextureType.BACKGROUND, Resources.GS_STAIRS),
    LOWER_HALF(TextureType.BACKGROUND, Resources.GS_LOWER_HALF),
    MIDDLE(TextureType.BACKGROUND, Resources.GS_MIDDLE),
    BAT(TextureType.MOB, Resources.GS_BAT, 6, 1);

    private TextureType textureType;
    private String resource;
    private boolean tiled;
    private int columns;
    private int rows;

    private GameSceneAssets(TextureType textureType, String resource) {
        this.textureType = textureType;
        this.resource = resource;
    }

    private GameSceneAssets(TextureType textureType, String resource, int columns, int rows) {
        this.textureType = textureType;
        this.resource = resource;
        this.tiled = true;
        this.columns = columns;
        this.rows = rows;
    }

    public TextureType getTextureType() {
        return textureType;
    }

    public String getResource() {
        return resource;
    }

    public boolean isTiled() {
        return tiled;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}

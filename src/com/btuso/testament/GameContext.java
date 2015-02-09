package com.btuso.testament;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.content.res.AssetManager;

public class GameContext {

    private final Camera                    camera;
    private final AssetManager              assetManager;
    private final TextureManager            textureManager;
    private final VertexBufferObjectManager vertexBuffer;

    public GameContext(Context context, Engine engine) {
        // TODO remove engine parameter, take individual objects
        this.camera = engine.getCamera();
        assetManager = context.getAssets();
        this.textureManager = engine.getTextureManager();
        this.vertexBuffer = engine.getVertexBufferObjectManager();
    }

    public Camera getCamera() {
        return camera;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public VertexBufferObjectManager getVertexBuffer() {
        return vertexBuffer;
    }

}

package com.btuso.testament.scene.gamescene.factory;

import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.res.AssetManager;

import com.btuso.testament.GameContext;
import com.btuso.testament.factory.TextureType;
import com.btuso.testament.scene.gamescene.GameSceneAssets;

public class GameSceneTextureFactory {

    private final AssetManager assetManager;
    private final TextureManager textureManager;
    private final Map<GameSceneAssets, BaseTextureRegion> textures = new HashMap<GameSceneAssets, BaseTextureRegion>();
    private final Map<TextureType, BuildableBitmapTextureAtlas> atlases = new HashMap<TextureType, BuildableBitmapTextureAtlas>();

    public GameSceneTextureFactory(GameContext context) {
        this.assetManager = context.getAssetManager();
        this.textureManager = context.getTextureManager();
        createTextureAtlases();
        createTextures();
        loadTextures();
    }

    private void createTextureAtlases() {
        atlases.put(TextureType.BACKGROUND, createBuildableAtlas(290, 540, TextureType.BACKGROUND));
        atlases.put(TextureType.MOB, createBuildableAtlas(200, 68, TextureType.MOB));
    }

    private BuildableBitmapTextureAtlas createBuildableAtlas(int width, int height, TextureType texType) {
        return new BuildableBitmapTextureAtlas(textureManager, width, height, texType.getFormat(), texType.getOptions());
    }

    private void createTextures() {
        for (GameSceneAssets asset : GameSceneAssets.values()) {
            BuildableBitmapTextureAtlas atlas = atlases.get(asset.getTextureType());
            BaseTextureRegion texture = createTexture(atlas, asset);
            textures.put(asset, texture);
        }
    }

    private BaseTextureRegion createTexture(BuildableBitmapTextureAtlas atlas, GameSceneAssets asset) {
        if (asset.isTiled()) {
            return BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas, assetManager,
                    asset.getResource(), asset.getColumns(), asset.getRows());
        } else {
            return BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, assetManager, asset.getResource());
        }
    }

    private void loadTextures() {
        for (BuildableBitmapTextureAtlas atlas : atlases.values()) {
            try {
                atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 3, 1));
                atlas.load();
            } catch (TextureAtlasBuilderException e) {
                e.printStackTrace();
            }
        }
    }

    public TextureRegion getTextureRegionFor(GameSceneAssets asset) {
        BaseTextureRegion baseTextureRegion = textures.get(asset);
        if (baseTextureRegion instanceof TextureRegion) {
            return (TextureRegion) baseTextureRegion;
        } else {
            throw new RuntimeException("Texture must be normal for " + asset);
        }
    }

    public TiledTextureRegion getTiledTextureRegionFor(GameSceneAssets asset) {
        BaseTextureRegion baseTextureRegion = textures.get(asset);
        if (baseTextureRegion instanceof TiledTextureRegion) {
            return (TiledTextureRegion) baseTextureRegion;
        } else {
            throw new RuntimeException("Texture must be Tiled for " + asset);
        }
    }

}

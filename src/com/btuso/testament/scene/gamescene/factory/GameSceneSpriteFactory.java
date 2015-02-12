package com.btuso.testament.scene.gamescene.factory;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.btuso.testament.GameContext;
import com.btuso.testament.scene.gamescene.GameSceneAssets;

public class GameSceneSpriteFactory {

    private final VertexBufferObjectManager vertexBuffer;
    private final GameSceneTextureFactory textureFactory;

    public GameSceneSpriteFactory(GameContext context, GameSceneTextureFactory textures) {
        this.vertexBuffer = context.getVertexBuffer();
        this.textureFactory = textures;
    }

    public Sprite createStairsLowerPart() {
        return new Sprite(0, 0, textureFactory.getTextureRegionFor(GameSceneAssets.LOWER_HALF), vertexBuffer);
    }

    public Sprite createStairs() {
        return new Sprite(0, 0, textureFactory.getTextureRegionFor(GameSceneAssets.STAIRS), vertexBuffer);
    }

    public Sprite createTowerMiddle() {
        return new Sprite(0, 0, textureFactory.getTextureRegionFor(GameSceneAssets.MIDDLE), vertexBuffer);
    }

    public AnimatedSprite createBat() {
        TiledTextureRegion textureRegion = textureFactory.getTiledTextureRegionFor(GameSceneAssets.BAT);
        AnimatedSprite sprite = new AnimatedSprite(0, 0, textureRegion, vertexBuffer);
        long[] durations = { 100, 100, 100, 100 };
        sprite.animate(durations, 0, 3, true);
        sprite.setRotation(30f);
        return sprite;
    }

    public AnimatedSprite createPlayer() {
        TiledTextureRegion textureRegion = textureFactory.getTiledTextureRegionFor(GameSceneAssets.PLAYER);
        AnimatedSprite sprite = new AnimatedSprite(0, 0, textureRegion, vertexBuffer);
        long[] durations = { 100, 100, 100, 100 };
        sprite.animate(durations, true);
        return sprite;
    }

}

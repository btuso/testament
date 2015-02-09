package com.btuso.testament.factory.gamescene;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.btuso.testament.GameContext;

public class GameSceneSpriteFactory {

    private final static FixtureDef MOB_FIXTURE_DEF = PhysicsFactory.createFixtureDef(20f, 0f, 0.5f);

    private final PhysicsWorld physicsWorld;
    private final VertexBufferObjectManager vertexBuffer;
    private final GameSceneTextureFactory textureFactory;

    public GameSceneSpriteFactory(GameContext context, PhysicsWorld physicsWorld) {
        this.physicsWorld = physicsWorld;
        this.vertexBuffer = context.getVertexBuffer();
        this.textureFactory = new GameSceneTextureFactory(context);
    }

    public Entity createTower(GameSceneSpriteFactory factory, float posX, float posY) {
        Sprite towerBottom = factory.createStairsLowerPart();
        Sprite lowerStairsLowerPart = factory.createStairsLowerPart();
        Sprite lowerStairs = factory.createStairs();
        Sprite towerMiddle = factory.createTowerMiddle();
        Sprite upperStairsLowerPart = factory.createStairsLowerPart();
        Sprite upperStairs = factory.createStairs();

        towerBottom.setY(towerBottom.getHeight() * 0.25f);
        lowerStairsLowerPart.setY(calculateNextY(towerBottom, lowerStairsLowerPart));
        lowerStairs.setY(calculateNextY(lowerStairsLowerPart, lowerStairs));
        towerMiddle.setY(calculateNextY(lowerStairs, towerMiddle));
        upperStairsLowerPart.setY(calculateNextY(towerMiddle, upperStairsLowerPart));
        upperStairs.setY(calculateNextY(upperStairsLowerPart, upperStairs));

        Entity tower = new Entity(posX, posY);
        tower.attachChild(towerBottom);
        tower.attachChild(lowerStairsLowerPart);
        tower.attachChild(lowerStairs);
        tower.attachChild(towerMiddle);
        tower.attachChild(upperStairsLowerPart);
        tower.attachChild(upperStairs);
        return tower;
    }

    private Sprite createStairsLowerPart() {
        return new Sprite(0, 0, textureFactory.getTextureRegionFor(GameSceneAssets.LOWER_HALF), vertexBuffer);
    }

    private Sprite createStairs() {
        return new Sprite(0, 0, textureFactory.getTextureRegionFor(GameSceneAssets.STAIRS), vertexBuffer);
    }

    private Sprite createTowerMiddle() {
        return new Sprite(0, 0, textureFactory.getTextureRegionFor(GameSceneAssets.MIDDLE), vertexBuffer);
    }

    private float calculateNextY(Entity entityBelow, Entity currentEntity) {
        return entityBelow.getY() + entityBelow.getHeight() * 0.5f + currentEntity.getHeight() * 0.5f;
    }

    public AnimatedSprite createBat() {
        AnimatedSprite sprite = new AnimatedSprite(0, 0, textureFactory.getTiledTextureRegionFor(GameSceneAssets.BAT),
                vertexBuffer);
        long[] durations = { 100, 100, 100, 100 };
        sprite.animate(durations, 0, 3, true);
        sprite.setRotation(30f);
        return sprite;
    }

    public Body createMobPhysicBody(Entity mob) {
        return PhysicsFactory.createBoxBody(physicsWorld, mob, BodyType.KinematicBody, MOB_FIXTURE_DEF);
    }

}

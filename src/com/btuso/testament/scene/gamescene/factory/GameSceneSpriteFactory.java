package com.btuso.testament.scene.gamescene.factory;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.GameContext;
import com.btuso.testament.scene.gamescene.GameSceneAssets;
import com.btuso.testament.scene.gamescene.components.Despawner;
import com.btuso.testament.scene.gamescene.components.Teleporter;

public class GameSceneSpriteFactory {

    public static final int UPPER_STAIRS = 1;
    public static final int LOWER_STAIRS = 2;

    private static final float SENSOR_WIDTH = 10f;

    private final VertexBufferObjectManager vertexBuffer;
    private final GameSceneTextureFactory textureFactory;
    private final GameScenePhysicsFactory physicsFactory;

    // TODO Create an object factory, which contains all factories and
    // orchestrates construction
    public GameSceneSpriteFactory(GameContext context, PhysicsWorld physicsWorld) {
        this.vertexBuffer = context.getVertexBuffer();
        this.textureFactory = new GameSceneTextureFactory(context);
        this.physicsFactory = new GameScenePhysicsFactory(physicsWorld);
    }

    public Entity createTower() {
        Sprite towerBottom = createStairsLowerPart();
        Sprite lowerStairsLowerPart = createStairsLowerPart();
        Sprite lowerStairs = createStairs();
        Sprite towerMiddle = createTowerMiddle();
        Sprite upperStairsLowerPart = createStairsLowerPart();
        Sprite upperStairs = createStairs();

        towerBottom.setY(towerBottom.getHeight() * 0.25f);
        lowerStairsLowerPart.setY(calculateNextY(towerBottom, lowerStairsLowerPart));
        lowerStairs.setY(calculateNextY(lowerStairsLowerPart, lowerStairs));
        towerMiddle.setY(calculateNextY(lowerStairs, towerMiddle));
        upperStairsLowerPart.setY(calculateNextY(towerMiddle, upperStairsLowerPart));
        upperStairs.setY(calculateNextY(upperStairsLowerPart, upperStairs));

        upperStairs.setTag(UPPER_STAIRS);
        lowerStairs.setTag(LOWER_STAIRS);

        Entity tower = new Entity();
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

    public AnimatedSprite createBat(float teleportX, float teleportY) {
        AnimatedSprite sprite = new AnimatedSprite(0, 0, textureFactory.getTiledTextureRegionFor(GameSceneAssets.BAT),
                vertexBuffer);
        long[] durations = { 100, 100, 100, 100 };
        sprite.animate(durations, 0, 3, true);
        sprite.setRotation(30f);
        Body body = physicsFactory.createBatPhysicBody(sprite);
        sprite.setUserData(body);
        sprite.registerUpdateHandler(new Teleporter(body, teleportX, teleportY));
        sprite.registerUpdateHandler(new Despawner(body));
        return sprite;
    }

    public Entity createSensor(float height) {
        Entity teleportSensor = new Entity(0, 0, SENSOR_WIDTH, height);
        Body sensor = physicsFactory.createZoneSensor(teleportSensor);
        teleportSensor.setUserData(sensor);
        return teleportSensor;
    }
}

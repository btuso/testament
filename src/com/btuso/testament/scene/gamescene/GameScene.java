package com.btuso.testament.scene.gamescene;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.GameContext;
import com.btuso.testament.scene.gamescene.components.MobSpawner;
import com.btuso.testament.scene.gamescene.components.SensorContactHandler;
import com.btuso.testament.scene.gamescene.factory.GameSceneSpriteFactory;
import com.btuso.testament.scene.gamescene.sensors.DespawnSensor;

public class GameScene extends Scene {

    private static final float SPAWN_OFFSET = 30f;
    private static final float MOB_SPAWN_INTERVAL = 3f;

    private final GameContext context;
    private FixedStepPhysicsWorld physicsWorld;
    private MobSpawner mobSpawner;
    private SensorContactHandler contactHandler;
    private IEntity upperStairs;
    private IEntity lowerStairs;

    public GameScene(GameContext context) {
        this.context = context;

        this.setBackground(new Background(Color.BLUE));
        this.setIgnoreUpdate(true);
        this.setChildrenIgnoreUpdate(true);
        loadResources();
        this.registerUpdateHandler(physicsWorld);
        this.registerUpdateHandler(mobSpawner);
        this.setChildrenIgnoreUpdate(false);
        this.setIgnoreUpdate(false);
    }

    private void loadResources() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0f, 0f), false, 8, 3);
        contactHandler = new SensorContactHandler();
        physicsWorld.setContactListener(contactHandler);
        GameSceneSpriteFactory factory = new GameSceneSpriteFactory(context, physicsWorld);
        createTower(factory);
        addStairSensorsToScene(factory);
        createMobSpawner(factory);

        DebugRenderer debug = new DebugRenderer(physicsWorld, context.getVertexBuffer());
        this.attachChild(debug);
    }

    private void createTower(GameSceneSpriteFactory factory) {
        final float towerX = context.getCamera().getWidth() * 0.5f;
        Entity tower = factory.createTower();
        tower.setPosition(towerX, 0f);
        lowerStairs = tower.getChildByTag(GameSceneSpriteFactory.LOWER_STAIRS);
        upperStairs = tower.getChildByTag(GameSceneSpriteFactory.UPPER_STAIRS);
        this.attachChild(tower);
    }

    private void addStairSensorsToScene(GameSceneSpriteFactory factory) {
        Body teleport = attachStairSensorToScene(upperStairs, factory);
        contactHandler.registerSensor(teleport, new TeleportSensor());
        Body despawn = attachStairSensorToScene(lowerStairs, factory);
        contactHandler.registerSensor(despawn, new DespawnSensor());
    }

    private Body attachStairSensorToScene(IEntity stairs, GameSceneSpriteFactory factory) {
        Entity sensorEntity = factory.createSensor(stairs.getHeight());
        Body sensorBody = (Body) sensorEntity.getUserData();
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(sensorEntity, sensorBody));
        float[] sceneCoords = stairs.convertLocalCoordinatesToSceneCoordinates(0f, stairs.getHeight() * 0.5f);
        sensorBody.setTransform(toPhysUnit(sceneCoords[0]), toPhysUnit(sceneCoords[1]), sensorBody.getAngle());
        return sensorBody;
    }

    private float toPhysUnit(float pixUnit) {
        return pixUnit * (1 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
    }

    private void createMobSpawner(GameSceneSpriteFactory factory) {
        Entity mobs = new Entity();
        this.attachChild(mobs);
        float[] spawnCoords = calculateSpawnCoordinates(upperStairs);
        float[] teleportCoords = calculateSpawnCoordinates(lowerStairs);
        mobSpawner = new MobSpawner(spawnCoords, teleportCoords, MOB_SPAWN_INTERVAL, mobs, physicsWorld, factory);
    }

    private float[] calculateSpawnCoordinates(IEntity stairs) {
        float spawnX = stairs.getWidth() + SPAWN_OFFSET;
        float spawnY = stairs.getHeight() + SPAWN_OFFSET;
        float[] spawnCoords = stairs.convertLocalCoordinatesToSceneCoordinates(spawnX, spawnY);
        return toPhysUnit(spawnCoords);
    }

    private float[] toPhysUnit(float[] pixUnits) {
        float[] physUnits = new float[pixUnits.length];
        for (int i = 0; i < physUnits.length; i++) {
            physUnits[i] = toPhysUnit(pixUnits[i]);
        }
        return physUnits;
    }

}

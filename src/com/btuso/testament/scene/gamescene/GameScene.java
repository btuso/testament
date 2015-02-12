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
import com.btuso.testament.mediator.EntityDataMediator;
import com.btuso.testament.scene.gamescene.components.MobSpawn;
import com.btuso.testament.scene.gamescene.components.SensorContactHandler;
import com.btuso.testament.scene.gamescene.factory.GameSceneEntityFactory;
import com.btuso.testament.scene.gamescene.sensors.DespawnSensor;
import com.btuso.testament.scene.gamescene.sensors.HitSensor;
import com.btuso.testament.scene.gamescene.sensors.TeleportSensor;

public class GameScene extends Scene {

    private static final float MOB_SPAWN_OFFSET = 30f;
    private static final float MOB_SPAWN_INTERVAL = 4.5f;
    private static final float PLAYER_X_OFFSET = 35f;
    private static final float PLAYER_Y_OFFSET = 0f;

    private final GameContext context;
    private FixedStepPhysicsWorld physicsWorld;
    private GameSceneEntityFactory entityFactory;
    private MobSpawn mobSpawner;
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
        createPhysics();
        entityFactory = new GameSceneEntityFactory(context, physicsWorld);
        createTower();
        addStairSensorsToScene();
        createMobSpawner();
        createPlayer();
        DebugRenderer debug = new DebugRenderer(physicsWorld, context.getVertexBuffer());
        this.attachChild(debug);
    }

    private void createPhysics() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0f, 0f), false, 8, 3);
        contactHandler = new SensorContactHandler();
        physicsWorld.setContactListener(contactHandler);
    }

    private void createTower() {
        final float towerX = context.getCamera().getWidth() * 0.5f;
        Entity tower = entityFactory.createTower();
        tower.setPosition(towerX, 0f);
        lowerStairs = tower.getChildByTag(GameSceneEntityFactory.LOWER_STAIRS);
        upperStairs = tower.getChildByTag(GameSceneEntityFactory.UPPER_STAIRS);
        this.attachChild(tower);
    }

    private void addStairSensorsToScene() {
        Body teleport = attachStairSensorToScene(upperStairs);
        contactHandler.registerSensor(teleport, new TeleportSensor());
        Body despawn = attachStairSensorToScene(lowerStairs);
        contactHandler.registerSensor(despawn, new DespawnSensor());
    }

    private Body attachStairSensorToScene(IEntity stairs) {
        PhysicsConnector connector = entityFactory.createSensor(stairs.getHeight());
        Body sensorBody = connector.getBody();
        physicsWorld.registerPhysicsConnector(connector);
        float[] sceneCoords = stairs.convertLocalCoordinatesToSceneCoordinates(0f, stairs.getHeight() * 0.5f);
        sensorBody.setTransform(toPhysUnit(sceneCoords[0]), toPhysUnit(sceneCoords[1]), sensorBody.getAngle());
        return sensorBody;
    }

    private float toPhysUnit(float pixUnit) {
        return pixUnit * (1 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
    }

    private void createMobSpawner() {
        Entity mobs = new Entity();
        this.attachChild(mobs);
        float[] spawnCoords = calculateSpawnCoordinates(upperStairs);
        float[] teleportCoords = calculateSpawnCoordinates(lowerStairs);
        mobSpawner = new MobSpawn(spawnCoords, teleportCoords, MOB_SPAWN_INTERVAL, mobs, physicsWorld, entityFactory);
    }

    private float[] calculateSpawnCoordinates(IEntity stairs) {
        float spawnX = stairs.getWidth() + MOB_SPAWN_OFFSET;
        float spawnY = stairs.getHeight() + MOB_SPAWN_OFFSET;
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

    private void createPlayer() {
        PhysicsConnector connector = entityFactory.createPlayer();
        Body body = connector.getBody();
        IEntity player = connector.getEntity();
        this.attachChild(player);
        physicsWorld.registerPhysicsConnector(connector);
        movePlayerToStartingPosition(body);
        contactHandler.registerSensor(body, new HitSensor((EntityDataMediator) player.getUserData()));
    }

    private void movePlayerToStartingPosition(Body body) {
        float x = lowerStairs.getWidth() * 0.5f - PLAYER_X_OFFSET;
        float y = lowerStairs.getHeight() * 0.5f - PLAYER_Y_OFFSET;
        float[] playerCoords = lowerStairs.convertLocalCoordinatesToSceneCoordinates(x, y);
        body.setTransform(toPhysUnit(playerCoords[0]), toPhysUnit(playerCoords[1]), body.getAngle());
    }
}

package com.btuso.testament.scene.gamescene;

import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.btuso.testament.GameContext;
import com.btuso.testament.mediator.DataMediator;
import com.btuso.testament.mediator.EntityDataMediator;
import com.btuso.testament.scene.gamescene.components.MobSpawn;
import com.btuso.testament.scene.gamescene.components.SensorContactHandler;
import com.btuso.testament.scene.gamescene.factory.GameSceneEntityFactory;
import com.btuso.testament.scene.gamescene.sensors.AnimationSensor;
import com.btuso.testament.scene.gamescene.sensors.DespawnSensor;
import com.btuso.testament.scene.gamescene.sensors.HitSensor;
import com.btuso.testament.scene.gamescene.sensors.OnGroundSensor;
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
    private IEntity player;

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
        createGroundForPlayer();
        this.setOnSceneTouchListener(new TouchInputController((DataMediator) player.getUserData()));
        this.attachChild(new DebugRenderer(physicsWorld, context.getVertexBuffer()));
    }

    private void createGroundForPlayer() {
        Body sensor = entityFactory.createGround(player);
        contactHandler.registerSensor(sensor, new OnGroundSensor());
    }

    private void createPhysics() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0f, -SensorManager.GRAVITY_EARTH), false, 8, 3);
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
        float[] sceneCoords = stairs.convertLocalCoordinatesToSceneCoordinates(0f, stairs.getHeight() * 0.5f);
        PhysicsConnector connector = entityFactory.createSensor(stairs.getHeight(), sceneCoords);
        physicsWorld.registerPhysicsConnector(connector);
        Body sensorBody = connector.getBody();
        Body sensorPin = entityFactory.createBodyPin(sensorBody);
        weldBodiesAt(sensorPin, sensorBody, sensorPin.getWorldCenter());
        return sensorBody;
    }

    private void weldBodiesAt(Body first, Body second, Vector2 position) {
        WeldJointDef jointDef = new WeldJointDef();
        jointDef.initialize(first, second, position);
        physicsWorld.createJoint(jointDef);
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
            physUnits[i] = pixUnits[i] / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        }
        return physUnits;
    }

    private void createPlayer() {
        PhysicsConnector connector = entityFactory.createPlayer(calculatePlayerPosition());
        player = connector.getEntity();
        this.attachChild(player);
        Body playerBody = connector.getBody();
        physicsWorld.registerPhysicsConnector(connector);
        // TODO REFACTOR
        ArrayList<Fixture> fixtures = playerBody.getFixtureList();
        contactHandler.registerSensor(fixtures.get(0), new HitSensor((EntityDataMediator) player.getUserData()));
        contactHandler.registerSensor(fixtures.get(1), new AnimationSensor());
    }

    private float[] calculatePlayerPosition() {
        float x = lowerStairs.getWidth() * 0.5f - PLAYER_X_OFFSET;
        float y = lowerStairs.getHeight() * 0.5f - PLAYER_Y_OFFSET;
        float[] playerCoords = lowerStairs.convertLocalCoordinatesToSceneCoordinates(x, y);
        return playerCoords;
    }

}

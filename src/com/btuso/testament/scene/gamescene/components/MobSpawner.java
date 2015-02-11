package com.btuso.testament.scene.gamescene.components;

import java.util.Arrays;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.scene.gamescene.factory.GameSceneSpriteFactory;

public class MobSpawner implements IUpdateHandler {

    private final float[] spawn;
    private final float[] teleport;
    private final float interval;
    private final Entity target;
    private final GameSceneSpriteFactory factory;
    private final FixedStepPhysicsWorld physicsWorld;

    private float timePassed = 0f;

    public MobSpawner(float[] spawn, float[] teleport, float interval, Entity target,
            FixedStepPhysicsWorld physicsWorld, GameSceneSpriteFactory factory) {
        this.spawn = Arrays.copyOf(spawn, 2);
        this.teleport = Arrays.copyOf(teleport, 2);
        this.interval = interval;
        this.factory = factory;
        this.target = target;
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        timePassed += pSecondsElapsed;
        if (timePassed >= interval) {
            timePassed = 0f;
            // TODO implement mob pool -> so lazy~
            spawnMob();
        }
    }

    private void spawnMob() {
        // Create
        AnimatedSprite mob = factory.createBat(teleport[0], teleport[1]);
        Body body = (Body) mob.getUserData();
        // Attach
        target.attachChild(mob);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(mob, body));
        // Adjust
        mob.setPosition(-100f, -100f); // To avoid flicker in (0, 0) position
        body.setTransform(spawn[0], spawn[1], body.getAngle());
    }

    @Override
    public void reset() {
        // TODO recycle all mobs
    }

}

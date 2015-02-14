package com.btuso.testament.scene.gamescene.components;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.util.adt.pool.GenericPool;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.Logger;
import com.btuso.testament.scene.gamescene.factory.GameSceneEntityFactory;

public class MobSpawn implements IUpdateHandler {
    /*
     * TODO Figure out a way to clean up the
     * "physics world <-> mob <-> mob spawn <->pool" mess
     */
    private final float[] spawn;
    private final float[] teleport;
    private final float interval;
    private final GenericPool<PhysicsConnector> pool;

    private float timePassed = 0f;

    public MobSpawn(float[] spawn, float[] teleport, float interval, Entity target, FixedStepPhysicsWorld physicsWorld,
            GameSceneEntityFactory factory) {
        this.spawn = spawn;
        this.teleport = teleport;
        this.interval = interval;
        pool = createPool(factory, physicsWorld, target);
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        timePassed += pSecondsElapsed;
        if (timePassed >= interval) {
            timePassed = 0f;
            spawnMob();
        }
    }

    private void spawnMob() {
        pool.obtainPoolItem();
    }

    public void removeMob(PhysicsConnector connector) {
        pool.recyclePoolItem(connector);
    }

    @Override
    public void reset() {
        // TODO recycle all mobs
    }

    private GenericPool<PhysicsConnector> createPool(final GameSceneEntityFactory factory,
            final FixedStepPhysicsWorld physicsWorld, final Entity target) {
        // TODO preallocation leaves bodies hanging around, not cool
        return new GenericPool<PhysicsConnector>() {

            @Override
            protected PhysicsConnector onAllocatePoolItem() {
                Logger.log("Allocating bat in pool");
                PhysicsConnector connector = factory.createBat(teleport[0], teleport[1], MobSpawn.this);
                IEntity mob = connector.getEntity();
                target.attachChild(mob);
                physicsWorld.registerPhysicsConnector(connector);
                setActive(mob, connector.getBody(), false);
                return connector;
            }

            @Override
            protected void onHandleObtainItem(PhysicsConnector connector) {
                IEntity mob = connector.getEntity();
                Body body = connector.getBody();
                // TODO check how to avoid flicker in (0, 0) without this
                mob.setPosition(-100f, -100f);
                body.setTransform(spawn[0], spawn[1], body.getAngle());
                setActive(mob, body, true);
            }

            @Override
            protected void onHandleRecycleItem(PhysicsConnector connector) {
                IEntity mob = connector.getEntity();
                Body body = connector.getBody();
                // TODO can't reset handlers without reseting base entity
                // properties. One workaround would be adding a RESET message
                // and letting handlers reset themselves if they need to do so
                setActive(mob, body, false);
            }

            private void setActive(IEntity mob, Body body, boolean active) {
                mob.setVisible(active);
                mob.setIgnoreUpdate(!active);
                body.setActive(active);
            }
        };
    }
}

package com.btuso.testament.scene.gamescene.components;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.scene.gamescene.MobFlags;

public class Teleport implements IUpdateHandler {

    private static final float CAST_TIME = 3f;
    private final Body body;
    private final float x;
    private final float y;
    private float castingTime = 0;
    private boolean shouldTeleport = false;

    public Teleport(Body body, float x, float y) {
        this.body = body;
        this.x = x;
        this.y = y;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (body.getUserData() == MobFlags.TELEPORT) {
            body.setUserData(MobFlags.NONE);
            body.setActive(false);
            shouldTeleport = true;
        }
        if (shouldTeleport) {
            castingTime += pSecondsElapsed;
            if (castingTime >= CAST_TIME) {
                shouldTeleport = false;
                body.setTransform(x, y, body.getAngle());
                body.setActive(true);
            }
        }
    }

    @Override
    public void reset() {
        castingTime = 0;
        shouldTeleport = false;
    }

}

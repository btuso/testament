package com.btuso.testament.scene.gamescene.components;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.scene.gamescene.MobFlags;

public class Despawner implements IUpdateHandler {

    private final Body body;

    public Despawner(Body body) {
        this.body = body;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (body.getUserData() == MobFlags.DESPAWN) {
            body.setUserData(MobFlags.NONE);
            body.applyAngularImpulse(200f);
        }
    }

    @Override
    public void reset() {
    }

}

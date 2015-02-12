package com.btuso.testament.scene.gamescene.components;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.physics.box2d.Body;

public class Movement implements IUpdateHandler {

    public Movement(Body body) {
        // TODO make body move by applying force
        body.setLinearVelocity(-1f, -0.15f);
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        // Applying a force based on tpf results in a chaos, so figure it out
        // later. Also check if it's worth it, probably linear velocity also
        // depends on tpf.
    }

    @Override
    public void reset() {
    }

}

package com.btuso.testament.scene.gamescene.components;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;
import com.btuso.testament.mediator.QueuedDataListener;

public class Teleport implements IUpdateHandler {

    private static final float CAST_TIME = 3f;

    private final QueuedDataListener dataQueue = new QueuedDataListener();
    private final Body body;
    private final float x;
    private final float y;

    private float castingTime = 0;
    private boolean shouldTeleport = false;

    public Teleport(Body body, DataMediator mediator, float x, float y) {
        this.body = body;
        mediator.registerListener(dataQueue);
        this.x = x;
        this.y = y;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (dataQueue.contains(Data.TELEPORT)) {
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
        dataQueue.clearQueue();
    }

    @Override
    public void reset() {
        castingTime = 0;
        shouldTeleport = false;
        dataQueue.clearQueue();
    }

}

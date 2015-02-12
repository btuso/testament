package com.btuso.testament.scene.gamescene.components;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;
import com.btuso.testament.mediator.QueuedDataListener;

public class Despawn implements IUpdateHandler {
    // TODO think about making a queued data component, which automatically
    // registers the queue and also clears it after update and clear it on reset
    // TODO Do this ASAP, seriously
    private final QueuedDataListener dataQueue = new QueuedDataListener();
    private final Body body;

    public Despawn(Body body, DataMediator mediator) {
        this.body = body;
        mediator.registerListener(dataQueue);
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (dataQueue.contains(Data.DESPAWN)) {
            body.applyAngularImpulse(40f); // TODO proper mob recycling
        }
        dataQueue.clearQueue();
    }

    @Override
    public void reset() {
        dataQueue.clearQueue();
    }

}

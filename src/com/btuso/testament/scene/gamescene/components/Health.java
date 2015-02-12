package com.btuso.testament.scene.gamescene.components;

import org.andengine.engine.handler.IUpdateHandler;

import android.util.Log;

import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.EntityDataMediator;
import com.btuso.testament.mediator.QueuedDataListener;

public class Health implements IUpdateHandler {

    private final QueuedDataListener dataQueue = new QueuedDataListener();

    public Health(EntityDataMediator entityData, int healthPoints) {
        entityData.registerListener(dataQueue);
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (dataQueue.contains(Data.HIT)) {
            Log.d("Mediator", "Hit");
        }
        dataQueue.clearQueue();
    }

    @Override
    public void reset() {
        dataQueue.clearQueue();
    }

}

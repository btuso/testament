package com.btuso.testament.component;

import org.andengine.engine.handler.IUpdateHandler;

import com.btuso.testament.mediator.DataMediator;
import com.btuso.testament.mediator.QueuedDataListener;

public abstract class QueuedDataComponent implements IUpdateHandler {

    protected final QueuedDataListener dataQueue = new QueuedDataListener();

    protected abstract void update(float pSecondsElapsed);

    public QueuedDataComponent(DataMediator dataMediator) {
        dataMediator.registerListener(dataQueue);
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        update(pSecondsElapsed);
        dataQueue.clearQueue();
    }

    @Override
    public void reset() {
        dataQueue.clearQueue();
    }
}

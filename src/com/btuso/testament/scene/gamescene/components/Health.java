package com.btuso.testament.scene.gamescene.components;

import com.btuso.testament.Logger;
import com.btuso.testament.component.QueuedDataComponent;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class Health extends QueuedDataComponent {

    public Health(DataMediator mediator, int healthPoints) {
        super(mediator);
    }

    @Override
    public void update(float pSecondsElapsed) {
        if (dataQueue.contains(Data.HIT)) {
            Logger.log("Hit");
        }
    }

}

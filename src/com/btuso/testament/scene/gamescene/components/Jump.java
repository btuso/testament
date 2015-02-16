package com.btuso.testament.scene.gamescene.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.component.QueuedDataComponent;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class Jump extends QueuedDataComponent {

    private final Body body;

    public Jump(Body body, DataMediator dataMediator) {
        super(dataMediator);
        this.body = body;
    }

    @Override
    protected void update(float pSecondsElapsed) {
        if (dataQueue.contains(Data.TAP)) {
            // TODO take into account ground
            body.applyLinearImpulse(new Vector2(0, 50f), body.getWorldCenter());
        }
    }

}

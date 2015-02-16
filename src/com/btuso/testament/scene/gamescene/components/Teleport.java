package com.btuso.testament.scene.gamescene.components;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.component.QueuedDataComponent;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class Teleport extends QueuedDataComponent {

    private static final float CAST_TIME = 3f;

    private final Body body;
    private final float x;
    private final float y;

    private float castingTime = 0;
    private boolean shouldTeleport = false;

    public Teleport(Body body, DataMediator mediator, float x, float y) {
        super(mediator);
        this.body = body;
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(float pSecondsElapsed) {
        if (dataQueue.contains(Data.TELEPORT)) {
            body.setActive(false);
            shouldTeleport = true;
        }
        if (shouldTeleport) {
            castingTime += pSecondsElapsed;
            if (castingTime >= CAST_TIME) {
                shouldTeleport = false;
                castingTime = 0;
                body.setTransform(x, y, body.getAngle());
                body.setActive(true);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        castingTime = 0;
        shouldTeleport = false;
    }

}

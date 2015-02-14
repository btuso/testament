package com.btuso.testament.scene.gamescene.components;

import org.andengine.extension.physics.box2d.PhysicsConnector;

import com.btuso.testament.component.QueuedDataComponent;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class Recycle extends QueuedDataComponent {
    private final PhysicsConnector connector;
    private final MobSpawn spawn;

    public Recycle(PhysicsConnector connector, DataMediator mediator, MobSpawn spawn) {
        super(mediator);
        this.connector = connector;
        this.spawn = spawn;
    }

    @Override
    public void update(float pSecondsElapsed) {
        if (dataQueue.contains(Data.DEATH)) {
            spawn.removeMob(connector);
        }
    }
}

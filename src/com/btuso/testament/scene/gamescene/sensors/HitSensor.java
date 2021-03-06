package com.btuso.testament.scene.gamescene.sensors;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class HitSensor implements CollitionSensor {

    private final DataMediator entityData;

    public HitSensor(DataMediator entityData) {
        this.entityData = entityData;
    }

    @Override
    public void onBeginContact(Body collider) {
        entityData.broadcast(Data.HIT);
    }

    @Override
    public void onEndContact(Body collider) {
    }

}

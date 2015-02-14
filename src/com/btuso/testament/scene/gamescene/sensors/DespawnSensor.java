package com.btuso.testament.scene.gamescene.sensors;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class DespawnSensor implements CollitionSensor {

    @Override
    public void onBeginContact(Body collider) {
    }

    @Override
    public void onEndContact(Body collider) {
        DataMediator mediator = (DataMediator) collider.getUserData();
        mediator.broadcast(Data.DEATH);
    }

}

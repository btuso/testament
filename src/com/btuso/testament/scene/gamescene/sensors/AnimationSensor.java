package com.btuso.testament.scene.gamescene.sensors;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class AnimationSensor implements CollitionSensor {

    @Override
    public void onBeginContact(Body collider) {
        DataMediator mediator = (DataMediator) collider.getUserData();
        mediator.broadcast(Data.ATTACK_ANIMATION);
    }

    @Override
    public void onEndContact(Body collider) {
    }

}

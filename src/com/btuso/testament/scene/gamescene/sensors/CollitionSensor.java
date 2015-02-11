package com.btuso.testament.scene.gamescene.sensors;

import com.badlogic.gdx.physics.box2d.Body;

public interface CollitionSensor {

    void onBeginContact(Body collider);

    void onEndContact(Body collider);

}

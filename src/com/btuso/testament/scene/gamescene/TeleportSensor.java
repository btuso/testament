package com.btuso.testament.scene.gamescene;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.scene.gamescene.sensors.CollitionSensor;

public class TeleportSensor implements CollitionSensor {

    @Override
    public void onBeginContact(Body collider) {
    }

    @Override
    public void onEndContact(Body mob) {
        mob.setUserData(MobFlags.TELEPORT);
    }

}

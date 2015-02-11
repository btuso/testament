package com.btuso.testament.scene.gamescene.sensors;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.scene.gamescene.MobFlags;

public class DespawnSensor implements CollitionSensor {

    @Override
    public void onBeginContact(Body collider) {
    }

    @Override
    public void onEndContact(Body collider) {
        collider.setUserData(MobFlags.DESPAWN);
    }

}

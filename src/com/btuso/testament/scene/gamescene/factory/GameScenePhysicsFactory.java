package com.btuso.testament.scene.gamescene.factory;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameScenePhysicsFactory {

    private static final FixtureDef MOB_FIXTURE_DEF = PhysicsFactory.createFixtureDef(20f, 0f, 10f);
    private final PhysicsWorld physicsWorld;

    public GameScenePhysicsFactory(PhysicsWorld physicsWorld) {
        this.physicsWorld = physicsWorld;
    }

    public Body createBatPhysicBody(Sprite batSprite) {
        Body body = createMobPhysicBody(batSprite);
        body.setLinearVelocity(-2f, -0.25f);
        return body;
    }

    private Body createMobPhysicBody(Entity mob) {
        return PhysicsFactory.createBoxBody(physicsWorld, mob, BodyType.DynamicBody, MOB_FIXTURE_DEF);
    }

    public Body createZoneSensor(IEntity zoneEntity) {
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        return PhysicsFactory.createBoxBody(physicsWorld, zoneEntity, BodyType.StaticBody, fixtureDef);
    }

}

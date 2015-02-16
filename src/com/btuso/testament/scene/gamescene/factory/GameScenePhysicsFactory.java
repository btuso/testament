package com.btuso.testament.scene.gamescene.factory;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class GameScenePhysicsFactory {

    private static final float PIXEL_TO_METER = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
    private static final short CATEGORY_GROUND = 2;
    private static final short MASKBITS_GROUND = CATEGORY_GROUND;
    private static final short NONE = 0;

    private static final FixtureDef MOB_FIXTURE_DEF = PhysicsFactory.createFixtureDef(20f, 0f, 10f);
    private static final FixtureDef SENSOR_FIXTURE_DEF = PhysicsFactory.createFixtureDef(2f, 0f, 1f, true);
    private static final FixtureDef GROUND_FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 0f, 5f, false,
            CATEGORY_GROUND, MASKBITS_GROUND, NONE);
    private final PhysicsWorld physicsWorld;

    public GameScenePhysicsFactory(PhysicsWorld physicsWorld) {
        this.physicsWorld = physicsWorld;
    }

    public Body createBatPhysicBody(Sprite batSprite) {
        return createCircularMobPhysicBody(batSprite);
    }

    private Body createCircularMobPhysicBody(Entity mob) {
        return PhysicsFactory.createCircleBody(physicsWorld, mob, BodyType.KinematicBody, MOB_FIXTURE_DEF);
    }

    public Body createSensor(IEntity entity) {
        return PhysicsFactory.createBoxBody(physicsWorld, entity, BodyType.DynamicBody, SENSOR_FIXTURE_DEF);
    }

    public Body createPin(Body body) {
        final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(2f, 0f, 1f);
        fixtureDef.isSensor = true;
        Vector2 center = body.getWorldCenter().tmp().mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        return PhysicsFactory.createBoxBody(physicsWorld, center.x, center.y, 5f, 5f, BodyType.StaticBody, fixtureDef);
    }

    public Body createGroundBody(Entity entity) {
        return PhysicsFactory.createBoxBody(physicsWorld, entity, BodyType.StaticBody, GROUND_FIXTURE_DEF);
    }

    public Body createPlayer(Entity entity) {
        Body body = createPlayerBody(entity);
        addSensorFixtureToBody(body, entity.getWidth() * 1.5f, entity.getHeight());
        addFixtureToBody(body, GROUND_FIXTURE_DEF, entity.getWidth(), entity.getHeight());
        return body;
    }

    private Body createPlayerBody(IEntity player) {
        final BodyDef boxBodyDef = createBodyDef(player.getSceneCenterCoordinates());
        final Body boxBody = physicsWorld.createBody(boxBodyDef);
        addSensorFixtureToBody(boxBody, player.getWidth(), player.getHeight());
        boxBody.setTransform(boxBody.getWorldCenter(), MathUtils.degToRad(0));
        return boxBody;
    }

    private BodyDef createBodyDef(float[] coords) {
        final BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = BodyType.DynamicBody;
        boxBodyDef.position.x = coords[0] / PIXEL_TO_METER;
        boxBodyDef.position.y = coords[1] / PIXEL_TO_METER;
        return boxBodyDef;
    }

    private void addSensorFixtureToBody(Body owner, float width, float height) {
        this.addFixtureToBody(owner, SENSOR_FIXTURE_DEF, width, height);
    }

    private void addFixtureToBody(Body owner, FixtureDef fixture, float width, float height) {
        final PolygonShape boxPoly = new PolygonShape();
        boxPoly.setAsBox(width * 0.5f / PIXEL_TO_METER, height * 0.5f / PIXEL_TO_METER);
        fixture.shape = boxPoly;
        owner.createFixture(fixture);
        boxPoly.dispose();
    }
}

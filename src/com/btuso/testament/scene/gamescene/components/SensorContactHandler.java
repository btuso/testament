package com.btuso.testament.scene.gamescene.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.btuso.testament.scene.gamescene.sensors.CollitionSensor;

public class SensorContactHandler implements ContactListener {

    private Map<Fixture, CollitionSensor> registeredSensors = new HashMap<Fixture, CollitionSensor>();

    public void registerSensor(Body body, CollitionSensor sensor) {
        ArrayList<Fixture> fixtures = body.getFixtureList();
        registeredSensors.put(fixtures.get(0), sensor);
    }

    public void registerSensor(Fixture fixture, CollitionSensor sensor) {
        registeredSensors.put(fixture, sensor);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    @Override
    public void endContact(Contact contact) {
        CollitionSensor sensor = getSensor(contact.getFixtureA(), contact.getFixtureB());
        Body mob = getMob(contact.getFixtureA(), contact.getFixtureB());
        sensor.onEndContact(mob);
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.isTouching()) {
            CollitionSensor sensor = getSensor(contact.getFixtureA(), contact.getFixtureB());
            Body mob = getMob(contact.getFixtureA(), contact.getFixtureB());
            sensor.onBeginContact(mob);
        }
    }

    private CollitionSensor getSensor(Fixture first, Fixture second) {
        CollitionSensor sensor = registeredSensors.get(first);
        if (sensor == null) {
            sensor = registeredSensors.get(second);
        }
        // Mobs collided, weird. TODO check & review this
        return sensor != null ? sensor : emptyCollitionSensor;
    }

    private Body getMob(Fixture first, Fixture second) {
        return registeredSensors.get(first) == null ? first.getBody() : second.getBody();
    }

    private final CollitionSensor emptyCollitionSensor = new CollitionSensor() {

        @Override
        public void onEndContact(Body collider) {
        }

        @Override
        public void onBeginContact(Body collider) {
        }

    };

}

package com.btuso.testament.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.GameContext;
import com.btuso.testament.factory.gamescene.GameSceneSpriteFactory;

public class GameScene extends Scene {

    private final GameContext context;
    private FixedStepPhysicsWorld physicsWorld;

    public GameScene(GameContext context) {
        this.context = context;

        this.setBackground(new Background(Color.BLUE));
        this.setChildrenIgnoreUpdate(true);
        loadResources();
        setChildrenIgnoreUpdate(false);
        registerUpdateHandler(physicsWorld);
    }

    private void loadResources() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0f, 0f), false, 8, 3);

        GameSceneSpriteFactory factory = new GameSceneSpriteFactory(context, physicsWorld);
        createBackground(factory);

        final AnimatedSprite bat = factory.createBat();
        bat.setPosition(240f, 310f);
        this.attachChild(bat);

        Body batBody = factory.createMobPhysicBody(bat);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(bat, batBody));
        batBody.setLinearVelocity(-2f, -1f);

        DebugRenderer debug = new DebugRenderer(physicsWorld, context.getVertexBuffer());
        this.attachChild(debug);
    }

    private void createBackground(GameSceneSpriteFactory factory) {
        Entity backgroundLayer = new Entity();
        final float towerX = context.getCamera().getWidth() * 0.5f;
        Entity tower = factory.createTower(factory, towerX, 0f);
        backgroundLayer.attachChild(tower);
        this.attachChild(backgroundLayer);
    }

}

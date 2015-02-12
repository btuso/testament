package com.btuso.testament.scene.gamescene.factory;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimationData;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.btuso.testament.GameContext;
import com.btuso.testament.mediator.DataMediator;
import com.btuso.testament.mediator.EntityDataMediator;
import com.btuso.testament.scene.gamescene.components.AttackAnimation;
import com.btuso.testament.scene.gamescene.components.Despawn;
import com.btuso.testament.scene.gamescene.components.Health;
import com.btuso.testament.scene.gamescene.components.Movement;
import com.btuso.testament.scene.gamescene.components.Teleport;

public class GameSceneEntityFactory {

    public static final int UPPER_STAIRS = 1;
    public static final int LOWER_STAIRS = 2;

    private static final float SENSOR_WIDTH = 10f;

    private final GameSceneSpriteFactory sprites;
    private final GameScenePhysicsFactory physics;

    public GameSceneEntityFactory(GameContext context, PhysicsWorld physicsWorld) {
        GameSceneTextureFactory textures = new GameSceneTextureFactory(context);
        sprites = new GameSceneSpriteFactory(context, textures);
        physics = new GameScenePhysicsFactory(physicsWorld);
    }

    public Entity createTower() {
        Sprite towerBottom = sprites.createStairsLowerPart();
        Sprite lowerStairsLowerPart = sprites.createStairsLowerPart();
        Sprite lowerStairs = sprites.createStairs();
        Sprite towerMiddle = sprites.createTowerMiddle();
        Sprite upperStairsLowerPart = sprites.createStairsLowerPart();
        Sprite upperStairs = sprites.createStairs();

        towerBottom.setY(towerBottom.getHeight() * 0.25f);
        lowerStairsLowerPart.setY(calculateNextY(towerBottom, lowerStairsLowerPart));
        lowerStairs.setY(calculateNextY(lowerStairsLowerPart, lowerStairs));
        towerMiddle.setY(calculateNextY(lowerStairs, towerMiddle));
        upperStairsLowerPart.setY(calculateNextY(towerMiddle, upperStairsLowerPart));
        upperStairs.setY(calculateNextY(upperStairsLowerPart, upperStairs));

        upperStairs.setTag(UPPER_STAIRS);
        lowerStairs.setTag(LOWER_STAIRS);

        Entity tower = new Entity();
        tower.attachChild(towerBottom);
        tower.attachChild(lowerStairsLowerPart);
        tower.attachChild(lowerStairs);
        tower.attachChild(towerMiddle);
        tower.attachChild(upperStairsLowerPart);
        tower.attachChild(upperStairs);
        return tower;
    }

    private float calculateNextY(Entity entityBelow, Entity currentEntity) {
        return entityBelow.getY() + entityBelow.getHeight() * 0.5f + currentEntity.getHeight() * 0.5f;
    }

    public PhysicsConnector createSensor(float height) {
        Entity sensorEntity = new Entity(0, 0, SENSOR_WIDTH, height);
        Body sensorBody = physics.createSensor(sensorEntity);
        return new PhysicsConnector(sensorEntity, sensorBody);
    }

    public PhysicsConnector createBat(float teleportX, float teleportY) {
        AnimatedSprite sprite = sprites.createBat();
        Body body = physics.createBatPhysicBody(sprite);
        DataMediator mediator = createAndSetMediatorTo(body);
        sprite.registerUpdateHandler(new Movement(body));
        sprite.registerUpdateHandler(new Teleport(body, mediator, teleportX, teleportY));
        sprite.registerUpdateHandler(new Despawn(body, mediator));
        AnimationData normal = sprites.createBatAnimation();
        AnimationData attack = sprites.createBatAttackAnimation();
        sprite.registerUpdateHandler(new AttackAnimation(sprite, mediator, normal, attack));
        return new PhysicsConnector(sprite, body);
    }

    public DataMediator createAndSetMediatorTo(Body body) {
        EntityDataMediator dataMediator = new EntityDataMediator();
        body.setUserData(dataMediator);
        return dataMediator;
    }

    public PhysicsConnector createPlayer() {
        AnimatedSprite player = sprites.createPlayer();
        Body body = physics.createSensor(player);
        EntityDataMediator dataMediator = new EntityDataMediator();
        player.setUserData(dataMediator);
        player.registerUpdateHandler(new Health(dataMediator, 5));
        return new PhysicsConnector(player, body);
    }
}

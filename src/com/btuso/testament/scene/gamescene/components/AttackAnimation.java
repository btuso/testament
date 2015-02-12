package com.btuso.testament.scene.gamescene.components;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimationData;

import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;
import com.btuso.testament.mediator.QueuedDataListener;

public class AttackAnimation implements IUpdateHandler {

    private final QueuedDataListener dataQueue = new QueuedDataListener();
    private final AnimatedSprite owner;
    private final AnimationData normal;
    private final AnimationData attack;

    private boolean animating = false;

    public AttackAnimation(AnimatedSprite owner, DataMediator mediator, AnimationData normal, AnimationData attack) {
        this.owner = owner;
        this.normal = normal;
        this.attack = attack;
        mediator.registerListener(dataQueue);
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (dataQueue.contains(Data.ATTACK_ANIMATION)) {
            owner.animate(attack);
            animating = true;
        }
        if (animating) {
            if (!owner.isAnimationRunning()) {
                owner.animate(normal);
                animating = false;
            }
        }
        dataQueue.clearQueue();
    }

    @Override
    public void reset() {
        dataQueue.clearQueue();
    }
}

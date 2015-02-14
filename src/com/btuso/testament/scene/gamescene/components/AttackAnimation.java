package com.btuso.testament.scene.gamescene.components;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimationData;

import com.btuso.testament.component.QueuedDataComponent;
import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class AttackAnimation extends QueuedDataComponent {

    private final AnimatedSprite owner;
    private final AnimationData normal;
    private final AnimationData attack;

    private boolean animating = false;

    public AttackAnimation(AnimatedSprite owner, DataMediator mediator, AnimationData normal, AnimationData attack) {
        super(mediator);
        this.owner = owner;
        this.normal = normal;
        this.attack = attack;
    }

    @Override
    public void update(float pSecondsElapsed) {
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
    }

}

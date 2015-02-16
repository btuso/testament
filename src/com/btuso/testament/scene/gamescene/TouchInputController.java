package com.btuso.testament.scene.gamescene;

import org.andengine.input.touch.detector.ClickDetector;

import com.btuso.testament.mediator.Data;
import com.btuso.testament.mediator.DataMediator;

public class TouchInputController extends ClickDetector {

    private final DataMediator mediator;

    public TouchInputController(final DataMediator mediator) {
        super(createDetector(mediator));
        this.mediator = mediator;
        // TODO everything
    }

    private static IClickDetectorListener createDetector(final DataMediator mediator) {
        return new IClickDetectorListener() {

            @Override
            public void onClick(ClickDetector pClickDetector, int pPointerID, float pSceneX, float pSceneY) {
                mediator.broadcast(Data.TAP);
            }
        };
    }
}

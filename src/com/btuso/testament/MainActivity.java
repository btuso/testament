package com.btuso.testament;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.btuso.testament.scene.GameScene;

public class MainActivity extends SimpleBaseGameActivity {

    private static final int CAMERA_WIDTH = 480;
    private static final int CAMERA_HEIGHT = 720;
    private GameContext context;

    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                new FillResolutionPolicy(), camera);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }

    @Override
    protected void onCreateResources() throws IOException {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    }

    @Override
    protected Scene onCreateScene() {
        return new GameScene(context);
    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        LimitedFPSEngine engine = new LimitedFPSEngine(pEngineOptions, 60);
        context = new GameContext(this, engine);
        return engine;
    }
}
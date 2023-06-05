package com.game;

import org.eteriaEngine.components.GameObject;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.camera.CameraManager;
import org.eteriaEngine.scenes.Scene;
import com.game.components.GameCamera;
import com.game.world.WorldManager;
import com.game.world.WorldMap;
import com.game.world.WorldMapRenderer;

public class TestScene extends Scene {

    @Override
    public void onEnter() {
        System.out.println("Entered test scene!");

        //---- CAMERA INITIALIZATION ----
        GameObject cameraObject = new GameObject("Camera");
        Camera gameCamera = cameraObject.addComponent(GameCamera.class);
        CameraManager.setCurrentCamera(gameCamera);
        addToScene(cameraObject);

        //---- WORLD GENERATION ----
        GameObject world = new GameObject("World");
        WorldMap map = world.addComponent(WorldMap.class);
        //Setting map values
        if(map != null){
            map.setChunkTileSize(32);
            map.setTileSize(1);
            map.setTileScale(1);
        }
        //Adds world manager and generates world
        WorldManager worldManager = world.addComponent(WorldManager.class);
        if(worldManager != null){
            worldManager.generateWorld(16, 16);
        }
        //Adds renderer to render the world.
        world.addComponent(WorldMapRenderer.class);
        addToScene(world);
    }
    @Override
    public void onExit() {

    }
}
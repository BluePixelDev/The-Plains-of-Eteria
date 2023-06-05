package org.eteriaEngine.scenes;

import org.eteriaEngine.assets.AssetDatabase;

public class SceneManager {
    private static volatile SceneManager instance;
    private static final Object mutex = new Object();

    private Scene currentScene;

    private SceneManager(){
    }

    public static SceneManager instance(){
        SceneManager result = instance;

        if(instance == null){
            synchronized (mutex){
                result = instance;

                if(result == null){
                    instance = result = new SceneManager();
                }
            }
        }
        return result;
    }

    public void update() {
        if(currentScene != null){
            currentScene.update();
        }
    }

    /**
     * Loads a new Scene and invokes events in both of them.
     */
    public void loadScene(Scene scene){
        if(currentScene != null){
            currentScene.exit();
            AssetDatabase.release();
        }
        currentScene = scene;
        currentScene.enter();
    }
}

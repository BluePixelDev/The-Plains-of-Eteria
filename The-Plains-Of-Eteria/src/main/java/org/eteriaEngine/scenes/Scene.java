package org.eteriaEngine.scenes;

import org.eteriaEngine.components.GameObject;

import java.util.ArrayList;

public abstract class Scene {
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    void enter(){
        onEnter();
        for (GameObject gameObject: gameObjects) {
            if(gameObject != null){
                gameObject.start();
            }
        }
    }
    void exit(){
        for (GameObject gameObject : gameObjects) {
            gameObject.onDisable();
        }
        onExit();
    }
    void update(){
        for (GameObject gameObject : gameObjects) {
            if(gameObject != null){
                gameObject.update();
            }
        }
    }

    protected void addToScene(GameObject gameObject){
        gameObjects.add(gameObject);
    }
    protected void removeFromScene(GameObject gameObject){
        gameObjects.remove(gameObject);
    }

    public abstract void onEnter();
    public abstract void onExit();
}

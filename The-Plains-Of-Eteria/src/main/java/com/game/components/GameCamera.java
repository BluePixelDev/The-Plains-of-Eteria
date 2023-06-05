package com.game.components;
import org.eteriaEngine.core.Input;
import org.eteriaEngine.core.Time;
import org.eteriaEngine.components.GameObject;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.core.EngineUtility;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameCamera extends Camera {

    private float acc = 0.1f;
    private float dec = 0.08f;
    private float speed = 0.5f;
    private Vector3f targetPosition = new Vector3f();

    public GameCamera(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void start(){
        super.start();
        targetPosition.z = 10f;
    }
    @Override
    public void update() {
        super.update();

        boolean up = Input.GetKey(GLFW_KEY_W);
        boolean down = Input.GetKey(GLFW_KEY_S);
        boolean left = Input.GetKey(GLFW_KEY_A);
        boolean right = Input.GetKey(GLFW_KEY_D);

        boolean back = Input.GetKey(GLFW_KEY_E);
        boolean forward = Input.GetKey(GLFW_KEY_Q);

        float tX = left && right ? 0 : left ? -1 : right ? 1 : 0;
        float tY = up && down ? 0 : up ? 1 : down ? -1 : 0;
        float tZ = back && forward ? 0 : back ? -1 : forward ? 1 : 0;

        targetPosition.add(new Vector3f(tX, tY , tZ).mul(speed));
        if(tX == 0 && tY == 0){
            transform().setPosition(EngineUtility.lerp(transform().getPosition(), targetPosition,  Time.deltaTime() / acc));
        }
        else {
            transform().setPosition(EngineUtility.lerp(transform().getPosition(), targetPosition,  Time.deltaTime() / dec));
        }

    }
}

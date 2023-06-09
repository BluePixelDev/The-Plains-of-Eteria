package com.game;
import org.eteriaEngine.Mathf;
import org.eteriaEngine.core.Input;
import org.eteriaEngine.core.Time;
import org.eteriaEngine.components.GameObject;
import org.eteriaEngine.rendering.camera.Camera;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameCamera extends Camera {

    private float acc = 0.1f;
    private float dec = 0.08f;
    private float speed = 0.5f;
    private float rotation;
    private final Vector3f targetPosition = new Vector3f();

    public GameCamera(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void start(){
        super.start();
        targetPosition.y = 20f;
        setFar(1000f);
    }
    @Override
    public void update() {
        super.update();

        boolean up = Input.getKey(GLFW_KEY_W);
        boolean down = Input.getKey(GLFW_KEY_S);
        boolean left = Input.getKey(GLFW_KEY_A);
        boolean right = Input.getKey(GLFW_KEY_D);

        boolean back = Input.getKey(GLFW_KEY_E);
        boolean forward = Input.getKey(GLFW_KEY_Q);

        float tX = left && right ? 0 : left ? -1 : right ? 1 : 0;
        float tZ = up && down ? 0 : up ? -1 : down ? 1 : 0;
        float tY = back && forward ? 0 : back ? -1 : forward ? 1 : 0;

        rotation -= Input.getMouseDelta().x * Time.deltaTime();
        transform().setLocalRotation(new Quaternionf().rotationY(rotation));

        Vector3f pos = new Vector3f();
        pos.add(transform().right().mul(tX));
        pos.add(transform().up().mul(tY));
        pos.add(transform().forward().mul(tZ));

        targetPosition.add(pos.mul(speed));
        if(tX == 0 && tZ == 0){
            transform().setLocalPosition(Mathf.lerp(transform().getLocalPosition(), targetPosition,  Time.deltaTime() / acc));
        }
        else {
            transform().setLocalPosition(Mathf.lerp(transform().getLocalPosition(), targetPosition,  Time.deltaTime() / dec));
        }

    }
}

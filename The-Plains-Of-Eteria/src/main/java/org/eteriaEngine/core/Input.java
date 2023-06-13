package org.eteriaEngine.core;

import org.eteriaEngine.Mathf;
import org.eteriaEngine.interfaces.IUpdate;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.DoubleBuffer;
import java.util.HashMap;
import static org.lwjgl.glfw.GLFW.*;

public final class Input {
    private static final HashMap<Integer, Boolean> keyLog = new HashMap<>();
    private static final Vector2f mousePosition = new Vector2f();
    private static final Vector2f prevMousePosition = new Vector2f();
    private static final Vector2f mouseDelta = new Vector2f();

    static void initialize(){
        Window engineWindow = EteriaEngine.getWindow();
        //glfwSetInputMode(engineWindow.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(engineWindow.getWindow(), (window, key, scancode, action, mods) -> {
            if(action == GLFW_PRESS){
                handleKey(key, true);
            }
            if(action == GLFW_RELEASE){
                handleKey(key, false);
            }
        });
        glfwSetCursorPosCallback(engineWindow.getWindow(), ((window, xPos, yPos) -> {


            mousePosition.x = (float) xPos;
            mousePosition.y = (float) yPos;
        }));
    }

    static void update(){
        mouseDelta.x =  mousePosition.x - prevMousePosition.x;
        mouseDelta.y =  mousePosition.y - prevMousePosition.y;

        prevMousePosition.x = mousePosition.x;
        prevMousePosition.y = mousePosition.y;
    }

    private static void handleKey(int key, boolean isDown){
       if(keyLog.containsKey(key)){
           keyLog.replace(key, isDown);
       }
       else{
           keyLog.put(key, isDown);
       }
    }

    public static boolean getKey(int key){
        if(keyLog.containsKey(key)){
            return keyLog.get(key);
        }
        return false;
    }

    public static Vector2f getMousePosition(){
        return new Vector2f(mousePosition);
    }
    public static Vector2f getMouseDelta(){
        return new Vector2f(mouseDelta);
    }
}

package org.eteriaEngine.core;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public final class Input {
    private static final HashMap<Integer, Boolean> keyLog = new HashMap<>();
    static void registerInput(int key, int action){
        if(action == GLFW_PRESS){
            handleKey(key, true);
        }
        if(action == GLFW_RELEASE){
            handleKey(key, false);
        }
    }
    private static void handleKey(int key, boolean isDown){
       if(keyLog.containsKey(key)){
           keyLog.replace(key, isDown);
       }
       else{
           keyLog.put(key, isDown);
       }
    }

    public static boolean GetKey(int key){
        if(keyLog.containsKey(key)){
            return keyLog.get(key);
        }
        return false;
    }
}

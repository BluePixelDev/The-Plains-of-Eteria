package org.eteriaEngine.core;

import java.lang.reflect.InvocationTargetException;

public abstract class Engine {

    public Engine(){
    }
    public abstract void start();

    public static void launch(){
        EteriaEngine engine = new EteriaEngine();
        Engine application = getEngine();

        if(application != null){
            engine.initialize(application);
        }
    }
    public static void launch(Class<? extends Engine> engineClass){
        EteriaEngine engine = new EteriaEngine();

        try {
            engine.initialize(engineClass.getConstructor().newInstance());

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    //Returns new instance of class that called launch() method.
    private static Engine getEngine() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        StackTraceElement ste = stElements[stElements.length - 1];

        try {
            Class<?> cl = Class.forName(ste.getClassName());
            if(Engine.class.isAssignableFrom(cl)){
                Object obj = cl.getConstructor().newInstance();

                Engine.class.cast(obj);

                return (Engine) obj;
            }
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

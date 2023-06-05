package org.eteriaEngine.core;

import java.lang.reflect.InvocationTargetException;

public abstract class Game {

    public Game(){
    }
    public abstract void start();

    public static void launch(){
        EteriaEngine engine = new EteriaEngine();
        Game application = getApplication();

        if(application != null){
            engine.initialize(application);
        }
    }
    public static void launch(Class<? extends Game> appClass){
        EteriaEngine engine = new EteriaEngine();

        try {
            engine.initialize(appClass.getConstructor().newInstance());

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    //Returns new instance of class that called launch() method.
    private static Game getApplication() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        StackTraceElement ste = stElements[stElements.length - 1];

        try {
            Class<?> cl = Class.forName(ste.getClassName());
            if(Game.class.isAssignableFrom(cl)){
                Object obj = cl.getConstructor().newInstance();

                Game.class.cast(obj);

                return (Game) obj;
            }
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

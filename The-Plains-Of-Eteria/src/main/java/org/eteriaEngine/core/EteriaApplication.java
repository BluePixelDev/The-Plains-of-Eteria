package org.eteriaEngine.core;

import java.lang.reflect.InvocationTargetException;

public abstract class EteriaApplication {

    public abstract void start();

    public static void launch(){
        EteriaApplication application = getApplication();
        new EteriaEngine(application);
    }
    public static void launch(Class<? extends EteriaApplication> engineClass){
        try {
            new EteriaEngine(engineClass.getConstructor().newInstance());

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    //Returns new instance of class that called launch() method.
    private static EteriaApplication getApplication() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        StackTraceElement ste = stElements[stElements.length - 1];

        try {
            Class<?> cl = Class.forName(ste.getClassName());
            if(EteriaApplication.class.isAssignableFrom(cl)){
                Object obj = cl.getConstructor().newInstance();

                return (EteriaApplication) obj;
            }
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

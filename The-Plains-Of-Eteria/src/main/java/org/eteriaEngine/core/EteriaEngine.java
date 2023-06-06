package org.eteriaEngine.core;

import org.eteriaEngine.rendering.RenderManager;
import org.eteriaEngine.scenes.SceneManager;

public final class EteriaEngine {
    private static final Window window = new Window();

    EteriaEngine(){
    }

    //Initializes the game engine.
    void initialize(Engine application){
        window.createWindow();

        ThreadGroup threadGroup = new ThreadGroup("Engine");
        Thread engineThread = new Thread(threadGroup, () -> {

            window.makeContext();
            application.start();
            window.showWindow();

            while (!window.shouldWindowClose()) {
                long startTime = System.nanoTime();

                Time.update();
                SceneManager.instance().update();
                RenderManager.get().render(window);

                long totalTime = System.nanoTime() - startTime;
                long targetTime = 1000000000 / Application.targetFramerate();

                if(totalTime < targetTime){
                    try {
                        Thread.sleep((targetTime - totalTime) / 1000000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }


            }

            window.closeWindow();
        });
        engineThread.start();
        window.pollEvents();
    }

    static Window getWindow() {
        return window;
    }
}
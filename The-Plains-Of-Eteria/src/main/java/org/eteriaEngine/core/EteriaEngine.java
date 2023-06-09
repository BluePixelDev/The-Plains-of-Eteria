package org.eteriaEngine.core;

import org.eteriaEngine.rendering.RenderManager;
import org.eteriaEngine.scenes.SceneManager;

public class EteriaEngine {
    static final Window window = new Window();
    EteriaEngine(EteriaApplication application){
        ThreadGroup threadGroup = new ThreadGroup("Eteria_Engine");
        Thread engineThread = new Thread(threadGroup, () -> {

            Input.initialize();
            window.makeContext();
            application.start();
            window.showWindow();

            while (!window.shouldWindowClose()) {
                long startTime = System.nanoTime();

                Time.update();
                Input.update();
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
package com.game;
import org.eteriaEngine.core.Application;
import org.eteriaEngine.core.Game;
import org.eteriaEngine.core.Screen;
import org.eteriaEngine.scenes.SceneManager;

public class Main extends Game {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start() {
        Screen.setScreenResolution(18);
        Application.setTargetFramerate(75);
        Application.setWindowTitle("Title!");
        SceneManager.instance().loadScene(new TestScene());
    }
}
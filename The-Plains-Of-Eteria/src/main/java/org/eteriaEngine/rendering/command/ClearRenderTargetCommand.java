package org.eteriaEngine.rendering.command;

import org.eteriaEngine.interfaces.ICommand;

import static org.lwjgl.opengl.GL11.*;

class ClearRenderTargetCommand implements ICommand {

    @Override
    public void execute() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
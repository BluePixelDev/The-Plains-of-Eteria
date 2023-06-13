package org.eteriaEngine.rendering.commandBuffer.commands;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.RTHandle;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class SetRenderTargetCMD implements ICommand {
    private final RTHandle rtHandle;
    public SetRenderTargetCMD(RTHandle rtHandle){
        this.rtHandle = rtHandle;
    }

    @Override
    public void execute() {
        if(rtHandle == null){
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
            glViewport(0, 0, Screen.width(), Screen.height());
            return;
        }
        rtHandle.bind();
    }
}


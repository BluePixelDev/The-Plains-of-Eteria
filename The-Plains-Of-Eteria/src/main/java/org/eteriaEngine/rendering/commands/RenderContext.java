package org.eteriaEngine.rendering.commands;

import org.eteriaEngine.rendering.enums.BlendingMode;
import org.eteriaEngine.rendering.enums.CullingMode;
import org.eteriaEngine.rendering.enums.DepthFunc;

public record RenderContext(CullingMode cullingMode, DepthFunc depthFunc, BlendingMode blendingMode) {

    @Override
    public String toString() {
        return "MaterialRenderContext{" +
                "cullingMode=" + cullingMode +
                ", depthFunc=" + depthFunc +
                ", blendingMode=" + blendingMode +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderContext that = (RenderContext) o;
        return cullingMode == that.cullingMode && depthFunc == that.depthFunc && blendingMode == that.blendingMode;
    }
}

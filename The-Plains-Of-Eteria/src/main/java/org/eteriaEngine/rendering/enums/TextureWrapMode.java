package org.eteriaEngine.rendering.enums;

/**
 * Wrapping mode of a texture. <br/>
 * <i>CLAMP</i> - Clamps the texture to the last pixel at the edge.<br/>
 * <i>REPEAT</i> - Tiles the texture, creating a repeating pattern.<br/>
 * <i>MIRROR</i> - Tiles the texture, creating a repeating pattern by mirroring it at every integer boundary.<br/>
 * <i>MIRRORONCE</i> - Mirrors the texture once, then clamps to edge pixels.
 */
public enum TextureWrapMode {
    CLAMP,
    REPEAT,
    MIRROR,
    MIRRORONCE
}

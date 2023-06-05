package org.eteriaEngine.rendering.enums;

/**
 * Defines filtering mode of a texture. <br />
 * <i>POINT</i>- Texture pixels become blocky up close. <br />
 * <i>BILINEAR</i> - Texture samples are averaged. <br />
 * <i>TRILINEAR</i> - Texture samples are averaged and also blended between mipmap levels.
 */
public enum FilterMode {
    POINT,
    BILINEAR,
    TRILINEAR
}

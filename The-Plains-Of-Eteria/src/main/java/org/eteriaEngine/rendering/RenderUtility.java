package org.eteriaEngine.rendering;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL33.GL_RGB10_A2UI;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL44.GL_MIRROR_CLAMP_TO_EDGE;

public final class RenderUtility {
    public static final int POSITION_SIZE = 3;
    public static final int COLOR_SIZE = 4;
    public static final int UV_SIZE = 2;

    public static final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + UV_SIZE;
    public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    public static final int POSITION_POINTER = 0;
    public static final int COLOR_POINTER = POSITION_SIZE * Float.BYTES;
    public static final int UV_POINTER = (POSITION_SIZE + COLOR_SIZE) * Float.BYTES;

    /**
     * Converts TextureWrapMode enum to int accepted by openGL.
     * @param wrapMode The wrapping mode of a texture.
     */
    public static int GetWrapMode(TextureWrapMode wrapMode){
        int mode = 0;
        switch (wrapMode){
            case CLAMP -> mode = GL_CLAMP;
            case REPEAT -> mode = GL_REPEAT;
            case MIRROR -> mode = GL_MIRRORED_REPEAT;
            case MIRRORONCE -> mode = GL_MIRROR_CLAMP_TO_EDGE;
        }
        return mode;
    }
    /**
     * Converts FilterMode enum to int accepted by openGL.
     * @param filterMode The filtering mode of a texture.
     */
    public static int GetFilterMode(FilterMode filterMode){
        int mode = 0;
        switch (filterMode){
            case POINT -> mode = GL_NEAREST;
            case BILINEAR -> mode = GL_LINEAR;
            case TRILINEAR -> mode = GL_NEAREST_MIPMAP_LINEAR ;
        }
        return mode;
    }

    /**
     * @return Int value of a GraphicsFormat.
     */
    public static int GetGraphicsFormat(GraphicsFormat graphicsFormat){
        int format = 0;
        switch (graphicsFormat){
            case R8 -> format = GL_R8;
            case R8_SNORM -> format = GL_R8_SNORM;
            case R16 -> format = GL_R16;
            case R16_SNORM -> format = GL_R16_SNORM;
            case RG8 -> format = GL_RG8;
            case RG8_SNORM -> format = GL_RG8_SNORM;
            case RG16 -> format = GL_RG16;
            case RG16_SNORM -> format = GL_RG16_SNORM;
            case R3_G3_B2 -> format = GL_R3_G3_B2;
            case RGB4 -> format = GL_RGB4;
            case RGB5 -> format = GL_RGB5;
            case RGB8 -> format = GL_RGB8;
            case RGB8_SNORM -> format = GL_RGB8_SNORM;
            case RGB10 -> format = GL_RGB10;
            case RGB12 -> format = GL_RGB12;
            case RGB16_SNORM -> format = GL_RGB16_SNORM;
            case RGBA2 -> format = GL_RGBA2;
            case RGBA4 -> format = GL_RGBA4;
            case RGB5_A1 -> format = GL_RGB5_A1;
            case RGBA8 -> format = GL_RGBA8;
            case RGBA8_SNORM -> format = GL_RGBA8_SNORM;
            case RGB10_A2 -> format = GL_RGB10_A2;
            case RGB10_A2UI -> format = GL_RGB10_A2UI;
            case RGBA12 -> format = GL_RGBA12;
            case RGBA16 -> format = GL_RGBA16;
            case SRGB8 -> format = GL_SRGB8;
            case SRGB8_ALPHA8 -> format = GL_SRGB8_ALPHA8;
            case R16F -> format = GL_R16F;
            case RG16F -> format = GL_RG16F;
            case RGB16F -> format = GL_RGB16F;
            case RGBA16F -> format = GL_RGBA16F;
            case R32F -> format = GL_R32F;
            case RG32F -> format = GL_RG32F;
            case RGB32F -> format = GL_RGB32F;
            case RGBA32F -> format = GL_RGBA32F;
            case R11F_G11F_B10F -> format = GL_R11F_G11F_B10F;
            case RGB9_E5 -> format = GL_RGB9_E5;
            case R8I -> format = GL_R8I;
            case R8UI -> format = GL_R8UI;
            case R16I -> format = GL_R16I;
            case R16UI -> format = GL_R16UI;
            case R32I -> format = GL_R32I;
            case R32UI -> format = GL_R32UI;
            case RG8I -> format = GL_RG8I;
            case RG8UI -> format = GL_RG8UI;
            case RG16I -> format = GL_RG16I;
            case RG16UI -> format = GL_RG16UI;
            case RG32I -> format = GL_RG32I;
            case RG32UI -> format = GL_RG32UI;
            case RGB8I -> format = GL_RGB8I;
            case RGB8UI -> format = GL_RGB8UI;
            case RGB16I -> format = GL_RGB16I;
            case RGB16UI -> format = GL_RGB16UI;
            case RGB32I -> format = GL_RGB32I;
            case RGB32UI -> format = GL_RGB32UI;
            case RGBA8I -> format = GL_RGBA8I;
            case RGBA8UI -> format = GL_RGBA8UI;
            case RGBA16I -> format = GL_RGBA16I;
            case RGBA16UI -> format = GL_RGBA16UI;
            case RGBA32I -> format = GL_RGBA32I;
            case RGBA32UI -> format = GL_RGBA32UI;

            case COMPRESSED_RG -> format = GL_COMPRESSED_RG;
            case COMPRESSED_RGB -> format = GL_COMPRESSED_RGB;
            case COMPRESSED_RGBA -> format = GL_COMPRESSED_RGBA;
            case COMPRESSED_SRGB -> format = GL_COMPRESSED_SRGB;
            case COMPRESSED_SRGB_ALPHA -> format = GL_COMPRESSED_SRGB_ALPHA;
            case COMPRESSED_RED_RGTC1 -> format = GL_COMPRESSED_RED_RGTC1;
            case COMPRESSED_SIGNED_RED_RGTC1 -> format = GL_COMPRESSED_SIGNED_RED_RGTC1;
            case COMPRESSED_RG_RGTC2 -> format = GL_COMPRESSED_RG_RGTC2;
            case COMPRESSED_SIGNED_RG_RGTC2 -> format = GL_COMPRESSED_SIGNED_RG_RGTC2;
            case COMPRESSED_RGBA_BPTC_UNORM -> format = GL_COMPRESSED_RGBA_BPTC_UNORM;
            case COMPRESSED_SRGB_ALPHA_BPTC_UNORM -> format = GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM;
            case COMPRESSED_RGB_BPTC_SIGNED_FLOAT -> format = GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT;
            case COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT -> format = GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT;
        }
        return format;
    }
    /**
     * @return Internal format of a GraphicFormat in integer.
     */
    public static int GetInternalFormat(GraphicsFormat graphicsFormat){
        int format = 0;
        switch (graphicsFormat){
            case R8,
                    R8_SNORM,
                    R16,
                    R16_SNORM,
                    R16F,
                    R32F,
                    R8I,
                    R32I,
                    R16UI,
                    R8UI,
                    R16I,
                    R32UI,
                    COMPRESSED_RED,
                    COMPRESSED_SIGNED_RED_RGTC1,
                    COMPRESSED_RED_RGTC1 -> format = GL_RED;

            case RG8,
                    RG8_SNORM,
                    RG16,
                    RG16_SNORM,
                    RG16F,
                    RG32F,
                    RG8I,
                    RG8UI,
                    COMPRESSED_RG,
                    RGB8I,
                    RG32UI,
                    RG32I,
                    RG16UI,
                    RG16I,
                    COMPRESSED_SIGNED_RG_RGTC2,
                    COMPRESSED_RG_RGTC2 -> format = GL_RG;

            case R3_G3_B2,
                    RGBA4,
                    RGBA2,
                    RGB16_SNORM,
                    RGB12,
                    RGB10,
                    RGB8_SNORM,
                    RGB8,
                    RGB5,
                    RGB4,
                    SRGB8,
                    RGB8UI,
                    RGB16I,
                    RGB16UI,
                    RGB32I,
                    RGB32UI,
                    RGBA8I,
                    RGB16F,
                    RGB32F,
                    R11F_G11F_B10F,
                    RGB9_E5,
                    COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT,
                    COMPRESSED_RGB_BPTC_SIGNED_FLOAT,
                    COMPRESSED_SRGB,
                    COMPRESSED_RGB -> format = GL_RGB;

            case RGB5_A1,
                    RGBA8,
                    RGBA8_SNORM,
                    RGB10_A2,
                    RGB10_A2UI,
                    RGBA12,
                    RGBA16,
                    SRGB8_ALPHA8,
                    RGBA16F,
                    RGBA32F,
                    COMPRESSED_RGBA,
                    RGBA32UI,
                    RGBA32I,
                    RGBA16UI,
                    RGBA16I,
                    RGBA8UI,
                    COMPRESSED_SRGB_ALPHA_BPTC_UNORM,
                    COMPRESSED_RGBA_BPTC_UNORM,
                    COMPRESSED_SRGB_ALPHA -> format = GL_RGBA;
        }
        return format;
    }
}

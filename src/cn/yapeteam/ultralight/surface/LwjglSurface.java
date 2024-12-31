package cn.yapeteam.ultralight.surface;

import cn.yapeteam.ultralight.logger.Logger;
import lombok.Getter;
import net.janrupf.ujr.api.math.IntRect;
import net.janrupf.ujr.api.surface.UltralightSurface;
import net.janrupf.ujr.api.util.UltralightBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL42;

import java.nio.ByteBuffer;

/**
 * This is a custom implementation of the UltralightSurface interface.
 * <p>
 * It uses a texture to store the pixel data and a ByteBuffer to store the
 * pixel data in system memory.
 */
public class LwjglSurface implements UltralightSurface {
    @Getter
    private int texture;
    private ByteBuffer pixelBuffer;

    private long width;
    private long height;

    private IntRect dirtyBounds;

    public LwjglSurface(long width, long height) {
        resize(width, height);
        Logger.warn("Created surface with texture {}", texture);
    }

    @Override
    public long width() {
        return width;
    }

    @Override
    public long height() {
        return height;
    }

    @Override
    public long rowBytes() {
        return width * 4;
    }

    @Override
    public long size() {
        return width * height * 4;
    }

    @Override
    public UltralightBuffer lockPixels() {
        return new SurfaceBuffer(pixelBuffer, this);
    }

    @Override
    public void resize(long width, long height) {
        this.width = width;
        this.height = height;

        if (this.texture != 0) {
            // We need to delete the old texture as we set the size using
            // immutable storage.
            GL11.glDeleteTextures(this.texture);
        }

        this.texture = GL11.glGenTextures();

        // Configure the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, GL11.GL_RGBA8, (int) width, (int) height);

        Logger.warn("Resized surface with texture {} to {}x{}", texture, width, height);

        // Make sure to have a fitting pixel buffer
        this.pixelBuffer = ByteBuffer.allocateDirect((int) (width * height * 4));
    }

    @Override
    public void setDirtyBounds(IntRect bounds) {
        this.dirtyBounds = bounds;
    }

    @Override
    public IntRect dirtyBounds() {
        if (dirtyBounds != null) {
            return dirtyBounds;
        } else {
            // We don't want to return null into the native code
            return new IntRect(0, 0, 0, 0);
        }
    }

    @Override
    public void clearDirtyBounds() {
        this.dirtyBounds = null;
    }

    public void destroy() {
        GL11.glDeleteTextures(this.texture);

        Logger.warn("Destroyed surface with texture {}", texture);
    }
}

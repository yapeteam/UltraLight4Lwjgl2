package cn.yapeteam.ultralight.surface;

import net.janrupf.ujr.api.surface.UltralightSurface;
import net.janrupf.ujr.api.surface.UltralightSurfaceFactory;

/**
 * This replaces the default bitmap surface factory so that Ultralight renders into GL textures.
 */
public class LwjglSurfaceFactory implements UltralightSurfaceFactory {
    @Override
    public UltralightSurface createSurface(long width, long height) {
        return new LwjglSurface(width, height);
    }

    @Override
    public void destroySurface(UltralightSurface surface) {
        ((LwjglSurface) surface).destroy();
    }
}

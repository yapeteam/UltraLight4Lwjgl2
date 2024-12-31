package cn.yapeteam.ultralight.surface;

import cn.yapeteam.ultralight.logger.Logger;
import net.janrupf.ujr.api.math.IntRect;
import net.janrupf.ujr.api.util.NioUltralightBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

/**
 * LWJGL2 实现，用于将 GlfwSurface 的内容渲染到纹理。
 */
public class SurfaceBuffer extends NioUltralightBuffer {
    private final ByteBuffer pixelBuffer;
    private final LwjglSurface surface;

    public SurfaceBuffer(ByteBuffer pixelBuffer, LwjglSurface surface) {
        super(pixelBuffer);
        this.pixelBuffer = pixelBuffer;
        this.surface = surface;
    }

    @Override
    public void close() {
        IntRect dirtyBounds = surface.dirtyBounds();

        // 检查脏区域的合法性
        if (dirtyBounds.width() <= 0 || dirtyBounds.height() <= 0) {
            Logger.warn("Dirty bounds are empty or invalid, skipping texture update.");
            return;
        }

        // 绑定纹理
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, surface.getTexture());

        // 如果脏区域宽度等于纹理宽度，可以直接上传整个矩形区域
        if (dirtyBounds.width() == surface.width()) {
            GL11.glTexSubImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    dirtyBounds.getLeft(),
                    dirtyBounds.getTop(),
                    dirtyBounds.width(),
                    dirtyBounds.height(),
                    GL12.GL_BGRA, // 使用 BGRA 格式
                    GL11.GL_UNSIGNED_BYTE,
                    getBufferPosition(dirtyBounds.getTop(), dirtyBounds.getLeft())
            );
        } else {
            // 如果宽度不一致，需要逐行上传
            for (int y = dirtyBounds.getTop(); y < dirtyBounds.getBottom(); y++) {
                GL11.glTexSubImage2D(
                        GL11.GL_TEXTURE_2D,
                        0,
                        dirtyBounds.getLeft(),
                        y,
                        dirtyBounds.width(),
                        1,
                        GL12.GL_BGRA, // 使用 BGRA 格式
                        GL11.GL_UNSIGNED_BYTE,
                        getBufferPosition(y, dirtyBounds.getLeft())
                );
            }
        }

        // 生成 Mipmap
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // 清除脏区域
        surface.clearDirtyBounds();

        super.close();
    }

    /**
     * 计算 ByteBuffer 中的偏移位置。
     *
     * @param row 行号
     * @param col 列号
     * @return 偏移后的 ByteBuffer
     */
    private ByteBuffer getBufferPosition(int row, int col) {
        // 计算偏移量：rowBytes * row + col * 4
        int offset = (int) (surface.rowBytes() * row + col * 4);
        ByteBuffer buffer = pixelBuffer.duplicate(); // 创建一个副本，避免修改原始缓冲区的位置
        buffer.position(offset);
        return buffer;
    }
}

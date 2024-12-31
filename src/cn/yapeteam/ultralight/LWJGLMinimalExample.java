package cn.yapeteam.ultralight;

import cn.yapeteam.ultralight.bridge.FilesystemBridge;
import cn.yapeteam.ultralight.bridge.LoggerBridge;
import cn.yapeteam.ultralight.logger.Logger;
import cn.yapeteam.ultralight.surface.LwjglSurface;
import cn.yapeteam.ultralight.surface.LwjglSurfaceFactory;
import net.janrupf.ujr.api.*;
import net.janrupf.ujr.api.event.*;
import net.janrupf.ujr.api.listener.UltralightLoadListener;
import net.janrupf.ujr.core.UltralightJavaReborn;
import net.janrupf.ujr.core.platform.PlatformEnvironment;
import net.janrupf.ujr.core.platform.UnsupportedPlatformEnvironmentException;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.util.EnumSet;

public class LWJGLMinimalExample {
    private static final double SCROLL_SENSITIVITY = 50.0; // 滚动灵敏度

    private UltralightRenderer renderer;
    private UltralightView view;

    public static void main(String[] args) {
        Logger.init(); // 初始化日志
        new LWJGLMinimalExample().run();
    }

    public void run() {
        try {
            init();
            loop();
        } finally {
            cleanup();
        }
    }

    private void init() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            Mouse.create();
            Keyboard.create();
            Logger.info("Display, Mouse, and Keyboard initialized.");
        } catch (LWJGLException e) {
            Logger.exception(e);
            System.exit(1);
        }

        initOpenGL();

        PlatformEnvironment environment;
        try {
            environment = PlatformEnvironment.load();
            Logger.info("Platform environment loaded successfully.");
        } catch (UnsupportedPlatformEnvironmentException e) {
            Logger.exception(e);
            System.exit(1);
            return;
        }

        try (UltralightJavaReborn ujr = new UltralightJavaReborn(environment)) {
            ujr.activate();
            Logger.info("UltralightJavaReborn activated.");

            UltralightPlatform platform = UltralightPlatform.instance();
            platform.usePlatformFontLoader();
            platform.setFilesystem(new FilesystemBridge());
            platform.setLogger(new LoggerBridge());
            platform.setSurfaceFactory(new LwjglSurfaceFactory());

            platform.setConfig(new UltralightConfigBuilder()
                    .cachePath(System.getProperty("java.io.tmpdir") + File.separator + "ujr-yolbi")
                    .resourcePathPrefix(FilesystemBridge.RESOURCE_PREFIX)
                    .build());

            Logger.info("Ultralight platform configured.");

            renderer = UltralightRenderer.getOrCreate();

            view = renderer.createView(800, 600, new UltralightViewConfigBuilder()
                    .transparent(false)
                    .enableImages(true)
                    .initialDeviceScale(1)
                    .build());
            Logger.info("Ultralight renderer and view initialized.");

            // 添加加载监听器
            view.setLoadListener(new WebViewLoadListener());

            // 加载网页
            view.loadURL("https://github.com/yapeteam");
            Logger.info("WebView initialized and URL loaded: " + view.url());
        } catch (Exception e) {
            Logger.exception(e);
        }
    }

    private void initOpenGL() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, -1, 1); // 设置左上角为原点
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(0, 0, 0, 1); // 黑色背景
        Logger.info("OpenGL initialized.");
    }

    private void loop() {
        while (!Display.isCloseRequested()) {
            try {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                // 更新和渲染 Ultralight
                renderer.update();
                renderer.render();

                // 获取 MinecraftSurface
                LwjglSurface surface = (LwjglSurface) view.surface();
                if (surface == null || surface.width() <= 0 || surface.height() <= 0) {
                    Logger.warn("Invalid surface, skipping rendering.");
                    Display.update();
                    continue;
                }

                // 渲染纹理
                int textureID = surface.getTexture();
                if (GL11.glIsTexture(textureID)) {
                    renderTexture(textureID, (int) surface.width(), (int) surface.height());
                } else {
                    Logger.warn("Invalid texture ID: {}", textureID);
                }

                // 处理输入事件
                handleInput();

                // 更新显示
                Display.update();
                Display.sync(60); // 限制帧率为 60 FPS
            } catch (Exception e) {
                Logger.exception(e);
            }
        }
    }

    private void renderTexture(int textureID, int width, int height) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        // 设置纹理过滤模式
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        // 绘制纹理
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, 0); // 左上角

        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(width, 0); // 右上角

        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(width, height); // 右下角

        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, height); // 左下角
        GL11.glEnd();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private void handleInput() {
        // 处理鼠标事件
        handleMouseInput();

        // 处理键盘输入事件
        handleKeyboardInput();
    }

    private void handleKeyboardInput() {
        while (Keyboard.next()) {
            boolean keyState = Keyboard.getEventKeyState(); // 按键状态：按下或释放
            int lwjglKeyCode = Keyboard.getEventKey();      // LWJGL 的虚拟键码
            char keyChar = Keyboard.getEventCharacter();    // 按键字符

            // 获取修饰键状态
            EnumSet<UlKeyEventModifiers> modifiers = KeyboardTranslator.getModifiers();

            // 将 LWJGL 键码转换为 Ultralight 键码
            int ulKeyCode = KeyboardTranslator.lwjglKeyToUltralight(lwjglKeyCode);

            // 如果键码无法识别，忽略事件
            if (ulKeyCode == UlKeyCode.UNKNOWN) {
                Logger.warn("Unknown key code: {}", lwjglKeyCode);
                continue;
            }

            // 创建 UlKeyEvent 对象
            UlKeyEvent keyEvent = new UlKeyEvent();
            keyEvent.type = keyState ? UlKeyEventType.DOWN : UlKeyEventType.UP; // 设置事件类型
            keyEvent.virtualKeyCode = ulKeyCode; // 设置 Ultralight 的虚拟键码
            keyEvent.nativeKeyCode = lwjglKeyCode; // 设置 LWJGL 的原生键码
            keyEvent.keyIdentifier = UlKeyEvent.keyIdentifierFromVirtualKeyCode(ulKeyCode); // 获取按键标识符
            keyEvent.text = String.valueOf(keyChar); // 按键字符作为文本
            keyEvent.unmodifiedText = String.valueOf(keyChar); // 假设未修饰文本与按键字符相同
            keyEvent.modifiers = modifiers; // 设置修饰键状态

            // 打印调试信息
            Logger.info("Key event: type={}, ulKeyCode={}, lwjglKeyCode={}, char={}, modifiers={}",
                    keyEvent.type, ulKeyCode, lwjglKeyCode, keyChar, modifiers);

            // 将事件传递给 Ultralight
            view.fireKeyEvent(keyEvent);
            if (keyState) {
                keyEvent.type = UlKeyEventType.CHAR;
                view.fireKeyEvent(keyEvent);
            }
        }
    }

    private void handleMouseInput() {
        while (Mouse.next()) {
            int x = Mouse.getEventX(); // 获取鼠标的 X 坐标
            int y = Display.getHeight() - Mouse.getEventY(); // 翻转 Y 坐标
            boolean buttonState = Mouse.getEventButtonState(); // 检查鼠标按键状态
            int button = Mouse.getEventButton(); // 获取触发事件的鼠标按键
            int dWheel = Mouse.getDWheel(); // 获取滚轮的变化量
            if (dWheel != 0) {
                UlScrollEvent scrollEvent = new UlScrollEvent();

                // 判断滚动类型
                scrollEvent.type = UlScrollEventType.BY_PIXEL; // 鼠标滚轮通常是像素级滚动
                scrollEvent.deltaX = 0; // 假设水平滚动量为 0
                scrollEvent.deltaY = (int) (dWheel > 0 ? SCROLL_SENSITIVITY : -SCROLL_SENSITIVITY); // 调整滚动灵敏度

                view.fireScrollEvent(scrollEvent);
            } else {
                // 创建 UlMouseEvent 对象
                UlMouseEvent mouseEvent = new UlMouseEvent();
                mouseEvent.x = x;
                mouseEvent.y = y;

                // 判断事件类型
                if (button != -1) { // 如果有鼠标按键触发
                    mouseEvent.type = buttonState ? UlMouseEventType.DOWN : UlMouseEventType.UP; // 按下或释放
                    switch (button) {
                        case 0:
                            mouseEvent.button = UlMouseButton.LEFT;
                            break;
                        case 1:
                            mouseEvent.button = UlMouseButton.RIGHT;
                            break;
                        case 2:
                            mouseEvent.button = UlMouseButton.MIDDLE;
                    }
                } else {
                    mouseEvent.type = UlMouseEventType.MOVED; // 鼠标移动事件
                }
                // 将事件传递给 Ultralight
                view.fireMouseEvent(mouseEvent);
            }
        }
    }

    private void cleanup() {
        try {
            Mouse.destroy();
            Keyboard.destroy();
            Display.destroy();
            Logger.info("Resources cleaned up.");
        } catch (Exception e) {
            Logger.exception(e);
        }
    }

    /**
     * 自定义网页加载监听器，实现 UltralightLoadListener 接口
     */
    private static class WebViewLoadListener implements UltralightLoadListener {
        @Override
        public void onBeginLoading(UltralightView view, long frameId, boolean isMainFrame, String url) {
            if (isMainFrame) {
                Logger.info("Begin loading page: {}", url);
            }
        }

        @Override
        public void onFinishLoading(UltralightView view, long frameId, boolean isMainFrame, String url) {
            if (isMainFrame) {
                Logger.info("Page loaded successfully: {}", url);
            }
        }

        @Override
        public void onFailLoading(UltralightView view, long frameId, boolean isMainFrame, String url, String description, String errorDomain, int errorCode) {
            if (isMainFrame) {
                Logger.error("Failed to load page: {}", url);
                Logger.error("Error: {} (Domain: {}, Code: {})", description, errorDomain, errorCode);
            }
        }

        @Override
        public void onWindowObjectReady(UltralightView view, long frameId, boolean isMainFrame, String url) {
            if (isMainFrame) {
                Logger.info("Window object ready for page: {}", url);
            }
        }

        @Override
        public void onDOMReady(UltralightView view, long frameId, boolean isMainFrame, String url) {
            if (isMainFrame) {
                Logger.info("DOM ready for page: {}", url);
            }
        }

        @Override
        public void onUpdateHistory(UltralightView view) {
            Logger.info("History updated.");
        }
    }
}

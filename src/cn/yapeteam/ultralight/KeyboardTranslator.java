package cn.yapeteam.ultralight;

import net.janrupf.ujr.api.event.UlKeyCode;
import net.janrupf.ujr.api.event.UlKeyEventModifiers;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper to translate GLFW keyboard events to Ultralight keyboard events.
 */
public class KeyboardTranslator {
    private static final Map<Integer, Integer> LWJGL_TO_ULTRALIGHT_KEY_MAP = new HashMap<>();

    static {
        // 字母键映射（手动列出每个键）
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_A, UlKeyCode.A); // ASCII 'A' = 65
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_B, UlKeyCode.B); // ASCII 'B' = 66
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_C, UlKeyCode.C); // ASCII 'C' = 67
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_D, UlKeyCode.D); // ASCII 'D' = 68
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_E, UlKeyCode.E); // ASCII 'E' = 69
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F, UlKeyCode.F); // ASCII 'F' = 70
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_G, UlKeyCode.G); // ASCII 'G' = 71
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_H, UlKeyCode.H); // ASCII 'H' = 72
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_I, UlKeyCode.I); // ASCII 'I' = 73
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_J, UlKeyCode.J); // ASCII 'J' = 74
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_K, UlKeyCode.K); // ASCII 'K' = 75
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_L, UlKeyCode.L); // ASCII 'L' = 76
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_M, UlKeyCode.M); // ASCII 'M' = 77
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_N, UlKeyCode.N); // ASCII 'N' = 78
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_O, UlKeyCode.O); // ASCII 'O' = 79
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_P, UlKeyCode.P); // ASCII 'P' = 80
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_Q, UlKeyCode.Q); // ASCII 'Q' = 81
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_R, UlKeyCode.R); // ASCII 'R' = 82
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_S, UlKeyCode.S); // ASCII 'S' = 83
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_T, UlKeyCode.T); // ASCII 'T' = 84
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_U, UlKeyCode.U); // ASCII 'U' = 85
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_V, UlKeyCode.V); // ASCII 'V' = 86
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_W, UlKeyCode.W); // ASCII 'W' = 87
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_X, UlKeyCode.X); // ASCII 'X' = 88
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_Y, UlKeyCode.Y); // ASCII 'Y' = 89
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_Z, UlKeyCode.Z); // ASCII 'Z' = 90

        // 手动映射 LWJGL 的数字键到 Ultralight 的数字键
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_0, UlKeyCode.NUMBER_0); // '0'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_1, UlKeyCode.NUMBER_1); // '1'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_2, UlKeyCode.NUMBER_2); // '2'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_3, UlKeyCode.NUMBER_3); // '3'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_4, UlKeyCode.NUMBER_4); // '4'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_5, UlKeyCode.NUMBER_5); // '5'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_6, UlKeyCode.NUMBER_6); // '6'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_7, UlKeyCode.NUMBER_7); // '7'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_8, UlKeyCode.NUMBER_8); // '8'
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_9, UlKeyCode.NUMBER_9); // '9'

        // 功能键 F1-F12
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F1, UlKeyCode.F1);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F2, UlKeyCode.F2);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F3, UlKeyCode.F3);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F4, UlKeyCode.F4);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F5, UlKeyCode.F5);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F6, UlKeyCode.F6);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F7, UlKeyCode.F7);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F8, UlKeyCode.F8);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F9, UlKeyCode.F9);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F10, UlKeyCode.F10);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F11, UlKeyCode.F11);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_F12, UlKeyCode.F12);

        // 操作键映射
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_ESCAPE, UlKeyCode.ESCAPE);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_RETURN, UlKeyCode.RETURN);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_TAB, UlKeyCode.TAB);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_BACK, UlKeyCode.BACK);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_INSERT, UlKeyCode.INSERT);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_DELETE, UlKeyCode.DELETE);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_HOME, UlKeyCode.HOME);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_END, UlKeyCode.END);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_SPACE, UlKeyCode.SPACE);

        // 方向键映射
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_RIGHT, UlKeyCode.RIGHT);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_LEFT, UlKeyCode.LEFT);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_DOWN, UlKeyCode.DOWN);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_UP, UlKeyCode.UP);

        // 数字键盘映射
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD0, UlKeyCode.NUMPAD0);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD1, UlKeyCode.NUMPAD1);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD2, UlKeyCode.NUMPAD2);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD3, UlKeyCode.NUMPAD3);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD4, UlKeyCode.NUMPAD4);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD5, UlKeyCode.NUMPAD5);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD6, UlKeyCode.NUMPAD6);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD7, UlKeyCode.NUMPAD7);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD8, UlKeyCode.NUMPAD8);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_NUMPAD9, UlKeyCode.NUMPAD9);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_DECIMAL, UlKeyCode.DECIMAL);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_ADD, UlKeyCode.ADD);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_SUBTRACT, UlKeyCode.SUBTRACT);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_MULTIPLY, UlKeyCode.MULTIPLY);
        LWJGL_TO_ULTRALIGHT_KEY_MAP.put(Keyboard.KEY_DIVIDE, UlKeyCode.DIVIDE);
    }


    public static int lwjglKeyToUltralight(int lwjglKeyCode) {
        // 从映射表中查找对应的键码
        return LWJGL_TO_ULTRALIGHT_KEY_MAP.getOrDefault(lwjglKeyCode, UlKeyCode.UNKNOWN);
    }

    /**
     * 获取当前的修饰键状态并映射为 UlKeyEventModifiers 的集合。
     *
     * @return 包含当前按下的修饰键的集合。
     */
    public static EnumSet<UlKeyEventModifiers> getModifiers() {
        EnumSet<UlKeyEventModifiers> modifiers = EnumSet.noneOf(UlKeyEventModifiers.class);

        // 检查 Shift 键状态
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            modifiers.add(UlKeyEventModifiers.SHIFT);
        }

        // 检查 Ctrl 键状态
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
            modifiers.add(UlKeyEventModifiers.CTRL);
        }

        // 检查 Alt 键状态
        if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
            modifiers.add(UlKeyEventModifiers.ALT);
        }

        // 检查 Meta 键状态（Windows 键或 Command 键）
        if (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)) {
            modifiers.add(UlKeyEventModifiers.META);
        }

        return modifiers;
    }
}

package cn.yapeteam.ultralight.bridge;

import cn.yapeteam.ultralight.logger.Logger;
import net.janrupf.ujr.api.logger.UltralightLogLevel;
import net.janrupf.ujr.api.logger.UltralightLogger;

public class LoggerBridge implements UltralightLogger {
    @Override
    public void logMessage(UltralightLogLevel logLevel, String message) {
        switch (logLevel) {
            // Map levels 1:1
            case ERROR:
                Logger.error(message);
                break;
            case WARNING:
                Logger.warn(message);
                break;
            case INFO:
                Logger.info(message);
                break;
            default:
                throw new RuntimeException("Unknown log level: " + logLevel);
        }
    }
}

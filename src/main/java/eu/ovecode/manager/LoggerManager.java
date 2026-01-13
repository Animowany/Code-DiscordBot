package eu.ovecode.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {

    private final static Logger logger = LogManager.getLogger("OveCode-Discord");

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void error(String msg, Exception exception) {
        logger.error(msg + ": " + exception.getMessage());
    }

}

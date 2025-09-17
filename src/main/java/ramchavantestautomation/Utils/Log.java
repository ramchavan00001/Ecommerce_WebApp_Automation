package ramchavantestautomation.Utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger logger = LogManager.getLogger(Log.class);
    
    public static void startTestCase(String sTestCaseName){          
        Log.info("====================================="+sTestCaseName+" TEST START=========================================");
    }

    public static void endTestCase(String sTestCaseName){
        Log.info("====================================="+sTestCaseName+" TEST END=========================================");
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }
    
    
}


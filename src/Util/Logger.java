package Util;

import org.apache.commons.lang3.StringUtils;

public class Logger {

    private static Logger logger;
    private static Stage stage = Stage.UNKNOWN;

    private static Boolean enabled = true;

    public enum Stage {
        UNKNOWN("FML"),
        LOADING("LOADING"),
        TOKENIZING("TOKENIZING"),
        PARSING("PARSING"),
        VALIDATING("VALIDATING"),
        EVALUATING("EVALUATING");

        private String name;
        Stage(String name) {
            this.name = name;
        }

        public String toString() { return this.name; }
    }

    public static Logger get() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public static void setStage(Stage stage) {
        Logger.stage = stage;
    }

    public static void setEnabled(Boolean enabled) {
        Logger.enabled = enabled;
    }

    public void log(String msg) {
        if (!enabled) {
            return;
        }
        String formatted = StringUtils.rightPad(Logger.stage.toString(), 25, " ");
        formatted += ": " + msg;
        System.out.println(formatted);
    }

    public void log(String msg, Integer line) {
        if (!enabled) {
            return;
        }
        String formatted = StringUtils.rightPad(String.format("%s (line %d)", Logger.stage.toString(), line), 25, " ");
        formatted += ": " + msg;
        System.out.println(formatted);
    }

    public void logSeparator() {
        if (!enabled) {
            return;
        }
        System.out.println("--------------------------------------------------------");
    }
}

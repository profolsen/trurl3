import java.io.PrintStream;

public class LogUpdate {
    private static PrintStream log = System.out;
    private static Configuration config;

    public static void setLog(PrintStream ps) {
        log = ps;
    }

    public static void setConfiguration(Configuration config) {
        LogUpdate.config = config;
    }

    public PrintStream log()  {  return log;  }

    public LogUpdate(String message) {
        log.println("[" + config.cpu.time() + "] " + message);
    }
}

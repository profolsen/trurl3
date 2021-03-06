import java.io.FileNotFoundException;

public class Trurl {
    Configuration config = new Configuration();

    public Trurl() {
        LogUpdate.setConfiguration(config);
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
        LogUpdate.setConfiguration(config);
    }

    //kind of the main point in the program:
    //cycles the CPU every tenth of a second.
    public void run() {
        for(;;) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            config.cpu.cycle();
        }
    }

    public void executeProgram(String path) throws FileNotFoundException {
        Process p = new Process(path, config.cpu.time());
        config.interruptLine.add(Interrupt.generateNewProcessInterrupt(p, config));
        config.timer.setAlarm(25);
    }
}

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class CPU {

    private int time = 0;  //the current system time.
    Configuration config;
    private Process current;

    public void cycle() {
        time++; //time has progressed.
        if(config.interruptLine.isEmpty()) { //either move a process forward once per cycle
            if(current != null) {
                step(current);
            } else {
                if(time % 10 == 0) new LogUpdate("idle");
            }
        } else { // ...or handle one interrupt per cycle
            config.interruptLine.remove().handleInterrupt();
        }
        config.inputDevice.cycle(config);
    }

    public  void step(Process p) {
        try {
            String s = p.nextInstruction();
            String[] parts = s.split("\\s+");
            if(parts[0].equals("io")) {
                config.interruptLine.add(Interrupt.generateIORequestInterrupt(current, config));
            } else if(parts[0].equals("halt")) {
                config.interruptLine.add(Interrupt.generateTerminateProcessInterrupt(current, config));
            }
            else {
                //process is advanced one step.
            }
        } catch(NoSuchElementException nsee) {
            new LogUpdate("Error: Process without halt: " + p);
        }
    }

    public void setCurrent(Process p) {
        current = p;
    }

    public Process current()  {  return current;  }

    public void setConfiguration(Configuration configuration) {
        config = configuration;
    }

    public int time()  {  return time;  }
}

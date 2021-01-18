import java.util.InputMismatchException;
import java.util.LinkedList;

public class Configuration {
    //the default settings: First-Come-First-Terminated Process Scheduling.
    public ProcessQueue ready = ProcessQueue.FIFO_ProcessQueue();
    public ProcessQueue wait = ProcessQueue.FIFO_ProcessQueue();;
    public CPU cpu = new CPU();
    public Scheduler scheduler = Scheduler.DEFAULT;
    public LinkedList<Interrupt> interruptLine = new LinkedList<>();
    public Input inputDevice = new Input();

    public Configuration() {
        cpu.setConfiguration(this);
    }
}

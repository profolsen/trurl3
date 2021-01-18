import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Process {

    public static final int NEW = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int WAITING = 3;
    public static final int TERMINATED = 4;

    private static int ID_COUNTER = 1;

    private int id = -1; //id of the process.
    private int state = -1; //state of the process.
    private Scanner pc = null; //gives the next instruction for the process.
    private File source; //the file the process is from; for debug purposes.

    public Process(File program) throws FileNotFoundException {
        source = program;
        id = ID_COUNTER++;
        state = NEW;
        pc = new Scanner(new FileInputStream(program));
    }

    public Process(String path) throws FileNotFoundException {
        this(new File(path));
    }

    public String nextInstruction() throws NoSuchElementException {
        if(pc.hasNextLine()) return pc.nextLine();
        else throw new NoSuchElementException();
    }

    public void setState(int newState) {
        state = newState;
    }

    @Override
    public String toString() {
        return id + " (" + source + ") " + stateAsString();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Process) {
            return ((Process) o).id == id;
        }
        return false;
    }

    @Override
    public int hashCode()  {  return id;  }

    public int pid() {  return id;  }
    public int state() {  return state;  }


    private String stateAsString() {
        switch(state) {
            case NEW:
                return "NEW_" + NEW;
            case READY:
                return "READY_" + READY;
            case WAITING:
                return "WAITING_" + WAITING;
            case RUNNING:
                return "RUNNING_" + RUNNING;
            case TERMINATED:
                return "TERMINATED_" + TERMINATED;
            case -1:
                return "UNINITIALIZED_" + state;
            default:
                return "INVALID_" + state;
        }
    }
}

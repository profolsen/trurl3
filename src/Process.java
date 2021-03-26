import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A process in the Trurl system.
 * A program for Trurl is a sequence of the following instructions (it must always end with "halt"):
 * + run (indicates some CPU work)
 * + reserve X (requests X bytes of memory from the system)
 * + lock X (requests mutually exclusive access to resource X)
 * + io (requests I/O -- Input).
 */
public class Process {

    /*
    the different states a process can have:
     */
    public static final int NEW = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int WAITING = 3;
    public static final int TERMINATED = 4;

    //universal source for unique ids for each process.
    private static int ID_COUNTER = 1;

    private int id = -1; //id of the process.
    private int state = -1; //state of the process.
    private Scanner pc = null; //gives the next instruction for the process.
    private File source; //the file the process is from; for debug purposes.
    private int creationTime = -1; //the time at which the process was created.
    private boolean hasDoneIO = false;

    /**
     * Creates a process for a program.
     * @param program the File the program is found in.
     * @throws FileNotFoundException if the file isn't found.
     */
    public Process(File program, int creationTime) throws FileNotFoundException {
        source = program;
        id = ID_COUNTER++;
        state = NEW;
        pc = new Scanner(new FileInputStream(program));
        this.creationTime = creationTime;
    }

    /**
     * Creates a process without a file.
     * This is <i>only</i> used to create a kernel process, because kernel processes don't have files.
     */
    private Process() {
        source = null;
        id = ID_COUNTER++;
        state = NEW;
        pc = null;
        creationTime = 0;
    }

    /** see Process(File).*/
    public Process(String path, int creationTime) throws FileNotFoundException {
        this(new File(path), creationTime);
    }

    /** Creates a kernel process*/
    public static Process kernelProcess() {
        return new Process();
    }

    /**
     * Fetches the next instruction for the process.
     * @return the next instruction
     * @throws NoSuchElementException if there are no more instructions
     */
    public String nextInstruction() throws NoSuchElementException {
        if(pc.hasNextLine()) return pc.nextLine();
        else throw new NoSuchElementException();
    }

    /**
     * Changes the state of the process.
     * @param newState the new state the process should be in.
     */
    public void setState(int newState) {
        if(newState == WAITING) hasDoneIO = true;
        state = newState;
    }

    //gets the pid of the process
    public int pid() {  return id;  }

    //gets the state of the current process.
    public int state() {  return state;  }

    public boolean hasDoneIO() {  return hasDoneIO;  }


    //Converts the state (an integer) into a readable string as follows:
    //<String description>_<state number>
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

    public int age(int time) {
        return time - creationTime;
    }
}

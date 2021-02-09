import java.util.LinkedList;
import java.util.NoSuchElementException;

public class CPU {

    private int time = 0;  //the current system time.
    Configuration config;  //A variable that holds reference to all the important parts of the simulation,
                //e.g., CPU, ProcessQueues, Scheduler, etc.
    private Process current;  //the process that is currently on the CPU.
    boolean statReports = true;


    //the main cycle code for the simulation.
    public void cycle() {
        time++; //time has progressed.
        if(config.interruptLine.isEmpty()) { //if there are no interrupts to handle
            if(current != null) { //and there is a process currently on the CPU
                statReports = true;
                step(current);  //let that process use this CPU cycle
            } else { //otherwise, there is no process on the CPU (the idle case)
                if(time % 10 == 0) new LogUpdate("idle");  //log, idle cycles every ten cycles.
                if(statReports) {
                    statReports = false;
                    StatUpdate.update(config);
                }
            }
        } else { // there is an interrupt on the interrupt queue.
            config.interruptLine.remove().handleInterrupt();  //handle the interrupt
        }
        config.inputDevice.cycle(config); //the input device needs to be synchronized with the CPU.
        //NOTE: here synchronized means "stuff needs to happen at the same time", not the OS def. of synch.
    }

    //this is where a process is advanced one step by the CPU.
    public  void step(Process p) {
        try {
            String s = p.nextInstruction(); //fetch the next instruction.
            String[] parts = s.split("\\s+"); //first part of instruction is instruction type.
            if(parts[0].equals("io")) {  //some things are I/O instructions.
                config.interruptLine.add(Interrupt.generateIORequestInterrupt(current, config));  //TRAP! to do I/O
            } else if(parts[0].equals("halt")) { //halt is a special instruction that ends the program.
                config.interruptLine.add(Interrupt.generateTerminateProcessInterrupt(current, config)); //TRAP
                                                                                                    //to do halt.
            }
            else { //right now, all others are treated the same (as Arithmetic unit instructions).
                //ALU instructions are not simulated.
            }
        } catch(NoSuchElementException nsee) { //something went wrong:
            //1. there is a process currently on the CPU
            //2. but it has not more instructions meaning
            //it did not halt correctly.
            new LogUpdate("Error: Process without halt: " + p);  //Error.  Actually does not crash the system.
                        //Probably what should happen here is a system shutdown (correctly).
        }
    }

    //sets the current process (for context switching).
    public void setCurrent(Process p) {
        current = p;
    }

    //gets the current process.
    public Process current()  {  return current;  }

    //sets the configurations up.  Warning: this is a byzantine and probably the least realistic part of the
                            //simulation.
    public void setConfiguration(Configuration configuration) {
        config = configuration;
    }

    //gets the CPU time.
    public int time()  {  return time;  }
}

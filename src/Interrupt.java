public interface Interrupt {  //an interface for interrupts.
    public void handleInterrupt();  //each interrupt (very much not how a real system would work)
                //contains all the information needed to completely handle the interrupt.

    //an interrupt triggered at the end of I/O.
    public static Interrupt generateIOCompleteInterrupt(Process target, Configuration c) {
        return new Interrupt() {
            public void handleInterrupt() {
                target.setState(Process.READY);  //set the state to ready from waiting.
                c.ready.add(target); //add the process back on the ready queue.
                c.wait.remove(target);  //remove the process from the wait queue.
                c.interruptLine.add(Interrupt.generateSchedulerInterrupt(c));  //may need to reschedule.
                //c.scheduler.schedule(c);
                new LogUpdate(target.pid() + " completed i/o.");  //log the event.
            }
        };
    }

    public static Interrupt generateNewProcessInterrupt(Process source, Configuration c) {
        return new Interrupt() {
            public void handleInterrupt() {
                source.setState(Process.READY);
                c.ready.add(source);
                c.interruptLine.add(Interrupt.generateSchedulerInterrupt(c));
                new LogUpdate(source.pid() + " created; ready for execution.");
            }
        };
    }

    public static Interrupt generateTerminateProcessInterrupt(Process source, Configuration c) {
        return new Interrupt() {
            public void handleInterrupt() {
                source.setState(Process.TERMINATED);
                c.cpu.setCurrent(null);
                c.interruptLine.add(Interrupt.generateSchedulerInterrupt(c));
                new LogUpdate(source.pid() + " terminated.");
            }
        };
    }

    //the interrupt (trap) to handle an I/O request
    public static Interrupt generateIORequestInterrupt(Process source, Configuration c) {
        return new Interrupt() {
            public void handleInterrupt() {
                source.setState(Process.WAITING);  //put the process in the waiting state.
                c.wait.add(source);  //move to the wait queue. (it isn't on the ready queue, it's on the CPU)
                c.cpu.setCurrent(null);  //empty the CPU.
                c.interruptLine.add(Interrupt.generateSchedulerInterrupt(c)); //may need to reschedule (don't waste
                                        //CPU time!)
                new LogUpdate(source.pid() + " requested i/o.");  //log the event.
            }
        };
    }

    public static Interrupt generateSchedulerInterrupt(Configuration c) {
        return new Interrupt() {
            public void handleInterrupt() {
                c.scheduler.schedule(c);
                new LogUpdate("Scheduler ran.");
            }
        };
    }
}

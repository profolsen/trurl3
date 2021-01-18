public interface Interrupt {
    public void handleInterrupt();

    public static Interrupt generateIOCompleteInterrupt(Process target, Configuration c) {
        return new Interrupt() {
            public void handleInterrupt() {
                target.setState(Process.READY);
                c.ready.add(target);
                c.wait.remove(target);
                c.interruptLine.add(Interrupt.generateSchedulerInterrupt(c));
                //c.scheduler.schedule(c);
                new LogUpdate(target.pid() + " completed i/o.");
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

    public static Interrupt generateIORequestInterrupt(Process source, Configuration c) {
        return new Interrupt() {
            public void handleInterrupt() {
                source.setState(Process.WAITING);
                c.wait.add(source);
                c.cpu.setCurrent(null);
                c.interruptLine.add(Interrupt.generateSchedulerInterrupt(c));
                new LogUpdate(source.pid() + " requested i/o.");
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

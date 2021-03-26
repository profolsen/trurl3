public interface Scheduler {
    public void schedule(Configuration config);

    public static void contextSwitch(Configuration config, Process p) {  //convenience: code for context switching...
        //current process is kernel.
        Process x = config.temp;
        if(x != null) {
            x.setState(Process.READY);
            config.ready.add(x);
        }
        p.setState(Process.RUNNING);
        config.temp = p;
    }
    public static final Scheduler DEFAULT = new Scheduler() {  //a default schedule.
        public void schedule(Configuration config) {
            new LogUpdate("(pre) Sched: CPU Ready: " + config.cpu.current() + " " + config.ready);  //log scheduling event.
            if(!config.ready.isEmpty() && config.cpu.current().pid() == 1) {
                                                                        //if there's a ready process and either
                                                                        // CPU's process is null or it is the kernel and
                                                                        // there isn't a running process on the ready queue.
                Process target = config.ready.first();  //get the first ready process.
                config.ready.remove(target); //remove the first ready process
                contextSwitch(config, target); //put the next ready process on the CPU.
            }
            new LogUpdate("(post) Sched: CPU Ready: " + config.cpu.current() + " " + config.ready);  //log scheduling event.
        }
    };
}

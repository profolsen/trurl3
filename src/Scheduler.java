public interface Scheduler {
    public void schedule(Configuration config);

    public static void contextSwitch(Configuration config, Process p) {  //convenience: code for context switching...
        Process x = config.cpu.current();
        if(x != null) {
            x.setState(Process.READY);
            config.ready.add(x);
        }
        config.cpu.setCurrent(p);
    }
    public static final Scheduler DEFAULT = new Scheduler() {  //a default schedule.
        public void schedule(Configuration config) {
            new LogUpdate("Sched: CPU Ready: " + config.cpu.current() + " " + config.ready);  //log scheduling event.
            if(!config.ready.isEmpty() && config.cpu.current() == null) { //if there's a ready process and
                                                                        // CPU's process is null
                Process target = config.ready.first();  //get the first ready process.
                config.ready.remove(target); //remove the first ready process
                contextSwitch(config, target); //put the next ready process on the CPU.
            }
        }
    };
}

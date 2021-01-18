public interface Scheduler {
    public void schedule(Configuration config);

    public static void contextSwitch(Configuration config, Process p) {
        Process x = config.cpu.current();
        if(x != null) {
            x.setState(Process.READY);
            config.ready.add(x);
        }
        config.cpu.setCurrent(p);
    }
    public static final Scheduler DEFAULT = new Scheduler() {
        public void schedule(Configuration config) {
            new LogUpdate("Sched: CPU Ready: " + config.cpu.current() + " " + config.ready);
            if(!config.ready.isEmpty() && config.cpu.current() == null) {
                Process target = config.ready.first();
                config.ready.remove(target);
                contextSwitch(config, target);
            }
        }
    };
}

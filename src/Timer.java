public class Timer {
    private Configuration config;
    public int alarmSet;
    Interrupt action;

    public Timer(Configuration config, Interrupt action) {
        config.timer = this;
        this.config = config;
        this.action = action;
    }

    public void setAlarm(int when) {
        alarmSet = when;
    }

    public void check() {
        if(alarmSet == config.cpu.time() && action != null) {
            config.interruptLine.add(action);
        }
    }

    public void setAction(Interrupt action) {
        this.action = action;
    }

    public void cancel() {
        setAlarm(config.cpu.time() - 1);
    }
}

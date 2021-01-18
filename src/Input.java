public class Input {
    private int frequency = 5;
    public int time;

    public void cycle(Configuration config) {
        time++;
        if(! config.wait.isEmpty() && (time % frequency == 0)) {
            Process target = config.wait.first();
            config.wait.remove(target);
            config.interruptLine.add(Interrupt.generateIOCompleteInterrupt(target, config));
        }
    }
}

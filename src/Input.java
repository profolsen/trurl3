public class Input {
    private int frequency = 5;
    public int time;

    public void cycle(Configuration config) {
        time++; //time has moved forward.
        if(! config.wait.isEmpty() && (time % frequency == 0)) {  //if there's a waiting process and
                                                            // I/O is due to happen
            Process target = config.wait.first();  //get the first process on the wait queue.
            config.interruptLine.add(Interrupt.generateIOCompleteInterrupt(target, config)); //interrupt:
                                //somebody just finished I/O!
        }
    }
}

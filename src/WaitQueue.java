public interface WaitQueue {
    public void add(Process p);
    public void remove(Process p);
    public Process first();
    public boolean isEmpty();
}

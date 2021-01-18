import java.util.LinkedList;

public interface ProcessQueue {
    public void add(Process p);
    public boolean isEmpty();
    public Process first();
    public void remove(Process p);

    public static ProcessQueue FIFO_ProcessQueue() {
        return new ProcessQueue() {
            LinkedList<Process> queue = new LinkedList<>();
            public void add(Process p) {
                queue.add(p);
            }

            public void remove(Process p) {
                queue.remove(p);
            }

            public Process first() {  return queue.getFirst();  }

            public boolean isEmpty() {  return queue.isEmpty();  }

            public String toString() {  return "" + queue;  }
        };
    }
}

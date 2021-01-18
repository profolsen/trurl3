import java.io.FileNotFoundException;

public class Klapaucius {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        Trurl trurl = new Trurl();
        Thread t = new Thread() {
            public void run() {
                trurl.run();
            }
        };
        t.start();
        trurl.executeProgram("p1.txt");
        trurl.executeProgram("p2.txt");
        trurl.executeProgram("p3.txt");
        Thread.sleep(60000);
        t.interrupt();
    }
}

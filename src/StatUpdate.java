import java.util.HashMap;
import java.util.ArrayList;

public class StatUpdate {

    public static final String termination = "termination";
    public static final String responseTime = "responseTime";
    public static HashMap<String, ArrayList<Object>> stats = new HashMap<>();

    public StatUpdate(String key, Object value) {
        if(!stats.containsKey(key)) stats.put(key, new ArrayList<Object>());
        stats.get(key).add(value);
    }

    public static void update(Configuration config) {
        if(stats.containsKey(termination)) { //first do avg. turnaround.
            double sum = 0;
            for(Object o : stats.get(termination)) {
                sum += (int)o;
            }
            new LogUpdate("Avg. Turnaround: " + (sum / (double)(stats.get(termination).size())) / 10 + " sec/process");
        }
        if(stats.containsKey(termination)) { //first do avg. turnaround.
            int count = 0;
            for(Object o : stats.get(termination)) {
                count++;
            }
            new LogUpdate("Throughput: " + (count / (double)config.cpu.time()) * 10 + " processes/sec");
        }
        if(stats.containsKey(responseTime)) {
            ArrayList<Object> responseTimes = stats.get(responseTime);
            double sum = 0;
            for(Object o : responseTimes) {
                sum = sum + (Integer)o;
            }
            double finalAvg = sum/responseTimes.size();
            new LogUpdate("Average response time: " + finalAvg * 10 + " ms.");
        }
    }
}

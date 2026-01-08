import java.util.List;

public class ProcessStatus {
    public int processId;
    public String processName;
    public int completedSteps;
    public int totalSteps;
    public String currentStatus; 
    public String waitingFor;    

    public ProcessStatus(int id, String name, int done, int total, String status, List<String> missing) {
        this.processId = id;
        this.processName = name;
        this.completedSteps = done;
        this.totalSteps = total;
        this.currentStatus = status;
        
        if (missing.isEmpty()) {
            this.waitingFor = "-";
        } else {
            this.waitingFor = String.join(", ", missing);
        }
    }
}
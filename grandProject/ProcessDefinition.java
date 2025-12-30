import java.util.ArrayList;
import java.util.List;

public class ProcessDefinition {
    private String processName;
    private List<String> steps;

    public ProcessDefinition(String processName) {
        this.processName = processName;
        this.steps = new ArrayList<>();
    }
    public void addStep(String stepName) {
        this.steps.add(stepName);
    }
    public String getProcessName() {
        return processName;
    }
    public List<String> getSteps() {
        return steps;
    }

    public boolean hasStep(String reportType) {
        return steps.contains(reportType);
         
    }
}
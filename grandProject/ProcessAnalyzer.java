import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessAnalyzer {

    // CHANGE: This now returns a String instead of void
   // ... imports ...

    // NEW METHOD: Returns structured data for the GUI Table
    public List<ProcessStatus> getTableData(List<Report> reports, List<ProcessDefinition> rules) {
        List<ProcessStatus> tableRows = new ArrayList<>();
        
        // 1. Group reports (same as before)
        Map<Integer, List<Report>> groupedReports = new HashMap<>();
        for (Report r : reports) {
            int pid = r.getProcessId();
            if (!groupedReports.containsKey(pid)) groupedReports.put(pid, new ArrayList<>());
            groupedReports.get(pid).add(r);
        }

        // 2. Analyze each group
        for (Integer pid : groupedReports.keySet()) {
            List<Report> currentReports = groupedReports.get(pid);
            ProcessDefinition matchingRule = findMatchingRule(currentReports, rules);

            if (matchingRule != null) {
                // Calculate stats
                int total = matchingRule.getSteps().size();
                int found = 0;
                List<String> missing = new ArrayList<>();

                for (String stepName : matchingRule.getSteps()) {
                    boolean stepExists = false;
                    for (Report r : currentReports) {
                        if (r.getReportType().equalsIgnoreCase(stepName)) {
                            stepExists = true;
                            break;
                        }
                    }
                    if (stepExists) found++;
                    else missing.add(stepName);
                }

                String status = (found == total) ? "COMPLETE" : "IN PROGRESS";
                
                // Add to our list
                tableRows.add(new ProcessStatus(pid, matchingRule.getProcessName(), found, total, status, missing));
            }
        }
        return tableRows;
    }

    // ... keep your helper methods (findMatchingRule) ...

    private ProcessDefinition findMatchingRule(List<Report> reports, List<ProcessDefinition> rules) {
        if (reports == null || reports.isEmpty()) return null;
        String sampleType = reports.get(0).getReportType();
        for (ProcessDefinition rule : rules) {
            if (rule.hasStep(sampleType)) return rule;
        }
        return null;
    }

    // CHANGE: This now returns a String line instead of printing
    private String evaluateRule(int pid, List<Report> reports, ProcessDefinition rule) {
        List<String> requiredSteps = rule.getSteps();
        int stepsFoundCount = 0;
        List<String> missingSteps = new ArrayList<>();

        for (String stepName : requiredSteps) {
            boolean found = false;
            for (Report r : reports) {
                if (r.getReportType() != null && r.getReportType().equalsIgnoreCase(stepName)) {
                    found = true;
                    break;
                }
            }
            if (found) stepsFoundCount++;
            else missingSteps.add(stepName);
        }

        String status = (stepsFoundCount == requiredSteps.size()) ? "[COMPLETE]" : "[IN PROGRESS]";
        String result = "Process #" + pid + " (" + rule.getProcessName() + "): " + status;
        
        if (!missingSteps.isEmpty()) {
            result += " -> Waiting for: " + missingSteps;
        }
        return result;
    }

    public String getAnalysisReport(List<Report> allReports, List<ProcessDefinition> rules) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAnalysisReport'");
    }
}
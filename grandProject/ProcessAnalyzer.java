import java.util.*;

public class ProcessAnalyzer {

    public List<ProcessStatus> getTableData(List<Report> allReports, List<ProcessDefinition> rules) {
        List<ProcessStatus> results = new ArrayList<>();

        // 1. Group all reports by their Process ID
        Map<Integer, List<Report>> reportsByProject = new HashMap<>();
        for (Report r : allReports) {
            reportsByProject.computeIfAbsent(r.getProcessId(), k -> new ArrayList<>()).add(r);
        }

        // 2. Loop through every Project ID found in your reports
        for (Integer pid : reportsByProject.keySet()) {
            List<Report> projectReports = reportsByProject.get(pid);
            
            // 3. Find which blueprint this project belongs to
            ProcessDefinition blueprint = findBlueprintForProject(projectReports, rules);

            if (blueprint != null) {
                int totalSteps = blueprint.getSteps().size();
                int doneCount = 0;
                List<String> missing = new ArrayList<>();

                // 4. THE PROOF: Does a report exist for each specific step?
                for (String stepName : blueprint.getSteps()) {
                    boolean isDone = false;
                    for (Report r : projectReports) {
                        if (r.getReportType().equalsIgnoreCase(stepName)) {
                            isDone = true;
                            break;
                        }
                    }
                    
                    if (isDone) doneCount++;
                    else missing.add(stepName);
                }

                String status = (doneCount == totalSteps) ? "OPERATIONAL" : "IN PROGRESS";
                results.add(new ProcessStatus(pid, blueprint.getProcessName(), doneCount, totalSteps, status, missing));
            }
        }
        return results;
    }

    private ProcessDefinition findBlueprintForProject(List<Report> reports, List<ProcessDefinition> rules) {
        for (Report r : reports) {
            for (ProcessDefinition rule : rules) {
                if (rule.hasStep(r.getReportType())) {
                    return rule;
                }
            }
            // --- DEBUG LINE START ---
            System.out.println("DEBUG: Found report type '" + r.getReportType() + "' but no Blueprint matches it!");
            // --- DEBUG LINE END ---
        }
        return null;
    }
}

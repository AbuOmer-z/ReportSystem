import java.util.*;

public class ProcessAnalyzer {

    // Main method to generate dashboard rows
    public List<ProcessStatus> getTableData(List<Report> allReports, 
                                            List<ProcessDefinition> rules, 
                                            Map<Integer, String> activeProjects) {
        
        List<ProcessStatus> results = new ArrayList<>();

        // 1. Group existing reports by Project ID
        Map<Integer, List<Report>> reportsByProject = new HashMap<>();
        for (Report r : allReports) {
            reportsByProject.computeIfAbsent(r.getProcessId(), k -> new ArrayList<>()).add(r);
        }

        // 2. MERGE: Get ALL IDs from both the Registry AND the Reports
        // This ensures we see "Just Started" tasks AND old tasks
        Set<Integer> allProjectIds = new HashSet<>();
        allProjectIds.addAll(activeProjects.keySet());
        allProjectIds.addAll(reportsByProject.keySet());

        // 3. Iterate through every Project ID found
        for (Integer pid : allProjectIds) {
            String blueprintName = activeProjects.get(pid);
            List<Report> projectReports = reportsByProject.getOrDefault(pid, new ArrayList<>());

            ProcessDefinition blueprint;

            // --- THE FIX IS HERE ---
            if (blueprintName != null) {
                // Case A: Found in Registry -> Search by String Name
                blueprint = findBlueprintByName(blueprintName, rules);
            } else {
                // Case B: Not in Registry -> Guess by analyzing Reports
                blueprint = findBlueprintForProject(projectReports, rules);
            }
            // -----------------------

            if (blueprint != null) {
                int totalSteps = blueprint.getSteps().size();
                int doneCount = 0;
                List<String> missing = new ArrayList<>();

                // Calculate Progress
                for (String stepName : blueprint.getSteps()) {
                    boolean isDone = false;
                    for (Report r : projectReports) {
                        // Use trim/ignoreCase to avoid "Clean " vs "Clean" mismatch
                        if (r.getReportType().trim().equalsIgnoreCase(stepName.trim())) {
                            isDone = true;
                            break;
                        }
                    }
                    if (isDone) doneCount++;
                    else missing.add(stepName);
                }

                String status = (doneCount == totalSteps) ? "COMPLETE" : "IN PROGRESS";
                if (doneCount == 0) status = "JUST STARTED";

                results.add(new ProcessStatus(pid, blueprint.getProcessName(), doneCount, totalSteps, status, missing));
            }
        }
        return results;
    }

    // --- HELPER METHODS ---

    // NEW METHOD: Finds blueprint by String name (Used for Registry)
    private ProcessDefinition findBlueprintByName(String name, List<ProcessDefinition> rules) {
        for (ProcessDefinition pd : rules) {
            if (pd.getProcessName().equalsIgnoreCase(name)) {
                return pd;
            }
        }
        return null;
    }

    // OLD METHOD: Finds blueprint by analyzing List of Reports (Used for legacy data)
    private ProcessDefinition findBlueprintForProject(List<Report> reports, List<ProcessDefinition> rules) {
        for (Report r : reports) {
            for (ProcessDefinition rule : rules) {
                if (rule.hasStep(r.getReportType())) {
                    return rule;
                }
            }
        }
        return null;
    }
}
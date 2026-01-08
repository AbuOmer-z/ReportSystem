import java.io.*;
import java.util.*;

public class ProjectRegistry {
    private File file = new File("active_projects.txt");

    // Saves a new project: "101,CarAssembly"
    public void startProject(int id, String blueprintName) {
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(id + "," + blueprintName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads all projects: Map<ProjectID, BlueprintName>
    public Map<Integer, String> getActiveProjects() {
        Map<Integer, String> projects = new HashMap<>();
        if (!file.exists()) return projects;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    projects.put(Integer.parseInt(parts[0]), parts[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }
}

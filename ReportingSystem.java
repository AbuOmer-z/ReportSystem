import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportingSystem {

    private List<Report> allReports;// 

    private ReportParser parser;

    public ReportingSystem() {
        this.allReports = new ArrayList<>();
        this.parser = new ReportParser();
    }

    // 1. Backend Logic: Read all .xml files in a specific folder
    public void loadReportsFromFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            // We can print to console just for debugging, but the User won't see this
            System.out.println("Reading reports from folder: " + folderPath);
            
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".xml")) {
                    loadSingleReport(file.getAbsolutePath());
                }
            }
        }
    }

    // 2. Backend Logic: Parse a single file
    public void loadSingleReport(String filePath) {
        Report report = parser.parseReport(filePath); //makes it an xml
        // Only add if it's a valid report (not null)
        if (report != null) {
            this.allReports.add(report);
            System.out.println(" -> Loaded: " + report.getTitle());
        }
    }

    // 3. Backend Logic: Getter for the GUI to access data
    public List<Report> getAllReports() {
        return allReports;
    }

    // 4. Main Method: The Entry Point
    public static void main(String[] args) {
        // Swing applications should run on the "Event Dispatch Thread"
        // This is the standard safe way to start a Java GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create and show the GUI Dashboard
                Dashboard gui = new Dashboard();
                gui.setVisible(true);
            }
        });
    }
}
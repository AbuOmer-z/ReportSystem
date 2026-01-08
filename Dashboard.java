import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class Dashboard extends JFrame {

    // Backend Tools
    private ReportingSystem system;
    private ProcessConfigLoader configLoader;
    private ProcessAnalyzer analyzer;
    private List<ProcessDefinition> rules;

    // GUI Components
    private JTable monitorTable;        // <--- THE NEW TABLE
    private DefaultTableModel tableModel; // <--- HOLDS THE DATA
    private ProjectRegistry projectRegistry; // <--- NEW

    public Dashboard() {
        // 1. Initialize Backend
        projectRegistry = new ProjectRegistry(); // <--- ADD THIS LINE HERE
        system = new ReportingSystem();
        configLoader = new ProcessConfigLoader();
        analyzer = new ProcessAnalyzer();
        rules = configLoader.loadDefinition("processes.xml");

        // 2. Setup Window
        setTitle("Factory Command Center"); // New Name
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 3. Setup the Table (Center)
        // Define Columns
        String[] columns = {"ID", "Process Name", "Progress", "Status", "Waiting For"};
        
        // Create the Model (Data Handler)
        tableModel = new DefaultTableModel(columns, 0);
        
        // Create the Table
        monitorTable = new JTable(tableModel);
        monitorTable.setRowHeight(25); // Make rows easier to read
        monitorTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Add to Scroll Pane
        add(new JScrollPane(monitorTable), BorderLayout.CENTER);

        // 4. Buttons (South)
        JPanel buttonPanel = new JPanel();

        JButton createButton = new JButton("Create Report");
        createButton.addActionListener(e -> new ReportForm(Dashboard.this).setVisible(true));
        
        JButton designButton = new JButton("Design New Process");
        designButton.addActionListener(e -> new ProcessDesigner().setVisible(true));

        JButton refreshButton = new JButton("Refresh Monitor");
        refreshButton.addActionListener(e -> refreshData());


        buttonPanel.add(createButton);
        buttonPanel.add(designButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 5. Initial Load
        refreshData();
        projectRegistry = new ProjectRegistry(); // <--- Initialize

        // ... inside buttonPanel ...
        JButton startTaskButton = new JButton("Start New Task");
        startTaskButton.addActionListener(e -> showStartTaskDialog());
        buttonPanel.add(startTaskButton);
    }

    private void showStartTaskDialog() {
        // 1. Get available Process Names from XML
        String[] processNames = rules.stream()
                                     .map(ProcessDefinition::getProcessName)
                                     .toArray(String[]::new);
        
        if (processNames.length == 0) {
            JOptionPane.showMessageDialog(this, "No Processes defined in XML!");
            return;
        }

        // 2. Let user pick one
        String selectedProcess = (String) JOptionPane.showInputDialog(
                this, "Select Process to Start:", "Start Task",
                JOptionPane.QUESTION_MESSAGE, null, processNames, processNames[0]);

        if (selectedProcess != null) {
            // 3. Generate a new Project ID (Random or Sequential)
            int newProjectId = (int)(System.currentTimeMillis() % 10000); // Simple ID
            
            // 4. Save to Registry
            projectRegistry.startProject(newProjectId, selectedProcess);
            
            JOptionPane.showMessageDialog(this, "Task Started! ID: " + newProjectId);
            refreshData(); // Refresh table immediately
        }
    }

    public void refreshData() {
        system = new ReportingSystem();
        system.loadReportsFromFolder("./");
        
        // PASS THE REGISTRY MAP HERE
        Map<Integer, String> activeMap = projectRegistry.getActiveProjects();
        List<ProcessStatus> rows = analyzer.getTableData(system.getAllReports(), rules, activeMap);
        
        tableModel.setRowCount(0); // Clear old rows

        for (ProcessStatus row : rows) {
            Object[] data = {
                row.processId,
                row.processName,
                row.completedSteps + " / " + row.totalSteps,
                row.currentStatus,
                row.waitingFor
            };
            tableModel.addRow(data);
        }
    }
   
}


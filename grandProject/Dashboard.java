import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class Dashboard extends JFrame {

    // Backend Tools
    private ReportingSystem system;
    private ProcessConfigLoader configLoader;
    private ProcessAnalyzer analyzer;
    private List<ProcessDefinition> rules;
    private ProjectRegistry projectRegistry; // Keeps track of active tasks

    // GUI Components
    private JTable monitorTable;
    private DefaultTableModel tableModel;

    public Dashboard() {
        // 1. Initialize Backend
        projectRegistry = new ProjectRegistry(); // Must init this first
        system = new ReportingSystem();
        configLoader = new ProcessConfigLoader();
        analyzer = new ProcessAnalyzer();
        
        // Load rules (ensure processes.xml exists)
        rules = configLoader.loadDefinition("processes.xml");

        // 2. Setup Window
        setTitle("Factory Command Center");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 3. Setup the Table (Center)
        String[] columns = {"ID", "Process Name", "Progress", "Status", "Waiting For"};
        tableModel = new DefaultTableModel(columns, 0);
        
        monitorTable = new JTable(tableModel);
        monitorTable.setRowHeight(25);
        monitorTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        add(new JScrollPane(monitorTable), BorderLayout.CENTER);

        // 4. Buttons (South)
        JPanel buttonPanel = new JPanel();

        // [NEW] Start Task Button
        JButton startTaskButton = new JButton("Start New Task");
        startTaskButton.addActionListener(e -> showStartTaskDialog());
        buttonPanel.add(startTaskButton);

        // [NEW] Delete Task Button
        JButton deleteButton = new JButton("Delete Task");
        deleteButton.setForeground(Color.RED); // Red color for caution
        deleteButton.addActionListener(e -> deleteSelectedTask());
        buttonPanel.add(deleteButton);

        // Existing Buttons
        JButton createButton = new JButton("Create Report");
        createButton.addActionListener(e -> new ReportForm(Dashboard.this).setVisible(true));
        buttonPanel.add(createButton);
        
        JButton designButton = new JButton("Design New Process");/*this button function is in ProcessDesigner */
        designButton.addActionListener(e -> new ProcessDesigner().setVisible(true));
        buttonPanel.add(designButton);

        JButton refreshButton = new JButton("Refresh Monitor");
        refreshButton.addActionListener(e -> refreshData());
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 5. Initial Load
        refreshData();
    }

    // --- BUTTON LOGIC: Start New Task ---
    private void showStartTaskDialog() {
        if (rules == null || rules.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Processes defined in processes.xml!");
            return;
        }

        String[] processNames = rules.stream()
                                     .map(ProcessDefinition::getProcessName)
                                     .toArray(String[]::new);
        
        String selectedProcess = (String) JOptionPane.showInputDialog(
                this, "Select Process to Start:", "Start Task",
                JOptionPane.QUESTION_MESSAGE, null, processNames, processNames[0]);

        if (selectedProcess != null) {
            int newProjectId = (int)(System.currentTimeMillis() % 10000); 
            projectRegistry.startProject(newProjectId, selectedProcess);
            JOptionPane.showMessageDialog(this, "Task Started! ID: " + newProjectId);
            refreshData(); 
        }
    }

    // --- BUTTON LOGIC: Delete Task ---
    private void deleteSelectedTask() {
        int selectedRow = monitorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task from the table first.");
            return;
        }

        // Get ID from column 0
        int projectId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete Task #" + projectId + "?", 
            "Delete Confirmation", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            projectRegistry.removeProject(projectId);
            refreshData(); // Reload table to show it's gone
        }
    }

    public void refreshData() {
        system = new ReportingSystem();
        system.loadReportsFromFolder("./"); 
        
        // Use the Registry + Reports hybrid approach
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

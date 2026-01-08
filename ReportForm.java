import javax.swing.*;
import java.awt.*;

public class ReportForm extends JDialog {
    private JTextField projectIdField, typeField, titleField;
    private JTextArea contentArea;
    private Dashboard parent;

    public ReportForm(Dashboard owner) {
        super(owner, "Submit Work Report", true);
        this.parent = owner;
        setSize(400, 450);
        setLayout(new GridLayout(6, 1, 10, 10));

        add(new JLabel(" Project ID (Case Number):"));
        projectIdField = new JTextField();
        add(projectIdField);

        add(new JLabel(" Step Name (Must match Blueprint exactly):"));
        typeField = new JTextField();
        add(typeField);

        add(new JLabel(" Report Title:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel(" Details/Notes:"));
        contentArea = new JTextArea();
        add(new JScrollPane(contentArea));

        JButton saveButton = new JButton("SUBMIT PROOF OF WORK");
        saveButton.addActionListener(e -> saveReport());
        add(saveButton);
    }

    private void saveReport() {
        try {
            int pid = Integer.parseInt(projectIdField.getText());
            String type = typeField.getText().trim();
            int reportId = (int)(System.currentTimeMillis() % 10000); 

            Report r = new Report(reportId, pid, titleField.getText(), contentArea.getText(), 0, "Staff", type);
            new ReportWriter().saveReport(r);
            
            JOptionPane.showMessageDialog(this, "Proof for '" + type + "' saved!");
            dispose();
            parent.refreshData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Check your Project ID number.");
        }
    }
}
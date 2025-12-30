import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ReportForm extends JDialog {
    private JTextField idField, processIdField, typeField, titleField;
    private JTextArea contentArea;
    private ReportWriter writer;
    private Dashboard parentDashboard;

    public ReportForm(Dashboard owner) {
        super(owner, "Create New Repprt", true);
        this.parentDashboard = owner;
        this.writer = new ReportWriter();

        setSize(400,500);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5,2,10,10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        formPanel.add(new JLabel("Report ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Process ID:"));
        processIdField = new JTextField();
        formPanel.add(processIdField);

        formPanel.add(new JLabel("Report Type:"));
        typeField = new JTextField();
        formPanel.add(typeField);

        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Content:"));
        contentArea = new JTextArea();
        contentArea.setRows(3);

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(contentArea), BorderLayout.CENTER);
        JButton saveButton = new JButton("Save Report");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                saveData();
            }
        });
        add(saveButton, BorderLayout.SOUTH);
    }
    private void saveData() {
        try{
        int id = Integer.parseInt(idField.getText());
        int pid = Integer.parseInt(processIdField.getText());
        String type = typeField.getText();
        String title = titleField.getText();
        String content = contentArea.getText();

        Report r = new Report(id, pid, title, content, 0 , "General", type);

        writer.saveReport(r);

        JOptionPane.showMessageDialog(this, "Report Saved!");
            dispose(); // Close the form
            parentDashboard.refreshData(); // Refresh the main screen

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: IDs must be numbers!");
        } catch (Exception ex) {
            ex.printStackTrace();
    
}
}
}

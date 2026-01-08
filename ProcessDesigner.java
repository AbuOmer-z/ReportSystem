import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.util.ArrayList;

public class ProcessDesigner extends JFrame {

    private JTextField processNameField;
    private JTextField stepNameField;
    private DefaultListModel<String> stepsListModel;
    private JList<String> stepsList;

    public ProcessDesigner() {
        setTitle("Process Blueprint Designer");
        setSize(500, 400);
        setLayout(new BorderLayout());

        // --- TOP: Name the Process ---
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Process Name:"));
        processNameField = new JTextField(20);
        topPanel.add(processNameField);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER: The Steps List ---
        stepsListModel = new DefaultListModel<>();
        stepsList = new JList<>(stepsListModel);
        add(new JScrollPane(stepsList), BorderLayout.CENTER);

        // --- BOTTOM: Add Steps & Save ---
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        
        // Row 1: Add Step Controls
        JPanel addStepPanel = new JPanel(new FlowLayout());
        addStepPanel.add(new JLabel("Step Name:"));
        stepNameField = new JTextField(15);
        JButton addStepButton = new JButton("Add Step ->");
        
        addStepButton.addActionListener(e -> {
            String step = stepNameField.getText().trim();
            if (!step.isEmpty()) {
                stepsListModel.addElement(step); // Add to UI list
                stepNameField.setText("");       // Clear text box
            }
        });
        
        addStepPanel.add(stepNameField);
        addStepPanel.add(addStepButton);
        
        // Row 2: Save Button
        JButton saveButton = new JButton("SAVE BLUEPRINT TO SYSTEM");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> saveToXML());

        bottomPanel.add(addStepPanel);
        bottomPanel.add(saveButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void saveToXML() {
        String procName = processNameField.getText().trim();
        if (procName.isEmpty() || stepsListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please define a name and at least one step.");
            return;
        }

        try {
            // 1. Load existing XML (or create new if missing)
            File file = new File("processes.xml");
            Document doc;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            if (file.exists()) {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } else {
                doc = dBuilder.newDocument();
                Element root = doc.createElement("processes");
                doc.appendChild(root);
            }

            // 2. Create the new <process> tag
            Element root = doc.getDocumentElement();
            Element newProcess = doc.createElement("process");

            // Name
            Element nameTag = doc.createElement("name");
            nameTag.setTextContent(procName);
            newProcess.appendChild(nameTag);

            // Steps
            for (int i = 0; i < stepsListModel.size(); i++) {
                Element stepTag = doc.createElement("step");
                stepTag.setTextContent(stepsListModel.get(i));
                newProcess.appendChild(stepTag);
            }

            root.appendChild(newProcess);

            // 3. Save back to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this, "Process '" + procName + "' saved!");
            dispose(); // Close window

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving XML: " + ex.getMessage());
        }
    }
}
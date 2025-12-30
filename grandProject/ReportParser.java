import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ReportParser {
    public Report parseReport(String xmlFilePath) {
        try {
            File xmlFlie = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFlie);
            doc.getDocumentElement().normalize();
            NodeList reportTags = doc.getElementsByTagName("report");

            if (reportTags.getLength() == 0) {
                return null;
            }

            Node reportNode = doc.getElementsByTagName("report").item(0);

            if (reportNode.getNodeType() == Node.ELEMENT_NODE) {
                Element reportElement = (Element) reportNode;

                int id = Integer.parseInt(getTagValue("id", reportElement));
                int processId = Integer.parseInt(getTagValue("process_id", reportElement)); // <--- NEW
                String title = getTagValue("title", reportElement);
                String content = getTagValue("content", reportElement);
                int authorId = Integer.parseInt(getTagValue("authorId", reportElement));
                String department = getTagValue("department", reportElement);
                String type = getTagValue("type", reportElement);

                Report report = new Report(id, processId, title, content, authorId, department,type);
                return report;

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
// --- Helper Method ---
// A small utility to get the text content from an XML tag.
private String getTagValue(String tagName, Element element) {
    return element.getElementsByTagName(tagName).item(0).getTextContent();
}

    
}

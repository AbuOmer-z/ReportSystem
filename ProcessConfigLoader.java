import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class ProcessConfigLoader {
    public List<ProcessDefinition> loadDefinition(String filePath) {
        List<ProcessDefinition> definitions = new ArrayList<>();

        try{
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList processNodes = doc.getElementsByTagName("process");

            for(int i = 0 ; i<processNodes.getLength(); i++){
                Element processElement = (Element) processNodes.item(i);

                String name = processElement.getElementsByTagName("name").item(0).getTextContent();
                ProcessDefinition def = new ProcessDefinition(name);

                NodeList stepNodes = processElement.getElementsByTagName("step");
                for(int j = 0;j< stepNodes.getLength(); j++){
                    String stepName = stepNodes.item(j).getTextContent();
                    def.addStep(stepName);
                }

                definitions.add(def);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return definitions;
        }
}

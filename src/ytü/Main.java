package ytü;

import LTSM.process.ProvenanceProcessor;
import org.apache.commons.codec.language.Soundex;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

public class Main {

    private static String newDocument;
    /*private static ArrayList<String> agentNodes;
    private static ArrayList<String> entityNodes;
    private static ArrayList<String> activityNodes;*/

    public static void main(String[] args){
        ProvenanceProcessor processor = new ProvenanceProcessor();
        Document document = null;
        String result = processor.getOPMGraphInStringFromOPMXMLFile
                ("/home/erkan/Desktop/Final_Project/Project_Files/data_files/animation/4.txt");

        /*agentNodes      = new ArrayList<>();
        entityNodes     = new ArrayList<>();
        activityNodes   = new ArrayList<>();*/
        newDocument     = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<prov:document xmlns:prov=\"http://www.w3.org/ns/prov#\" xmlns:ns2=\"http://openprovenance.org/prov/extension#\" " +
                 "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:pronaliz=\"http://www.pronaliz.yildiz.edu.tr\">\n";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(result)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i=0; i<document.getDocumentElement().getChildNodes().getLength(); i++){
            if(     document.getDocumentElement().getChildNodes().item(i).getNodeName().equals("v1:accounts")   ||
                    document.getDocumentElement().getChildNodes().item(i).getNodeName().equals("v1:processes")  ||
                    document.getDocumentElement().getChildNodes().item(i).getNodeName().equals("v1:agents")     ||
                    document.getDocumentElement().getChildNodes().item(i).getNodeName().equals("v1:artifacts")){

                document.getDocumentElement().removeChild(document.getDocumentElement().getChildNodes().item(i));

            }else if (document.getDocumentElement().getChildNodes().item(i).getNodeName().equals("v1:causalDependencies")){
                NodeList nodeList = document.getDocumentElement().getChildNodes().item(i).getChildNodes();
                for(int j=0; j< nodeList.getLength(); j++){
                    NodeList temp = nodeList.item(j).getChildNodes();
                    if(nodeList.item(j).getNodeName().contains("wasTriggeredBy")){
                        for(int k=0; k<temp.getLength(); k++){
                            if(temp.item(k).getNodeName().contains("cause")){
                                if(temp.item(k).getAttributes().getNamedItem("ref").getNodeValue().contains("Process")){
                                    createWasInformedBy(nodeList.item(j));
                                }else {
                                    createWasAssociatedWith(nodeList.item(j));
                                }
                                break;
                            }
                        }
                    }else if(nodeList.item(j).getNodeName().contains("used")){
                         createUsed(nodeList.item(j));
                    }else if(nodeList.item(j).getNodeName().contains("wasDerivedFrom")){
                        createWasDerivedFrom(nodeList.item(j));
                    }else if(nodeList.item(j).getNodeName().contains("wasGeneratedBy")){
                        createWasGeneratedBy(nodeList.item(j));
                    }else if(nodeList.item(j).getNodeName().contains("wasControlledBy")){
                        createWasControlledBy(nodeList.item(j));
                    }
                }
            }
        }

        newDocument += "</prov:document>";
        try{
            java.io.FileWriter fw = new java.io.FileWriter("/home/erkan/Desktop/result.xml");
            fw.write(newDocument);
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Result: " + newDocument);
    }

    public static void createWasControlledBy(Node node){

    }

    public static void createWasGeneratedBy(Node node){
        String id1      = getEffect(node);
        String id2      = getCause(node);
        String start    = getStart(node);
        String end      = getEnd(node);
        String role     = getRole(node);
        String others   = getOtherAttributes(node);

        String entity   = new String();
        String activity = new String();

//        if(!entityNodes.contains(id1)){
//            entityNodes.add(id1);
            entity = createEntity(id1, role);
//        }

//        if(!activityNodes.contains(id2)){
//            activityNodes.add(id2);
            activity = createActivity(start, end, id2);
//        }

        String result =  entity + activity + "<prov:wasGeneratedBy>\n" +
                "\t<prov:entity prov:ref=\"" + id1 + "\"/>\n" +
                "\t<prov:activity prov:ref=\"" + id2 + "\"/>\n" +
                "\t<prov:role>" + role + "</prov:role>\n" + others +
                "</prov:wasGeneratedBy>\n";

        newDocument += result;
    }

    public static void createWasDerivedFrom(Node node){
        String id1      = getEffect(node);
        String id2      = getCause(node);
        String others   = getOtherAttributes(node);

        String entity1  = new String();
        String entity2  = new String();

//        if(!entityNodes.contains(id1)){
//            entityNodes.add(id1);
            entity1 = createEntity(id1, null);
//        }

//        if(!entityNodes.contains(id2)){
//            entityNodes.add(id2);
            entity2 = createEntity(id2, null);
//        }

        String result = entity1 + entity2 + "<prov:wasDerivedFrom>\n" +
                "\t<prov:generatedEntity prov:ref=\"" + id1 + "\"/>\n" +
                "\t<prov:usedEntity prov:ref=\"" + id2 + "\"/>\n" + others +
                "</prov:wasDerivedFrom>\n";

        newDocument += result;
    }

    public static void createUsed(Node node){
        String id1      = getEffect(node);
        String id2      = getCause(node);
        String start    = getStart(node);
        String end      = getEnd(node);
        String role     = getRole(node);
        String others   = getOtherAttributes(node);

        String activity = new String();
        String entity   = new String();

//        if(!activityNodes.contains(id1)){
            activity = createActivity(start, end, id1);
//            activityNodes.add(id1);
//        }
//        if(!entityNodes.contains(id2)){
            entity = createEntity(id1, role);
//            entityNodes.add(id2);
//        }

        String result = activity + entity + "<prov:used>\n" +
                "\t<prov:activity prov:ref=\"" + id1 + "\"/>\n" +
                "\t<prov:entity prov:ref=\"" + id2 + "\"/>\n" + others +
                "</prov:used>\n";

        newDocument += result;
    }

    public static String createEntity(String id, String roleValue){
        return "<prov:entity prov:id=\"" + id + "\">\n" +
                ((roleValue != null) ? "\t<prov:value>" + roleValue + "</prov:value>\n" : "")+
                "</prov:entity>\n";
    }

    public static void createWasInformedBy(Node node){
        String id1      = getEffect(node);
        String id2      = getCause(node);
        String start    = getStart(node);
        String end      = getEnd(node);
        String others   = getOtherAttributes(node);
        String act1     = new String();
        String act2     = new String();

//        if(!activityNodes.contains(id1)){
//            activityNodes.add(id1);
            act1 = createActivity(start, end, id1);
//        }
//        if(!activityNodes.contains(id2)){
//            activityNodes.add(id2);
            act2 = createActivity(start, end, id2);
//        }

        String result =
                act1 + act2 + "<prov:wasInformedBy>\n" +
                        "\t<prov:informed prov:ref=\"" + id1 +  "\"/>\n" +
                        "\t<prov:informant prov:ref=\"" + id2 + "\"/>\n" +
                        others + "</prov:wasInformedBy>\n";


        newDocument += result;
    }

    public static String createActivity(String start, String end, String id){
        return "<prov:activity prov:id=\"" + id + "\">\n" +
                ((start == null || end == null) ? "" : "\t<prov:startTime>" + start + "</prov:startTime>\n" +
                "\t<prov:endTime>" + end + "</prov:endTime>\n") +
                "</prov:activity>\n";
    }

    public static String createAgent(String id){
        return "<prov:agent prov:id=\"" + id + "\"/>\n";
    }

    public static void createWasAssociatedWith(Node node){
        String effect   = getEffect(node);
        String cause    = getCause(node);
        String start    = getStart(node);
        String end      = getEnd(node);
        String agent    = new String();
        String activity = new String();
        String others   = getOtherAttributes(node);

//        if(!agentNodes.contains(cause)){
//            agentNodes.add(cause);
            agent = createAgent(cause);
//        }
//
//        if(!activityNodes.contains(effect)){
//            activityNodes.add(effect);
            activity = createActivity(start, end, effect);
//        }

        String result = agent + activity + "<prov:wasAssociatedWith>\n" +
                "\t<prov:activity prov:ref=\"" + effect + "\"/>\n" +
                "\t<prov:agent prov:ref=\"" + cause + "\"/>\n" + others +
                "</prov:wasAssociatedWith>\n";

        newDocument += result;
    }

    public static String getOtherAttributes(Node node){
        String start = new String();
        for(int t=0; t<node.getChildNodes().getLength(); t++){
            if(!node.getChildNodes().item(t).getNodeName().contains("time") && !node.getChildNodes().item(t).getNodeName().contains("role")
                    && node.getChildNodes().item(t).getNodeName().contains("effect") && node.getChildNodes().item(t).getNodeName().contains("cause")){
                start += "\t<prov:" + node.getChildNodes().item(t).getNodeName() + ">" + node.getChildNodes().item(t).getAttributes().item(0).getNodeValue()
                    + "</prov:" + node.getChildNodes().item(t).getNodeName() + ">\n";
                break;
            }
        }

        return start;
    }

    public static String getStart(Node node){
        String start = null;
        for(int t=0; t<node.getChildNodes().getLength(); t++){
            if(node.getChildNodes().item(t).getNodeName().contains("time")){
                start = node.getChildNodes().item(t).getAttributes().item(0).getNodeValue();
                break;
            }
        }

        return start;
    }

    public static String getRole(Node node){
        String role = null;
        for(int t=0; t<node.getChildNodes().getLength(); t++){
            if(node.getChildNodes().item(t).getNodeName().contains("role")){
                role = node.getChildNodes().item(t).getAttributes().item(0).getNodeValue();
                break;
            }
        }

        return role;
    }

    public static String getEnd(Node node){
        String end = null;
        for(int t=0; t<node.getChildNodes().getLength(); t++){
            if(node.getChildNodes().item(t).getNodeName().contains("time")){
                end = node.getChildNodes().item(t).getAttributes().item(1).getNodeValue();
                break;
            }
        }

        return end;
    }

    public static String getEffect(Node node){
        String effect = null;
        for(int t=0; t<node.getChildNodes().getLength(); t++){
            if(node.getChildNodes().item(t).getNodeName().contains("effect")){
                effect = node.getChildNodes().item(t).getAttributes().item(0).getNodeValue();
                break;
            }
        }

        return effect;
    }

    public static String getCause(Node node){
        String cause = null;
        for(int t=0; t<node.getChildNodes().getLength(); t++){
            if(node.getChildNodes().item(t).getNodeName().contains("cause")){
                cause = node.getChildNodes().item(t).getAttributes().item(0).getNodeValue();
                break;
            }
        }

        return cause;
    }

}

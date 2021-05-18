import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;


public class MyClass {

    public static String structuralElementName;
    public static String structuralElementIndex;
    public static Hashtable<String,String> conceptElements = new Hashtable<String,String>();
    public static Hashtable<String,String> childElements = new Hashtable<String,String>();
    public static Hashtable<String,String> propertyElements = new Hashtable<String,String>();

    // Defines a factory API that enables applications to obtain a parser that produces DOM object trees from XML documents.
    public static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    // Defines the API to obtain DOM Document instances from an XML document.
    public static DocumentBuilder documentBuilder;
    // The Document interface represents the entire HTML or XML document.
    public static Document eclipseEcoreXML;
    public static Document MPSEcoreXML;


    public static Node analyzeNode(Node concept) throws TransformerException {
        Element element=null;
        if(conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EPackage")){
            element = eclipseEcoreXML.createElement("ecore:EPackage");
            element.setAttributeNS("http://www.omg.org/XMI","xmi:version","2.0");
            element.setAttribute("xmlns:ecore","http://www.eclipse.org/emf/2002/Ecore");
            element.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
//            System.out.println(MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(3)
//                    .getChildNodes().item(3).getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue());
            element.setAttribute("name",concept.getChildNodes().item(1).getAttributes().getNamedItem("value").getNodeValue());
            element.setAttribute("nsURI",concept.getChildNodes().item(3).getAttributes().getNamedItem("value").getNodeValue());
            element.setAttribute("nsPrefix",concept.getChildNodes().item(5).getAttributes().getNamedItem("value").getNodeValue());
            for (int i=1;i<concept.getChildNodes().getLength();i=i+2){
                if(concept.getChildNodes().item(i).getNodeName().equals("property")){
                    continue;
                }
//                System.out.println(concept.getChildNodes().item(i).getAttributes().getNamedItem("concept"));
//                System.out.println(conceptElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("concept").getNodeValue()));
                if(conceptElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("concept").getNodeValue())!=null)
                {
                    try{
                        element.appendChild(analyzeNode(concept.getChildNodes().item(i)));
                    } catch (NullPointerException e) {
                        System.out.println("null Pointer");
                    }
                }
            }
            // If the newChild is already in the tree, it is first removed.
            eclipseEcoreXML.appendChild(element);
            return element;
        }
        else if (conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EEnum"))
        {
            element = eclipseEcoreXML.createElement("eClassifiers");
            element.setAttribute("xsi:type","ecore:EEnum");
            for( int i=1;i <concept.getChildNodes().getLength();i=i+2)
            {
                if(concept.getChildNodes().item(i).getNodeName().equals("node")){
                    try{
                        element.appendChild(analyzeNode(concept.getChildNodes().item(i)));
                    } catch (NullPointerException e) {
                        System.out.println("null Pointer");
                        continue;
                    }
                }
                else if (propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()).equals("name")){
                    element.setAttribute("name",concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue());
                }
            }
            return element;
        }
        else if (conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EEnumLiteral"))
        {
            element = eclipseEcoreXML.createElement("eLiterals");
            for( int i=1;i <concept.getChildNodes().getLength();i=i+2)
            {
                element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue());
            }
            return element;
        }
        else if (conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EClass"))
        {
            element = eclipseEcoreXML.createElement("eClassifiers");
            element.setAttribute("xsi:type","ecore:EClass");
            for( int i=1;i <concept.getChildNodes().getLength();i=i+2)
            {
                if(concept.getChildNodes().item(i).getNodeName().equals("node")){
                    try{
                        element.appendChild(analyzeNode(concept.getChildNodes().item(i)));
                    } catch (NullPointerException e) {
                        System.out.println("null Pointer");
                        continue;
                    }
                }
                else{
                element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue());
            }}
            return element;
        }
        else if (conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EReference"))
        {
            element = eclipseEcoreXML.createElement("eStructuralFeatures");
            element.setAttribute("xsi:type","ecore:EReference");
            for( int i=1;i <concept.getChildNodes().getLength();i=i+2)
            {
                if(concept.getChildNodes().item(i).getNodeName().equals("node")){
                    try{
                        element.appendChild(analyzeNode(concept.getChildNodes().item(i)));
                    } catch (NullPointerException e) {
                        System.out.println("null Pointer");
                        continue;
                    }
                }
                else if (concept.getChildNodes().item(i).getNodeName().equals("ref"))
                {
                    element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                }
                else{ element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue()); }
            }
            return element;
        }
        else if (conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EAttribute"))
        {
            element = eclipseEcoreXML.createElement("eStructuralFeatures");
            element.setAttribute("xsi:type","ecore:EAttribute");
            for( int i=1;i <concept.getChildNodes().getLength();i=i+2)
            {
                if(concept.getChildNodes().item(i).getNodeName().equals("node")){
                    try{
                        element.appendChild(analyzeNode(concept.getChildNodes().item(i)));
                        System.out.println(element);
                    } catch (NullPointerException e) {
                        System.out.println("null Pointer");
                        continue;
                    }
                }
                else if (concept.getChildNodes().item(i).getNodeName().equals("ref"))
                {
                    if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EInt"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                }
                else{
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue());
                }
            }
            return element;
        }
        return element;

    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        File file = new File("src/main/resources/EcoreLanguage.sanbox.mps");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        MPSEcoreXML = db.parse(file);
        MPSEcoreXML.getDocumentElement().normalize();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        eclipseEcoreXML = documentBuilder.newDocument();
        //Mapping concepts with their respective n mes
        for( int i=1; i < MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().getLength();i+=2) {
//            System.out.println(document.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getNodeName());
            for (int j=1;j<MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getChildNodes().getLength();j+=2){
                structuralElementName = MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getChildNodes().item(j).getAttributes().getNamedItem("name").getNodeValue();
//                System.out.println(MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getChildNodes().item(j).getAttributes().getNamedItem("name").getNodeValue());
                structuralElementIndex = MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getChildNodes().item(j).getAttributes().getNamedItem("index").getNodeValue();
                if (MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getChildNodes().item(j).getNodeName().equals("property")||MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getChildNodes().item(j).getNodeName().equals("reference")){

                    propertyElements.put(structuralElementIndex, structuralElementName);

                }
                else if (MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getChildNodes().item(j).getNodeName().equals("child"))
                {
                    childElements.put(structuralElementIndex, structuralElementName);
                }
            }

            structuralElementName = MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getAttributes().getNamedItem("name").getNodeValue();
            structuralElementIndex = MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(1).getChildNodes().item(i).getAttributes().getNamedItem("index").getNodeValue();

            String[] letstry = structuralElementName.split("EcoreLanguage.structure.");
            conceptElements.put(structuralElementIndex, letstry[1]);
        }
        for( int i=1; i < MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(3).getChildNodes().getLength();i+=2) {
            for (int j=1;j<MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(3).getChildNodes().item(i).getChildNodes().getLength();j+=2){
                structuralElementName = MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(3).getChildNodes().item(i).getChildNodes().item(j).getAttributes().getNamedItem("name").getNodeValue();
                structuralElementIndex = MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(3).getChildNodes().item(i).getChildNodes().item(j).getAttributes().getNamedItem("index").getNodeValue();
                propertyElements.put(structuralElementIndex, structuralElementName);

            }
        }

        for( int i=1; i < MPSEcoreXML.getDocumentElement().getChildNodes().getLength();i+=2) {
            if(MPSEcoreXML.getDocumentElement().getChildNodes().item(i).getAttributes().getNamedItem("concept")==null)
            {
                continue;
            }
            Node MPSConcept= MPSEcoreXML.getDocumentElement().getChildNodes().item(i);
            if(conceptElements.get(MPSConcept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EFactory"))
            {
                if(conceptElements.get(MPSConcept.getChildNodes().item(1).getAttributes().getNamedItem("concept").getNodeValue()).equals("EPackage"))
                {
                    analyzeNode(MPSConcept.getChildNodes().item(1));
                    // append child elements to root element
                    // An instance of this abstract class can transform a source tree into a result tree.
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                    // Acts as a holder for a transformation Source tree in the form of a Document Object Model (DOM) tree.
                    DOMSource source = new DOMSource(eclipseEcoreXML);

                    // Acts as an holder for a transformation result, which may be XML, plain Text, HTML, or some other form of markup.
//                    StreamResult console = new StreamResult(System.out);
                    StreamResult console = new StreamResult(new File("tryNewFile.ecore"));
                    transformer.transform(source, console);
                }
            }
        }

    }
}
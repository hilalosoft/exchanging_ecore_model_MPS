import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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
    public static String newFileName="";


    //Recursive function that takes a Node parameter and explores it's children
    public static Node analyzeNode(Node concept) {
        Element element=null;
        if(conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EPackage")){
            element = eclipseEcoreXML.createElement("ecore:EPackage");
            element.setAttributeNS("http://www.omg.org/XMI","xmi:version","2.0");
            element.setAttribute("xmlns:ecore","http://www.eclipse.org/emf/2002/Ecore");
            element.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
//            System.out.println(MPSEcoreXML.getDocumentElement().getChildNodes().item(7).getChildNodes().item(3)
//                    .getChildNodes().item(3).getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue());
            for(int i=1;i<6;i=i+2) {
                element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()), concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue());
                if(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()).equals("name"))
                {
                    newFileName=concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue();
                }
            }
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
                        System.out.println("null Pointer EPackage");
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
                        System.out.println("null Pointer eClassifier");
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
                if(concept.getChildNodes().item(i).getNodeName().equals("node") && conceptElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("concept").getNodeValue()).equals("EClassSuperType")){
                    for(int j=1;j<concept.getChildNodes().item(i).getChildNodes().getLength();j=j+2){
                        element.setAttribute("eSuperTypes","#//"+concept.getChildNodes().item(i).getChildNodes().item(j).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                }
                else  if(concept.getChildNodes().item(i).getNodeName().equals("node")){
                    try{
                        element.appendChild(analyzeNode(concept.getChildNodes().item(i)));
                    } catch (NullPointerException e) {
                        System.out.println("null Pointer EClass");
                        System.out.println(concept.getAttributes().getNamedItem("concept").getNodeValue()+ i);
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
                        System.out.println("null Pointer EReference");
                        continue;
                    }
                }
                else if (concept.getChildNodes().item(i).getNodeName().equals("ref"))
                {
                    if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EInt"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                }
                else{ element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue()); }
            }
            return element;
        }
//        EClassSuperType
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
                        System.out.println("null Pointer EAttribute");
                        continue;
                    }
                }
                else if (concept.getChildNodes().item(i).getNodeName().equals("ref"))
                {
                    if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EInt")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EInt"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EDouble")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EDouble"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EShort")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EShort"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EBoolean")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EBoolean"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EBigDecimal")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EBigDecimal"))
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
        else if (conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EAnnotation"))
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
                        System.out.println("null Pointer EAnnotation");
                        continue;
                    }
                }
                else if (concept.getChildNodes().item(i).getNodeName().equals("ref"))
                {
                    if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EInt"))
                    {
                        element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//"+concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue());
                    }
                    else if(concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString")||concept.getChildNodes().item(i).getAttributes().getNamedItem("resolve").getNodeValue().equals("EString"))
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
        else if (conceptElements.get(concept.getAttributes().getNamedItem("concept").getNodeValue()).equals("EOperation"))
        {
            element = eclipseEcoreXML.createElement("eOperations");
            for( int i=1;i <concept.getChildNodes().getLength();i=i+2)
            {
                if(concept.getChildNodes().item(i).getNodeName().equals("node")){
                    try{
                        element.appendChild(analyzeNode(concept.getChildNodes().item(i)));
                        System.out.println(element);
                    } catch (NullPointerException e) {
                        System.out.println("null Pointer EAnnotation");
                        continue;
                    }
                }
                element.setAttribute(propertyElements.get(concept.getChildNodes().item(i).getAttributes().getNamedItem("role").getNodeValue()),concept.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue());

            }
            return element;
        }

        return element;

    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            public String getDescription() {
                return "MPS Ecore files (*.mps)";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith(".mps");
                }
            }
        });
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        File file;
        if (result == JFileChooser.APPROVE_OPTION) {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
        }
        else{
            return;
        }
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
                    StreamResult console = new StreamResult(new File(newFileName+".ecore"));
                    System.out.println("Metamodel Generated for EMF :"+newFileName+".ecore");
                    transformer.transform(source, console);
                }
            }
        }

    }
}
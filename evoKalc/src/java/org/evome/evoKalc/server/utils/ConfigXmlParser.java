/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.server.utils;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import org.evome.evoKalc.client.shared.*;


/**
 *
 * @author nekoko
 */
public class ConfigXmlParser {
    
    private DocumentBuilder builder;
    private Document doc;
    private XPath xpath;
    
    public ConfigXmlParser(File file){
        //create document builder
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ConfigXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        //create XPath
        XPathFactory xpfactory = XPathFactory.newInstance();
        xpath = xpfactory.newXPath();
        
        //parse the xml file
        try{
            doc = builder.parse(file);
        }catch(SAXException ex){
            Logger.getLogger(ConfigXmlParser.class.getName()).log(Level.SEVERE, null, ex);            
        }catch(IOException ex){
            Logger.getLogger(ConfigXmlParser.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
    }

}

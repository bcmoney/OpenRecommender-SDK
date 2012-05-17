/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openrecommender;

import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * OpenReceommenderSAX
 *   SAX Parser for OpenRecommender XML format. SAX tends to perform better when
 *   parsing really large XML Documents.
 *
 * @author bcopeland
 */
public class OpenRecommenderSAX {

    public String filePath;	
    public String fileFormat;
    public String fileString = "";

    public OpenRecommenderSAX() {
        this.filePath = "recommendations.xml";
        this.fileFormat = "xml";
    }

    public OpenRecommenderSAX(String filePath) {
        this.filePath = filePath;
        this.fileFormat = "xml";
    }

    public OpenRecommenderSAX(String filePath, String fileFormat) {
        this.filePath = filePath;
        this.fileFormat = fileFormat;
    }	       
    
    SAXParserFactory factory = SAXParserFactory.newInstance();

    DefaultHandler handler = new DefaultHandler() {

        boolean title = false;
        boolean image = false;
        boolean link = false;
        boolean description = false;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {                                       
        // Process each child Element of this Element
            System.out.println("Start Element :" + qName);
            if (qName.equalsIgnoreCase("TITLE")) {
                title = true;
                getAttributes(attributes);
            }
            if (qName.equalsIgnoreCase("IMAGE")) {
                image = true;
                getAttributes(attributes);
            }
            if (qName.equalsIgnoreCase("LINK")) {
                link = true;
                getAttributes(attributes);
            }
            if (qName.equalsIgnoreCase("DESCRIPTION")) {
                description = true;
                getAttributes(attributes);
            }
        }

        @Override     
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.println("End Element :" + qName);
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            System.out.println(new String(ch, start, length));
            if (title) {
                System.out.println("TITLE: " + new String(ch, start, length));
                title = false;
            }
            if (image) {
                System.out.println("IMAGE: " + new String(ch, start, length));
                image = false;
            }
            if (link) {
                System.out.println("LINK: " + new String(ch, start, length));
                link = false;
            }
            if (description) {
                System.out.println("DESC: " + new String(ch, start, length));
                description = false;
            }		  
        }

        public String[] getAttributes(Attributes attributes) {
            String[] attrValues = {};
            String name = null;
            // Process each attribute of this Element
            for (int i=0; i<attributes.getLength(); i++) {                 
                name = attributes.getQName(i); //Attribute's name
                attrValues[i] = attributes.getValue(i); //Attribute's value
                /* The following methods are valid only if the parser is namespace-aware               
                String nsUri = attributes.getURI(i); //The uri of the attribute's namespace
                String lName = attributes.getLocalName(i); //This is the name without the prefix             
                */
            }
            return attrValues;
        }

        public void display() {
            try {
                SAXParser saxParser = factory.newSAXParser();            
                File file = new File(filePath);
                if (file.exists()) {
                    InputStream inputStream = new FileInputStream(file);
                    Reader reader = new InputStreamReader(inputStream,"UTF-8");
                    InputSource is = new InputSource(reader);
                    is.setEncoding("UTF-8");
                    saxParser.parse(is, handler);
                }
                else {
                    System.out.println("File not found!");
                }
            }
            catch(ParserConfigurationException pEx) {
                pEx.printStackTrace();
            }                    
            catch(FileNotFoundException fEx) {
                fEx.printStackTrace();
            }
            catch(IOException ioEx) {
                ioEx.printStackTrace();
            }
            catch(SAXException saxEx) {
                saxEx.printStackTrace();
            }
        }
    };

    
}

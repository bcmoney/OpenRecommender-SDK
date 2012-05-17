package org.openrecommender;

import java.io.*;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;


/**
 * OpenRecommender.java
 *   DOM Parser for the OpenRecommender XML format.
 *  (To be used by Applet, Servlet, JSP, GUI and other Classes)
 * @version 1.0
 * @author bcmoney
 * @date 2011-11-11
 */
public class OpenRecommender {

    public String filePath;	
    public String fileFormat;
    public String fileString = "";

    public OpenRecommender() {
        this.filePath = "recommendations.xml";
        this.fileFormat = "xml";
    }

    public OpenRecommender(String filePath) {
        this.filePath = filePath;
        this.fileFormat = "xml";
    }

    public OpenRecommender(String filePath, String fileFormat) {
        this.filePath = filePath;
        this.fileFormat = fileFormat;
    }	                        
        
    /*
     * load
     *   Loads an XML file or Web Service endpoint.
     * REMOTE USAGE: 
     *   OpenRecommender.load("http://www.openrecommender.org/schema/XML/recommendations.xml");
     *   -OR-
     * LOCAL USAGE: 
     *   OpenRecommender.load("file:///C:/Users/bcopeland/Documents/NetBeansProjects/OpenRecommenderClient/web/WEB-INF/recommendations.xml");
     * @param url String  location of the file or WebService endpoint to parse   
     * @return Document
     */
    public Document load(String url) throws Exception {
        String theUrl = (url != null && !url.equals("")) ? url : this.filePath;        
        File xmlFile = new File(this.filePath);        
        // if the xml file has not been cached, try to cache it from server
        if (!xmlFile.exists()) {           
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(theUrl).openStream()));
            FileWriter fstream = new FileWriter(xmlFile);
            BufferedWriter out = new BufferedWriter(fstream);                
            while(in.ready()) {
                out.write(in.readLine());
            }                
            in.close();
            out.close();
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        return doc;  //DEBUG: System.out.println("Root element: " + doc.getDocumentElement().getNodeName());		
    }

     
   /* 
    * toString
    *   Output all elements and attributes (useful for debugging)
    * @param doc  Document Object Model (DOM) document
    */      
    public void toString(Document doc) {
        NodeList recommendationList = getRecommendations(doc);
        for (int i = 0; i < recommendationList.getLength(); i++) {
            Node nNode = recommendationList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                System.out.println("\n--" + element.getTagName());
                if (nNode instanceof Element && nNode.hasAttributes()) {//if Element has any Attributes
                    NamedNodeMap attrs = nNode.getAttributes();
                    for (int j = 0; j < attrs.getLength(); j++) {
                    Attr attribute = (Attr) attrs.item(j);
                    System.out.println("  "+attribute.getName() + ": " + attribute.getValue());
                    }
                }                  
                if (element.hasChildNodes()) {
                    NodeList rec = element.getChildNodes();                      
                    for (int j = 0; j < rec.getLength(); j++) {                          
                    Node childNode = rec.item(j);
                    if (childNode.getNodeType() != Node.TEXT_NODE) {
                        System.out.println("----"+childNode.getNodeName()+" "+childNode.getTextContent());
                    }
                    if (childNode instanceof Element && childNode.hasAttributes()) {//if Element has any Attributes
                        NamedNodeMap attrs = childNode.getAttributes();
                        for (int k = 0; k < attrs.getLength(); k++) {
                            Attr attribute = (Attr) attrs.item(k);
                            System.out.println("    "+attribute.getName() + ": " + attribute.getValue());
                        }
                    }                        
                    }
                }
            }
        }		
    }
    
   /* 
    * toHtmlString
    *   Output all elements and attributes (good for simple web-client usage)
    * @param doc  Document Object Model (DOM) document
    * @return String  recommendationsHTML
    */      
    public String toHtmlString(Document doc) {                
        NodeList recommendations = getRecommendations(doc);
        String recommendationsHTML = "";
        for (int i = 0; i < recommendations.getLength(); i++) {
            Element recommendation = getRecommendation(recommendations, i);
            recommendationsHTML += "<li><a id='recommendation_" + getRecommendationIdentifier(recommendation) +
                "' href='" + getRecommendationLink(recommendation) + 
                "' title='" + getRecommendationTitle(recommendation) + "'>" +            
                "<img src='" + getRecommendationImage(recommendation) + "' />" + 
                "<span>" + getRecommendationDescription(recommendation) + "</span></a></li>";
        }            
        return recommendationsHTML;
    }

    /* get value of any tag, by Element name (String) */
    private String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }       


    public NodeList getRecommendations(Document doc) {	
        return doc.getElementsByTagName("recommendation");
    }
    
    public Element getRecommendation(NodeList recommendations, int index) {
        Node rec = recommendations.item(index);
        return (Element)rec;
    }    

    /* ELEMENTS (text/data between tags) */
    public String getRecommendationTitle(Element recommendation) {
        return (String) getTagValue("title", recommendation);
    }

    public String getRecommendationImage(Element recommendation) {
        return (String) getTagValue("image", recommendation);
    }

    public String getRecommendationLink(Element recommendation) {
        return (String) getTagValue("link", recommendation);
    }

    public String getRecommendationDescription(Element recommendation) {
        return (String) getTagValue("description", recommendation);
    }        


    /* <RECOMMENDATION> Attributes */           
    public String getRecommendationIdentifier(Element recommendation) {
        return recommendation.getAttribute("identifier");
    }

    public String getRecommendationSender(Element recommendation) {
        return recommendation.getAttribute("sender");
    }

    public String getRecommendationReceiver(Element recommendation) {
        return recommendation.getAttribute("receiver");
    }

    public String getRecommendationDate(Element recommendation) {
        return recommendation.getAttribute("date");
    }

    public String getRecommendationTime(Element recommendation) {
        return recommendation.getAttribute("time");
    }

    public String getRecommendationPriority(Element recommendation) {
        return recommendation.getAttribute("priority");
    }

    public String getRecommendationType(Element recommendation) {
        return recommendation.getAttribute("type");
    }            


    /* <TITLE> Attributes */
    public String getRecommendationTitleDuration(Element title) {
        return title.getAttribute("duration");
    }

    public String getRecommendationTitleLanguage(Element title) {
        return title.getAttribute("language");
    }

    public String getRecommendationTitleCaptions(Element title) {
        return title.getAttribute("captions");
    }

    public String getRecommendationTitleSubtitles(Element title) {
        return title.getAttribute("subtitles");
    }

    public String getRecommendationTitlePlaylist(Element title) {
        return title.getAttribute("playlist");
    }           


    /* <IMAGE> Attributes */
    public String getRecommendationImageCreator(Element image) {
        return image.getAttribute("creator");
    }

    public String getRecommendationImagePublisher(Element image) {
        return image.getAttribute("publisher");
    }		

    public String getRecommendationImageFormat(Element image) {
        return image.getAttribute("format");
    }

    public String getRecommendationImageRights(Element image) {
        return image.getAttribute("rights");
    }

    public String getRecommendationImageRightsHolder(Element image) {
        return image.getAttribute("rightsHolder");
    }


    /* <LINK> Attributes */
    public String getRecommendationLinkShortlink(Element link) {
        return link.getAttribute("shortlink");
    }

    public String getRecommendationLinkLocation(Element link) {
        return link.getAttribute("location");
    }

    public String getRecommendationLinkMobile(Element link) {
        return link.getAttribute("mobile");
    }

    public String getRecommendationLinkMobiledownload(Element link) {
        return link.getAttribute("mobiledownload");
    }

    public String getRecommendationLinkMobilestream(Element link) {
        return link.getAttribute("mboilestream");
    }

    public String getRecommendationLinkCreator(Element link) {
        return link.getAttribute("creator");
    }

    public String getRecommendationLinkPublisher(Element link) {
        return link.getAttribute("publisher");
    }

    public String getRecommendationLinkFormat(Element link) {
        return link.getAttribute("format");
    }

    public String getRecommendationLinkRights(Element link) {
        return link.getAttribute("rights");
    }

    public String getRecommendationLinkRightsHolder(Element link) {
        return link.getAttribute("rightsHolder");
    }

    public String getRecommendationLinkOembed(Element link) {
            return link.getAttribute("oembed");
    }


    /* <DESCRIPTION> Attributes */
    public String getRecommendationDescriptionRating(Element description) {
            return description.getAttribute("rating");
    }

    public String getRecommendationDescriptionReview(Element description) {
            return description.getAttribute("review");
    }

    public String getRecommendationDescriptionSubject(Element description) {
            return description.getAttribute("subject");
    }        
}       
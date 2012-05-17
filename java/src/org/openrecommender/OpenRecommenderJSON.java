package org.openrecommender;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * OpenRecommenderJSON 
 *   JSON-simple parser for OpenRecommender JSON Format
 * @author bcopeland
 */
public class OpenRecommenderJSON {

    public Object json = null;
    public JSONObject jsonObject = null; 

    public String filePath;	
    public String fileFormat;
    public String fileString = "";

    public OpenRecommenderJSON() {
        this.filePath = "recommendations.xml";
        this.fileFormat = "xml";
    }

    public OpenRecommenderJSON(String filePath) {
        this.filePath = filePath;
        this.fileFormat = "xml";
    }

    public OpenRecommenderJSON(String filePath, String fileFormat) {
        this.filePath = filePath;
        this.fileFormat = fileFormat;
    }
    
    /*
     * load
     *   Loads a JSON file or Web Service endpoint.
     * REMOTE USAGE: 
     *   OpenRecommender.load("http://www.openrecommender.org/schema/JSON/recommendations.json");
     *   -OR-
     * LOCAL USAGE: 
     *   OpenRecommender.load("file:///C:/xampp/htdocs/OpenRecommender/schema/JSON/recommendations.json");
     * @param url String  location of the file or WebService endpoint to parse   
     * @return Document
     */
    public String load(String url) {
        String theUrl = (url != null && !url.equals("")) ? url : this.filePath;
        JSONParser parser = new JSONParser();        
        String jsonText = null;
        try {
            json = parser.parse(new FileReader(theUrl));	             
            jsonObject = (JSONObject)json;
            jsonText = jsonObject.toJSONString();
        }
        catch(IOException ioEx) {
            ioEx.printStackTrace();
        }
        catch(org.json.simple.parser.ParseException pEx) {
            pEx.printStackTrace();
        }
        return jsonText;
    }

    public JSONObject getRecommendations() throws FileNotFoundException, IOException, ParseException {
        return (JSONObject)jsonObject.get("recommendations");
    }

    public JSONArray getRecommendation(JSONObject recommendations) {
        return (JSONArray)recommendations.get("recommendation");        
    }
    
    public String getRecommendationIdentifier(JSONObject recommendation) {
        JSONObject r = (JSONObject)recommendation.get("recommendation");
        return (String)r.get("identifier");
    }

    // TITLE
    public JSONArray getRecommendationTitles(JSONObject recommendations) {
        return (JSONArray)recommendations.get("title");        
    }    
    public JSONObject getRecommendationTitle(JSONArray recommendation, int index) {
        JSONObject title = (JSONObject)recommendation.get(index);
        return (JSONObject)title.get("title");        
    }
    public String getRecommendationTitleData(JSONObject title) {
        return (String)title.get("data");
    }
    public String getRecommendationTitleDuration(JSONObject title) {
        return (String)title.get("duration");
    }
    public String getRecommendationTitleCaptions(JSONObject title) {
        return (String)title.get("captions");
    }
    public String getRecommendationTitleSubtitles(JSONObject title) {
        return (String)title.get("subtitles");
    }
    public String getRecommendationTitlePlaylist(JSONObject title) {
        return (String)title.get("playlist");
    }
    public String getRecommendationTitleLanguage(JSONObject title) {
        return (String)title.get("language");
    }

    // IMAGE
    public JSONArray getRecommendationImages(JSONObject recommendations) {
        return (JSONArray)recommendations.get("title");        
    }
    public JSONObject getRecommendationImage(JSONArray recommendation, int index) {
        JSONObject image = (JSONObject)recommendation.get(index);
        return (JSONObject)image.get("image");        
    }
    public String getRecommendationImageData(JSONObject image) {
        return (String)image.get("data");
    }
    public String getRecommendationImageCamera(JSONObject image) {
        return (String)image.get("camera");
    }
    public String getRecommendationImageThumb(JSONObject image) {
        return (String)image.get("thumb");
    }
    public String getRecommendationImageGallery(JSONObject image) {
        return (String)image.get("gallery");
    }
    public String getRecommendationImageFormat(JSONObject image) {
        return (String)image.get("format");
    }
    public String getRecommendationImageCreator(JSONObject image) {
        return (String)image.get("creator");
    }
    public String getRecommendationImageDate(JSONObject image) {
        return (String)image.get("date");
    }
    public String getRecommendationImageTime(JSONObject image) {
        return (String)image.get("time");
    }
    public String getRecommendationImagePublisher(JSONObject image) {
        return (String)image.get("publisher");
    }
    public String getRecommendationImageRights(JSONObject image) {
        return (String)image.get("rights");
    }
    public String getRecommendationImageRightsHolder(JSONObject image) {
        return (String)image.get("rightsHolder");
    }
    
    // LINK
    public JSONArray getRecommendationLinks(JSONObject recommendations) {
        return (JSONArray)recommendations.get("link");        
    }    
    public JSONObject getRecommendationLink(JSONArray recommendation, int index) {
        JSONObject link = (JSONObject)recommendation.get(index);
        return (JSONObject)link.get("link");        
    }
    public String getRecommendationLinkData(JSONObject link) {
        return (String)link.get("data");
    }
    public String getRecommendationLinkProxy(JSONObject link) {
        return (String)link.get("proxy");
    }
    public String getRecommendationLinkDownload(JSONObject link) {
        return (String)link.get("download");
    }
    public String getRecommendationLinkOembed(JSONObject link) {
        return (String)link.get("oembed");
    }
    public String getRecommendationLinkMobiledownload(JSONObject link) {
        return (String)link.get("mobiledownload");
    }
    public String getRecommendationLinkMobilestream(JSONObject link) {
        return (String)link.get("mobilestream");
    }
    public String getRecommendationLinkFormat(JSONObject link) {
        return (String)link.get("format");
    }
    public String getRecommendationLinkCreator(JSONObject link) {
        return (String)link.get("creator");
    }
    public String getRecommendationLinkDate(JSONObject link) {
        return (String)link.get("date");
    }
    public String getRecommendationLinkTime(JSONObject link) {
        return (String)link.get("time");
    }
    public String getRecommendationLinkPublisher(JSONObject link) {
        return (String)link.get("publisher");
    }
    public String getRecommendationLinkRights(JSONObject link) {
        return (String)link.get("rights");
    }
    public String getRecommendationLinkRightsHolder(JSONObject link) {
        return (String)link.get("rightsholder");
    }
  
    //DESCRIPTION (optional)
    public JSONArray getRecommendationDescriptions(JSONObject recommendations) {
        return (JSONArray)recommendations.get("description");        
    }
    public JSONObject getRecommendationDescription(JSONArray recommendation, int index) {
        JSONObject description = (JSONObject)recommendation.get(index);
        return (JSONObject)description.get("description");        
    }
    public String getRecommendationDescriptionData(JSONObject description) {
        return (String)description.get("data");
    }
    public String getRecommendationDescriptionRating(JSONObject description) {
        return (String)description.get("rating");
    }
    public String getRecommendationDescriptionReview(JSONObject description) {
        return (String)description.get("review");
    }
    public String getRecommendationDescriptionLanguage(JSONObject description) {
        return (String)description.get("language");
    }
    public String getRecommendationDescriptionCreator(JSONObject description) {
        return (String)description.get("creator");
    }
    public String getRecommendationDescriptionDate(JSONObject description) {
        return (String)description.get("date");
    }
    public String getRecommendationDescriptionTime(JSONObject description) {
        return (String)description.get("time");
    }    

}

using System;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.ServiceModel.Web;
using System.Text;
using System.Xml;
using System.Xml.Serialization;

using OpenRecommender; //our parser

namespace OpenRecommender
{

    public class JSONHelper
    {
        public static string Serialize<T>(T obj) 
        {
            System.Runtime.Serialization.Json.DataContractJsonSerializer serializer = new System.Runtime.Serialization.Json.DataContractJsonSerializer(obj.GetType());
            MemoryStream ms = new MemoryStream();
            serializer.WriteObject(ms, obj);
            string retVal = Encoding.Default.GetString(ms.ToArray());
            ms.Dispose();
            return retVal;
        }

        public static T Deserialize<T>(string json) 
        {
            T obj = Activator.CreateInstance<T>();
            MemoryStream ms = new MemoryStream(Encoding.Unicode.GetBytes(json));
            System.Runtime.Serialization.Json.DataContractJsonSerializer serializer = new System.Runtime.Serialization.Json.DataContractJsonSerializer(obj.GetType());
            obj = (T)serializer.ReadObject(ms);
            ms.Close();
            ms.Dispose();
            return obj;
        }
    }
    
    
    /// OpenRecommender object to Serialize/Deserialize to JSON
    [DataContract]    
    public class OpenRecommenderJSON 
    {
        private string url;
        
        public OpenRecommenderJSON() { }
        public OpenRecommenderJSON(string url)
        {
            this.url = url;
        }

        [DataMember]
        public object recommendations { get; set; }
        
        [DataMember]
        public object recommendation { get; set; }
        
        [DataMember]
        public object title { get; set; }
        [DataMember]
        public object image { get; set; }
        [DataMember]
        public object link { get; set; }
        [DataMember]
        public object description { get; set; }        
        
        [DataMember]
        public string titleData { get; set; }
        [DataMember]
        public string imageData { get; set; }
        [DataMember]
        public string linkData { get; set; }
        [DataMember]
        public string descriptionData { get; set; }
    }
    
    
    class Program          
    {
      /*
        /// use the JSON parser
        public string openrecommenderJSON
        {
          get 
          {
              /// use above helper methods to serialize and deserialize the Person object
              OpenRecommenderJSON myRec = new OpenRecommenderJSON("http://openrecommender.org/schema/JSON/recommendations.json");

              /// Serialize
              string json = JSONHelper.Serialize<OpenRecommenderJSON>(myRec);
              return json; //json.recommendations.recommendation.title.data;
          }          
          set 
          {
            this.url = value;
          }

        }    
      */
        
      /*
        //use the XML parser
        public string openrecommenderXML 
        {
          get 
          {
           //code from MAIN should go here
            return recommend;
          }
          set 
          {
            this.url = value;
          }
        }
      */
        
        static void Main(string[] args) 
        {         
          //CSS to display a Microsofty style (accessible to both JSON and XML parser output generators)
          string CSHARP_STYLE = "<style>img{width:120px;height:90px;border:0;vertical-align:middle} a{text-decoration:none; color:#2779AA} a:hover{color:#E17009} a:visited{#color:2779AA} ul{list-style-type:none} li{background:#DFEFFC;border-bottom:1px dashed #A6C9E2} li:hover{background:#AED0EA}</style>";
    
            string recommend = CSHARP_STYLE;
            try 
            {
                recommend += "<ul>";
                TextReader reader = new StreamReader("OpenRecommender.xml");
                XmlSerializer serializer = new XmlSerializer(typeof(recommendations));
                recommendations rec = (recommendations)serializer.Deserialize(reader);
                
                /// For each RECOMMENDATION...
                recommendation[] recommendationList = rec.recommendation;         
                foreach(recommendation recommendation in recommendationList)
                {               
                   /// Get each RECOMMENDATION's:  attributes as metadata
                   string identifier = recommendation.identifier;
                   string from = recommendation.sender; 
                   string to = recommendation.receiver;
                   string duration = recommendation.title.duration;
                   
                   /// get each RECOMMENDATION's:  TITLE, IMAGE, LINK, DESCRIPTION                
                   recommend += "<li><a href=\""+recommendation.link.Value+"\" title=\""+recommendation.description.Text[0]+"\"><img alt=\""+identifier+"\" src=\""+recommendation.image.Value+"\" /> "+recommendation.title.Text[0]+"</a>";
                   recommend += " ("+duration+")  From: "+from+" | To: "+to+"</li>";
                }
                recommend += "</ul>";
                reader.Close();
            }
            catch(Exception e) 
            {
                recommend = e.Message;
            }
            
            System.Console.WriteLine(recommend);
            //System.Console.WriteLine(openrecommenderXML());
            //System.Console.WriteLine(openrecommenderJSON);
        }
    }
}

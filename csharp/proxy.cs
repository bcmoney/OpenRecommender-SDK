using System;
using System.IO;
using System.Net;
using System.Text;
using System.Xml;


/**
 * OpenRecommender
 *   Class-based approach you can use standalone/command-line, or within other ASPs or classes
 */
class OpenRecommender {

	public string openrecommenderXML(string url)
	{
		XmlDocument doc = new XmlDocument();
		doc.Load(url);
		 
		XmlNodeList recommendations = getRecommendations(doc);
		 
		foreach (XmlNode node in recommendations) {

			XmlElement recommendationElement = (XmlElement) node;
			 
			string title = getRecommendationTitle(recommendationElement);
			string image = getRecommendationImage(recommendationImage);
			string link = getRecommendationLink(recommendationImage);
			string description = getRecommendationDescription(recommendationImage);
			string id = getRecommendationIdentifier(recommendationElement);
						 
			if (recommendationElement.HasAttributes) {
				id = getRecommendationIdentifier(recommendationElement);
			}
			//DEBUG: 
			display(title,image,link,description,id);
		}
	}
	
	public display(string title, string image, string link, string description, string id) {
		Console.WriteLine("<a href='{0}' id='recommendation_{1}' title='{2}'><img src='{3}' />{4}</a>\n", link, id, description, image, title);
	}
	
	
	/* Elements */
	public XmlNodeList getRecommendations(XmlDocument xml) {
		return xml.dom.GetElementsByTagName("recommendations");
	}
	
	public string getRecommendationTitle(XmlElement recommendation) {
		return recommendation.GetElementsByTagName("title")[0].InnerText;
	}

	public string getRecommendationImage(XmlElement recommendation) {
		return recommendation.GetElementsByTagName("image")[0].InnerText;
	}

	public string getRecommendationLink(XmlElement recommendation) {
		return recommendation.GetElementsByTagName("link")[0].InnerText;
	}

	public string getRecommendationDescription(XmlElement recommendation) {
		return recommendation.GetElementsByTagName("description")[0].InnerText;
	}

	/* <RECOMMENDATION> Attributes */
	public string getRecommendationIdentifier(XmlElement recommendation) {
		return recommendation.Attributes["identifier"].InnerText;
	}	
	
 

	/* 
	 * makeRequest
	 *   <summary>
	 *   Fetches a Web Page
	 *   </summary>
	 */
	public makeRequest {
		static void Main(string[] args)	{
			// used to build entire input
			StringBuilder sb  = new StringBuilder();

			// used on each read operation
			byte[] buf = new byte[8192];

			// prepare the web page we will be asking for
			HttpWebRequest  request  = (HttpWebRequest)
				if (args.Length > 0) {
					WebRequest.Create(args[0]);
				}
				else {
					WebRequest.Create("recommendations.xml");
				}
			// execute the request
			HttpWebResponse response = (HttpWebResponse)
				request.GetResponse();

			// we will read data via the response stream
			Stream resStream = response.GetResponseStream();

			string tempString = null;
			int count = 0;

			do {
				// fill the buffer with data
				count = resStream.Read(buf, 0, buf.Length);

				// make sure we read some data
				if (count != 0)	{
					// translate from bytes to ASCII text
					tempString = Encoding.ASCII.GetString(buf, 0, count);

					// continue building the string
					sb.Append(tempString);
				}
			}
			while (count > 0); // any more data to read?
			
			// print out page source
			Console.WriteLine(sb.ToString());
		}
	}

}
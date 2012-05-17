package {
  import flash.display.*;
  import flash.events.*;
  import flash.utils.ByteArray;
  import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.HTTPStatusEvent;
	import flash.events.IEventDispatcher;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
  import flash.xml.XMLDocument;
  import flash.xml.XMLNode;
  import flash.xml.XMLNodeType;
  
//---------------------------------
//
// OpenRecommender
//   ActionScript v3.0 helper for loading OpenRecommender recommendations directly into Flash
//   
// @author bcmoney
// @version 1.0
// @date 2011-11-11
// @license LGPL v3.0: http://www.gnu.org/licenses/lgpl.html
//--------------------------------  
  
  public class OpenRecommender extends Sprite {
 
  //--------------------------------
  //XML Load method 1 (local)
  //--------------------------------
    [Embed(source="recommendations.xml", mimeType="application/octet-stream")]
    private var BinaryData:Class;

  //--------------------------------    
  //Proxy requests to get remote file    
  //--------------------------------
    public function Proxy () {
      var byteArray:ByteArray = new BinaryData(); // Create a new instance of the embedded data
      var data:XML = new XML(byteArray.readUTFBytes(byteArray.length)); // Convert the data instance to XML  
      trace(data.toXMLString()); // Display the source code for the embedded XML
    }
  
  //-------------------------------- 
  // HTTP request handling  
  //--------------------------------
    var requestVars:URLVariables = new URLVariables();
      requestVars.object_name = "ts";
      requestVars.cache = new Date().getTime(); //prevent caching by using a timestamp

    var request:URLRequest = new URLRequest();
      request.url = getParams("url"); //get the "url" parameter to set the HTTP request URL (path to data source)
      request.contentType = getParams("f"); //get the "f" parameter to set the MIME-type (format)
      request.type = getParams("e"); //get the "e" parameter to set the encoding type (i.e. UTF-8)
      request.method = URLRequestMethod.GET;
      request.data = requestVars;
		
		for (var prop:String in requestVars) {
			trace("Sent " + prop + " as: " + requestVars[prop]);
		}

    var loader:URLLoader = new URLLoader();
      loader.dataFormat = URLLoaderDataFormat.TEXT;
      loader.addEventListener(Event.COMPLETE, loaderCompleteHandler);
      loader.addEventListener(HTTPStatusEvent.HTTP_STATUS, httpStatusHandler);
      loader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
      loader.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);

    try	{
      loader.load(request);
    }	catch (error:Error)	{
      trace("Unable to load URL");
    }

	
  //--------------------------------
  // Get URL Parameter passed to the Flash object by name  
  //--------------------------------
    public function getParams(documentRoot):Object {
      try {
        var params:Object = LoaderInfo(documentRoot.loaderInfo).parameters;
        var pairs:Object = {};
        var key:String;

        for(key in params) {
          pairs.key = String(params.key);
        }
      } catch(e:Error) {
        return {};
      }
      return params;
    }
  

  //--------------------------------
  // Listeners - method 1 (local)
  //--------------------------------
    public function loaderCompleteHandler(e:Event):void	{
      var variables:URLVariables = new URLVariables( e.target.data );
      if(variables.success) {
        trace(variables.path);
        //parse Recommendations XML format
        var i:Number = 0;
        for each (var recommendation:XML in recommendations) {
          trace("ID: "+recommendation[i].@["id"]);
          trace("TITLE: "+recommendation[i].title);
          trace("IMAGE: "+recommendation[i].image);
          trace("LINK: "+recommendation[i].link);
          trace("DESC: "+recommendation[i].description);
          i++;
        }              
      }
    }
    
    private function httpStatusHandler (e:Event):void {
      trace("httpStatusHandler:" + e);
    }
    
    private function securityErrorHandler (e:Event):void {
      trace("securityErrorHandler:" + e);
    }
    
    private function ioErrorHandler(e:Event):void {
      trace("ioErrorHandler: " + e);
    }  
    
    
    
    
  //--------------------------------    
  //XML Load method 2 (remote)
  //--------------------------------
    var dataXML:XML;
    var path = "http://openrecommender.org/schema/XML/recommendations.xml";
    var xmlRequest:URLRequest = new URLRequest(path);
    var xmlLoader:URLLoader = new URLLoader()
    xmlLoader.dataFormat = URLLoaderDataFormat.TEXT;
    configXmlLoaderListeners(xmlLoader);
    xmlLoader.load(xmlRequest);    
    
  //--------------------------------
  // Listeners - method 2 (remote)
  //--------------------------------
    function configXmlLoaderListeners(dispatcher:IEventDispatcher):void {
      dispatcher.addEventListener(Event.COMPLETE, xmlCompleteHandler);
      dispatcher.addEventListener(Event.OPEN, xmlOpenHandler);
      dispatcher.addEventListener(ProgressEvent.PROGRESS, xmlProgressHandler);
      dispatcher.addEventListener(SecurityErrorEvent.SECURITY_ERROR, xmlSecurityErrorHandler);
      dispatcher.addEventListener(HTTPStatusEvent.HTTP_STATUS, xmlHttpStatusHandler);
      dispatcher.addEventListener(IOErrorEvent.IO_ERROR, xmlIoErrorHandler);
    }
    
    function xmlCompleteHandler(event:Event):void {
      var loader:URLLoader = URLLoader(event.target);
      //trace("completeHandler: " + loader.data);

       try{
          dataXML = new XML(event.target.data)  //DEBUG:  trace("dataXML " + dataXML);       
          gotoAndStop("build menu");
        } catch (error:TypeError) {
            trace("Could not parse the XML")
            trace(error.message)
        }
    }  
    
    function xmlOpenHandler(event:Event):void {
      trace("openHandler: " + event);
    }

    function xmlProgressHandler(event:ProgressEvent):void {
      trace("progressHandler loaded:" + event.bytesLoaded + " total: " + event.bytesTotal);
    }

    function xmlSecurityErrorHandler(event:SecurityErrorEvent):void {
      trace("securityErrorHandler: " + event);
    }

    function xmlHttpStatusHandler(event:HTTPStatusEvent):void {
      trace("httpStatusHandler: " + event);
    }

    function xmlIoErrorHandler(event:IOErrorEvent):void {
      trace("ioErrorHandler: " + event);
    }      
  
}
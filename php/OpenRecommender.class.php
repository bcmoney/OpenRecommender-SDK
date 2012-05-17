<?php

include_once "../../lib/parser/html/simplehtmldom/simple_html_dom.php"; //'simplehtmldom' for HTML parsing

class OpenRecommender {

  public $file_format;
  public $file_string;
  public $file_object;
  

  /* 
   * OpenRecommender
   *   constructor
   * @param url String representing the URL of the resource to load (optional)
   * @param format String representing the Format [XML,JSON] of the resource to load (optional)
   */  
  public function __construct($url="recommendations.xml", $format="xml") {
    //load file
  	try {
		$this->file_string = file_get_contents($url);
	}
	catch(Exception $ex) {
		$this->file_string = file_get_contents_curl($url);	
	}
	
    //parse contents for display
    switch ($format) {
	  case "xml": //XML
        $this->file_object = new SimpleXMLElement($this->file_string);
	    break;
	  case "json": //JSON
	    $this->file_object = json_decode($this->file_string);
	    break;	  
	  default: //HTML
	    $html = str_get_html($this->file_string);
		$this->file_object = $html->find('#recommendations');
        break;
	}
	$this->file_format = $format;
  }  
  
  
  
  /* 
   * file_get_contents_curl
   *   Helper utility for making an HTTP Request
   *   Requires CURL for PHP: 
   *   http://curl.haxx.se/libcurl/php/
   * @param url  String representing the URL of the resource to load
   * @return file  String containing the entire contents of the resource loaded
   */
  public function file_get_contents_curl($url) {
    $ch = curl_init();

    curl_setopt($ch, CURLOPT_HEADER, 0);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);

    $file = curl_exec($ch);
    curl_close($ch);

    return $file;
  }
  
  /*
   * display
   *   Echoes the contents of the file string we loaded in the constructor
   *   (useful for debugging)
   */
  public function display() {
	echo $this->file_string;
  }

  /*
   * output
   *   Outputs the contents of the file object (as an array var dump)
   *   (useful for debugging)
   */
  public function output() {
	print_r($this->file_object);
  }  
  
  
  
  /* RECOMMENDATIONS */
  public function getRecommendations() {
    return ($this->file_format == 'json') ? $this->file_object->recommendations->recommendation : $this->file_object->recommendation;
  }
  public function getRecommendationIdentifier($recommendation) {
    return $recommendation->identifier;
  }
  public function getRecommendationSender($recommendation) {
    return $recommendation->sender;
  }
  public function getRecommendationReceiver($recommendation) {
    return $recommendation->receiver;
  }
  public function getRecommendationDate($recommendation) {
    return $recommendation->date;
  }
  public function getRecommendationTime($recommendation) {
    return $recommendation->time;
  }
 public function getRecommendationPriority($recommendation) {
    return $recommendation->priority;
  }
  public function getRecommendationType($recommendation) {
    return $recommendation->type;
  }  
  
  /* TITLE */
  public function getRecommendationTitle($recommendation) {
    return ($this->file_format == 'json') ? $recommendation->title->data : (String)$recommendation->title;
  }
  public function getRecommendationTitleDuration($title) {
    return $recommendation->duration;
  }
  public function getRecommendationTitleLanguage($title) {
    return $recommendation->language;
  }
  public function getRecommendationTitleCaptions($title) {
    return $recommendation->captions;
  }
  public function getRecommendationTitleSubtitles($title) {
    return $recommendation->subtitles;
  }
  public function getRecommendationTitlePlaylist($title) {
    return $recommendation->playlist;
  }
  
  /* IMAGE */
  public function getRecommendationImage($recommendation) {
    return ($this->file_format == 'json') ? $recommendation->image->data : (String)$recommendation->image;
  }
  public function getRecommendationImageCreator($image) {
    return $recommendation->creator;
  }
  public function getRecommendationImagePublisher($image) {
    return $recommendation->publisher;
  }
  public function getRecommendationImageFormat($image) {
    return $recommendation->format;
  }
  public function getRecommendationImageRights($image) {
    return $recommendation->rights;
  }
  public function getRecommendationImageRightsHolder($image) {
    return $recommendation->rightsHolder;
  }
  
  /* LINK */
  public function getRecommendationLink($recommendation) {
    return ($this->file_format == 'json') ? $recommendation->link->data : (String)$recommendation->link;
  }
  public function getRecommendationLinkShortlink($link) {
    return $recommendation->shortlink;
  }
  public function getRecommendationLinkLocation($link) {
    return $recommendation->location;
  }
  public function getRecommendationLinkMobile($link) {
    return $recommendation->mobile;
  }
  public function getRecommendationLinkMobiledownload($link) {
    return $recommendation->mobiledownload;
  }
  public function getRecommendationLinkMobilestream($link) {
    return $recommendation->mobilestream;
  }
  public function getRecommendationLinkFormat($link) {
    return $recommendation->format;
  }
  public function getRecommendationLinkCreator($link) {
    return $recommendation->creator;
  }
  public function getRecommendationLinkPublisher($link) {
    return $recommendation->publisher;
  }
  public function getRecommendationLinkRights($link) {
    return $recommendation->rights;
  }
  public function getRecommendationLinkRightsHolder($link) {
    return $recommendation->rightsHolder;
  }
  public function getRecommendationLinkOembed($link) {
    return $recommendation->oembed;
  }
  
  /* DESCRIPTION (optional) */
  public function getRecommendationDescription($recommendation) {
    return ($this->file_format == 'json') ? $recommendation->description->data : (String)$recommendation->description;
  }
  public function getRecommendationDescriptionRating($description) {
    return $description->rating;
  }
  public function getRecommendationDescriptionReview($description) {
    return $description->review;
  }
  public function getRecommendationDescriptionSubject($description) {
    return $description->subject;
  }
  
}

?>
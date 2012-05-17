/**
 * jQuery OpenRecommender plugin
 *
 *   Makes REST Web Service requests and Parses XML or JSON responses from the
 *   OpenRecommender Recommendation Engine. 
 *
 * EXAMPLE USAGES: 
 *   $('#content').OpenRecommender(); // calls the init method
 *
 *   $('#content').OpenRecommender({  // calls the init method, with config set
 *     url : 'http://openrecommender.org/schema/XML/recommendations.xml'
 *   });
 *
 *	  $('#content').OpenRecommender('init',{   // calls init using JSON parser instead of XML default
 *		   url: 'recommendations.json',
 *		format: 'json'
 *	  });
 * 
 *   $('#content').OpenRecommender('display'); // calls the display method to print response (useful for debugging)
 *
 @ @version 1.0
 * @author bcmoney
 * @date 2011-11-11
 * FOR MORE INFO, SEE: 
 *   http://openrecommender.org
 */
(function($){
  

  /* extend jQuery ($) with OpenRecommender function */
  $.fn.OpenRecommender = function(config) {

    //default configurations
      var defaults = {
	    url              : 'recommendations.xml', //URL to make request to
        format           : 'xml', //expected response format of this URL request
		thumbnail_width  : '75', //preview image display width
		thumbnail_height : '50', //preview image display height
		container        : '#content article' //HTML DOM element on which to append the recommendations
	  }; 
  
	  var config = $.extend(defaults, config);
	  
		if (config.format.toLowerCase() == 'xml') {
			/* using the XML endpoint */	
			$.ajax({
			  url: config.url,
			  dataType: config.format,
			  success: function(xml) {
			    getRecommendations(xml);
			  },
			  error: function() {
				$.error('Error in AJAX request');
			  }
			});
		}
		else {
			/* using the JSON endpoint */
			$.getJSON(config.url, function(json) {      
			    getRecommendations(json);
			  }
			);
		}		
	  
	  getRecommendations = function(responseData) {
	    $(config.container).append('<ol>');
	    if (config.format.toLowerCase() == "xml") {		    
			$(responseData).find('recommendation').each(function(i, recommendation) {
				$(config.container).append(				    
					'<li class="' + ( (i%2==0)?'even':'odd' ) + '">' +
					'<a id="recommendation_'+getRecommendationIdentifier(recommendation) +
					'" href="'+getRecommendationLink(recommendation) +
					'" title="'+getRecommendationDescription(recommendation)+'">' +
					'<img src="'+getRecommendationImage(recommendation)+'" width="'+config.thumbnail_width+'" height="'+config.thumbnail_height+'" />' +
					getRecommendationTitle(recommendation)+'</a></li>'
				);
			});
		}
		else {
			$.each(responseData.recommendations.recommendation, function(i, recommendation) {
				$(config.container).append(
				    '<li class="' + ( (i%2==0)?'even':'odd' ) + '">' +
					'<a id="recommendation_'+getRecommendationIdentifier(recommendation) +
					'" href="'+getRecommendationLink(recommendation) +
					'" title="'+getRecommendationDescription(recommendation)+'">' +
					'<img src="'+getRecommendationImage(recommendation)+'" width="'+config.thumbnail_width+'" height="'+config.thumbnail_height+'" />' +
					getRecommendationTitle(recommendation)+'</a></li>'
				);
			});
		}
		$(config.container).append('</ol>');
      }
      getRecommendationIdentifier = function(recommendation) {	 
        return (config.format.toLowerCase() == "xml") ? $(recommendation).attr('identifier') : recommendation.identifier;
      },
      getRecommendationTitle = function(recommendation) {
        return (config.format.toLowerCase() == "xml") ? $(recommendation).find("title").text() : recommendation.title.data;
      },
      getRecommendationImage = function(recommendation) {
        return (config.format.toLowerCase() == "xml") ? $(recommendation).find("image").text() : recommendation.image.data;
      },
      getRecommendationLink = function(recommendation) {
        return (config.format.toLowerCase() == "xml") ? $(recommendation).find("link").text() : recommendation.link.data;
      },
      getRecommendationDescription = function(recommendation) {
        return (config.format.toLowerCase() == "xml") ? $(recommendation).find("description").text() : recommendation.description.data;
      }	  
  };
  
})( jQuery );
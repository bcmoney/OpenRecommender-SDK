  /* using XML */
  function loadXML(url) 
  {
    if (window.XMLHttpRequest)
    {
      xhttp = new XMLHttpRequest();
    }
    else
    {
      xhttp = new ActiveXObject('Microsoft.XMLHTTP');
    }
    xhttp.open('GET',url,false);
    xhttp.send();
    return xhttp.responseXML;
  }

  var xml = loadXML('recommendations.xml'); // loadxml('http://openrecommender.org/recommendations/');
  recommendations = xml.getElementsByTagName('recommendations');
	recListXML = document.createElement('ol'); //create an ordered list Element to list the Recommendations
		recListXML.setAttribute('id', 'recListXML');  
	document.getElementById('recommendXML').appendChild(recListXML);
  recommendation = xml.getElementsByTagName('recommendation');
  for (var i = 0; i < recommendation.length; i++)
  {
	 title = xml.getElementsByTagName('title')[i].childNodes[0].nodeValue;
	 image = xml.getElementsByTagName('image')[i].childNodes[0].nodeValue;
	 link = xml.getElementsByTagName('link')[i].childNodes[0].nodeValue;
	 desc = xml.getElementsByTagName('description')[i].childNodes[0].nodeValue;
	 document.getElementById('recListXML').innerHTML += '<li class="'+((i%2===0)?'even':'odd')+'"><a href="'+link+'" title="'+desc+'"><img src="'+image+'"/>'+title+'</a></li>';
  }  
  
  
  /* using JSON */
  function loadJSON(url) 
  {
    if (window.XMLHttpRequest)
    {
      xhttp = new XMLHttpRequest();
    }
    else
    {
      xhttp = new ActiveXObject('Microsoft.XMLHTTP');
    }
    xhttp.open('GET',url,false);
    xhttp.send();
    return xhttp.responseText;
  }  
  
  var jsonText  = loadJSON('recommendations.json'); // loadJSON('http://openrecommender.org/recommendations/'); + overrideHeader('format=json')  
  var json = eval('(' + jsonText + ')');
  recommendations = json.recommendations;
	recListJSON = document.createElement('ol'); //create an ordered list Element to list the Recommendations
		recListJSON.setAttribute('id', 'recListJSON');  
	document.getElementById('recommendJSON').appendChild(recListJSON);
  recommendation = recommendations.recommendation;
  for (var j = 0; j < recommendation.length; j++)
  {
	 title = recommendation[j].title.data;
	 image = recommendation[j].image.data;
	 link = recommendation[j].link.data;
	 desc = recommendation[j].description.data;
	 document.getElementById('recListJSON').innerHTML += '<li class="'+((j%2===0)?'even':'odd')+'"><a href="'+link+'" title="'+desc+'"><img src="'+image+'"/>'+title+'</a></li>';
  }  
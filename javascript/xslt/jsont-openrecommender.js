/* load JSON recommendations */
function loadJSON(url) {
	if (window.XMLHttpRequest) {
	  xhttp = new XMLHttpRequest();
	}
	else {
	  xhttp = new ActiveXObject('Microsoft.XMLHTTP');
	}
	xhttp.open('GET',url,false);
	xhttp.send();
	return xhttp.responseText;
}  

/* JSONT - setup JSON data and JSON Template */
recommendationsJSON = eval('(' + loadJSON('recommendations.json') + ')');
recommendationsHTML = { "recommendations": "<ol><br/>{$.recommendation}</ol>",
	  "recommendations.recommendation[*]": "<li id=\"recommendation_{$.identifier}\"><a href='{$.link.data}'><img src='{$.image.data}' width=\"75\" style=\"vertical-align:middle\" />{$.title.data}</a></li>" };

/* transform JSON on page load */
window.onload = function() { document.getElementById("recommend").innerHTML = jsonT(recommendationsJSON, recommendationsHTML); };
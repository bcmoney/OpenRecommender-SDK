<%-- 
    Document   : index
    Description: old-school style standard JSP with Java code directly in page
    Created on : Apr 26, 2012, 12:31:32 PM
    Author     : bcopeland
--%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.openrecommender.OpenRecommender"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="shortcut icon" href="/OpenRecommenderClient/favicon.ico" />
    <title>OpenRecommender - JSP</title>
    <style type="text/css">
        img{border:0;vertical-align:middle;width:120px} 
        ul{list-style-type:none;margin:0;padding:0px} 
        li{background:#587993;border-bottom:1px dashed #fff} 
        span{color:#E76F00}
        li:hover{background:#E76F00}
        li:hover span{color:#587993}
        li:hover a{#5382A1}
        a{color:#E76F00;text-decoration:none}
    </style>
</head>
<body>
<img src="http://upload.wikimedia.org/wikipedia/en/thumb/3/39/Java_logo.svg/300px-Java_logo.svg.png" alt="Java (JSP)"  />
<%
    String url = request.getParameter("url"); //get URL parameter from request
    //OpenRecommender parsing code
    OpenRecommender orec = new OpenRecommender(); 
    Document xml = orec.load(url);
    NodeList recommendations = orec.getRecommendations(xml);
    String recommendationsHTML = "";
    //HTML output
    for (int i = 0; i < recommendations.getLength(); i++) {
        Element recommendation = orec.getRecommendation(recommendations, i);
        recommendationsHTML += "<li><a id='recommendation_" + orec.getRecommendationIdentifier(recommendation) +
            "' href='" + orec.getRecommendationLink(recommendation) + 
            "' title='" + orec.getRecommendationTitle(recommendation) + "'>" +            
            "<img src='" + orec.getRecommendationImage(recommendation) + "' />" + 
            "<span>" + orec.getRecommendationDescription(recommendation) + "</span></a></li>";
    }
%>
<ul>
    <%= recommendationsHTML %>
</ul>
</body>
</html>
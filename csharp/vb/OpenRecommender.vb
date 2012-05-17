<%@LANGUAGE="VBSCRIPT"%>
<%
'gather the required Query Parameters URL, Format, Encoding
dim url
url = Request.QueryString("url")
If url<>"" Then	  
    url = "http://example.com"
End If

dim format
	format = Request.QueryString("f")
If format<>"" Then	  
     format = "text/plain"
End If

dim encoding
	encoding = Request.QueryString("e")
If encoding<>"" Then	  
    encoding = "UTF-8"
End If


'create a unique instance NONCE to ensure proxied calls are not cached
dim instanceID
	instanceID = Session.SessionID


'perform the HTTP Request 
dim xmlhttp
	set xmlhttp = server.Createobject("MSXML2.ServerXMLHTTP")
	xmlhttp.Open "GET",url&"instanceID="&instanceID,false	'IF doing a POST:   use --> "POST",url,false
	'"":   set DataToSend = value   then call --->   xmlhttp.send DataToSend
	'"":   also set Content-Type of POST body --->   xmlhttp.setRequestHeader "Content-Type", "application/x-www-form-urlencoded"

	
'display the response
Response.ContentType = format       'May want to check IF ContentType == "text/xml"
Response.Charset = encoding         'UTF-8 should be fine for most uses unless not supported, then go with:   
Response.Write xmlhttp.responseText 'IF ContentType == "text/xml":   Response.Write xmlhttp.responsexml.xml
Response.End()
Set xmlhttp = nothing

%>
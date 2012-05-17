#!c:/Python27/python.exe -u
#!/usr/bin/env python
# -*- coding: UTF-8 -*-
import sys
import cgitb
import cgi
import urllib
import OpenRecommender

cgitb.enable()

DEBUG = 0 #1 to turn debugging on, 0 to turn it off
 
def main():

  #Store url, encoding and format passed with URL parameters used to call this script (if any)
  params = cgi.FieldStorage()
  url = params.getvalue('url')
  format = params.getvalue('f')
  encoding = params.getvalue('e') 

  errors = []
  
  if (url is None) or (len(url) == 0):
    url = "http://openrecommender.org/schema/XML/recommendations.xml"
    errors.append("<br/>** WARNING - Missing URL (using default) **")

  if (format is None) or (len(format) == 0):
    format = "text/html"
    errors.append("<br/>** WARNING - Missing Format (using default) **")

  if (encoding is None) or (len(encoding) == 0):
    encoding = "utf-8"
    errors.append("<br/>** WARNING - Missing Encoding (using default) **")
   
  # HTTP Response Header
  print("Content-Type: "+format+";charset="+encoding)
  print("")
  
  #perform lookup to get remote content
  # urllib.urlopen(url).read()
    
  python_style = '<html><head><link rel="shortcut icon" href="./favicon.ico" /><style>img{border:0;vertical-align:middle} a{color:#fff} ul{list-style-type:none} li{background:#5a9fd4;border-bottom:1px dashed #fff} li:hover{background:#ffd43b} li:hover a{color:#5a9fd4}</style></head><body>'
  print(python_style)
  
  if len(sys.argv) >= 3:
    if (sys.argv[1].find("json") != -1) or (sys.argv[2].lower() == "json") or (url.find("json") != -1)  or (format.find("json") != -1):
      print(OpenRecommender.openrecommenderJSON(sys.argv[1]))
    else:
      print(OpenRecommender.openrecommenderXML(sys.argv[1]))
  elif len(sys.argv) == 2:
    if sys.argv[1].find("json") != -1 or (url.find("json") != -1)  or (format.find("json") != -1):
      print(OpenRecommender.openrecommenderJSON(sys.argv[1]))
    else:
      print(OpenRecommender.openrecommenderXML(sys.argv[1]))
  else:    
    print(OpenRecommender.openrecommenderJSON("recommendations.json"))
    errors.append(" <hr/>ERROR: Could not read Recommendations file, please call 'index.py RECOMMENDATIONS' from command-line. Defaulting to sample recommendations.")
	
  if DEBUG:    
    print("".join(errors))
  
  print('</body></html>')
  
if __name__ == '__main__':
  main()
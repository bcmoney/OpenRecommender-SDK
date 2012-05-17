#!C:\Ruby192\bin\ruby.exe
## WIN32
# --------------------- 
## UNIX
#!/usr/bin/ruby
require 'cgi'
require 'uri'
require './OpenRecommender.rb'


RUBY_STYLE = "<style>img{border:0;vertical-align:middle;} ul{list-style-type:none;} li{background:#FB7655;border-bottom:1px dashed #fff;color:#ccc} li:hover {background:#ccc;color:#000} li:hover a{color:#E42B1E} a{color:#871101}</style>";


cgi = CGI.new
url = cgi['url']
format = cgi['format']
encoding = cgi['encoding']

if url == "" then url = "http://openrecommender.org/schema/XML/recommendations.xml"; end
if format == "" then format = "text/html"; end
if encoding == "" then encoding = "utf-8"; end


openrecommender = OpenRecommender.new

puts "Content-type: " + format
puts ""
puts "<html>"
puts "<head><link rel=\"shortcut icon\" href=\"./favicon.ico\" /><meta http-equiv=\"Content-Type\" content=\"" + format + ";charset=" + encoding + "\" >"
puts "<title>OpenRecommender RUBY client</title>"
puts RUBY_STYLE
puts "</head>"
puts "<body>"

  print openrecommender.openrecommenderXML(url)
  #print openrecommender.openrecommenderJSON(url)
  
puts "<hr/><span style='font-size:0.8em;'>OpenRecommender RUBY client</span>"
puts "</body>"
puts "</html>"  

 


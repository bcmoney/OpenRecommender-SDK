#!C:\Ruby192\bin\ruby.exe
## WIN32
# --------------------- 
## UNIX
#!/usr/bin/ruby

require 'net/http'
require 'rexml/document'
require 'json'

class OpenRecommender
  
  def initialize
    
  end

  def openrecommenderJSON(url)  
    
    # get the JSON data as a string
    resp = Net::HTTP.get_response(URI.parse(url))
    data = resp.body
    # convert the returned JSON data to native Ruby data structure - a hash
    json = JSON.parse(data)

     # if the hash has 'Error' as a key, we raise an error
    if json.has_key? 'Error'
       raise "HTTP or web service error"
    end

    recommend = "<ul>"
    json['recommendations']['recommendation'].each { |recommendation|
      recommend = recommend + "<li><a href=\"#{recommendation['link']['data']}\" title=\"#{recommendation['description']['data']}\"><img src=\"#{recommendation['image']['data']}\" width=\"120\" height=\"90\"/>#{recommendation['title']['data']}</a></li>\n"
    }
    recommend = recommend + "</ul>"
   
    return recommend
  end


  def openrecommenderXML(url)  

    # get the XML data as a string
    xml_data = Net::HTTP.get_response(URI.parse(url)).body

    # extract event information
    doc = REXML::Document.new(xml_data)
    titles = []
    images = []
    links = []
    descriptions = []
    doc.elements.each('recommendations/recommendation/title') do |ele|
       titles << ele.text
    end
    doc.elements.each('recommendations/recommendation/image') do |ele|
       images << ele.text
    end
    doc.elements.each('recommendations/recommendation/link') do |ele|
       links << ele.text
    end
    doc.elements.each('recommendations/recommendation/description') do |ele|
       descriptions << ele.text
    end

    recommend = "<ul>"
    # print all events
    titles.each_with_index do |title, idx|
       recommend = recommend + "<li><a href=\"#{links[idx]}\" title=\"#{descriptions[idx]}\"><img src=\"#{images[idx]}\" width=\"120\" height=\"90\"/>#{title}</a></li>\n"
    end
    recommend = recommend + "</ul>"

    return recommend
  end

end
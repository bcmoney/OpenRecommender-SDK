#!c:/Python27/python.exe -u
#!/usr/bin/env python
# -*- coding: UTF-8 -*-
import json
from xml.dom import minidom
from urllib import urlopen


OPENRECOMMENDER_URL = 'http://openrecommender.org/schema/XML/recommendations.xml' #OpenRecommender Web Service endpoint
OPENRECOMMENDER_NS = 'http://openrecommender.org/schema/XML' #OpenRecommender Namespace

'''
' openrecommenderXML
'   displays XML recommendation data as human-readable HTML   
'''
def openrecommenderXML(u):

  if u is None:
    print("** ERROR - Missing URL (using default) **")
    url = OPENRECOMMENDER_URL
  elif len(u) == 0:
    print("** ERROR - Missing URL (using default) **")
    url = OPENRECOMMENDER_URL
  else:
    url = u
    
  dom = minidom.parse(urlopen(url))
  
  openrecommender = []
  for recommendation in dom.getElementsByTagName('recommendation'): # FOR NAMESPACES, USE: dom.getElementsByTagNameNS(OPENRECOMMENDER_NS, 'recommendation'):
    openrecommender.append(
      '<li><a id="recommendation_'+getRecommendationIdentifier(recommendation)+
      '" href="'+getRecommendationLink(recommendation)+
      '" title="'+getRecommendationDescription(recommendation)+
      '"><img src="'+getRecommendationImage(recommendation)+      
      '"/>'+getRecommendationTitle(recommendation)+'</a></li>\n'
    )    
      
  return '<ul>'+''.join(openrecommender)+'</ul>'


'''
' openrecommenderJSON
'   displays JSON recommendation data as human-readable HTML
'''  
def openrecommenderJSON(u):

  if u is None:
    print("** ERROR - Missing URL (using default) **")
    url = OPENRECOMMENDER_URL
  elif len(u) == 0:
    print("** ERROR - Missing URL (using default) **")
    url = OPENRECOMMENDER_URL
  else:
    url = u
  
  json_string = open(url)
  json_object = json.load(json_string)
    
  openrecommender = []
  for recommendation in json_object["recommendations"]["recommendation"]:    
    id = recommendation["identifier"]
    title = recommendation["title"]["data"]
    image = recommendation["image"]["data"]
    link = recommendation["link"]["data"]
    description = recommendation["description"]["data"]
    
    openrecommender.append(
      '<li><a id="recommendation_'+id+
      '" href="'+link+
      '" title="'+description+
      '"><img src="'+image+
      '" width="120" height="90"/>'+title+'</a></li>\n'
    )
  
  return '<ul>'+''.join(openrecommender)+'</ul>'

  

  
'''
' XML PARSER
'''
#Elements  
def getRecommendationTitle(recommendation):
  return recommendation.getElementsByTagName('title')[0].firstChild.data

def getRecommendationImage(recommendation):
  return recommendation.getElementsByTagName('image')[0].firstChild.data

def getRecommendationLink(recommendation):
  return recommendation.getElementsByTagName('link')[0].firstChild.data
  
def getRecommendationDescription(recommendation):
  return recommendation.getElementsByTagName('description')[0].firstChild.data

  
#<RECOMMENDATION> Attributes
def getRecommendationIdentifier(recommendation):
  return recommendation.getAttribute('identifier')
  
def getRecommendationSender(recommendation):
  return recommendation.getAttribute('sender')  
  
def getRecommendationReceiver(recommendation):
  return recommendation.getAttribute('receivier')
  
def getRecommendationDate(recommendation):
  return recommendation.getAttribute('date')

def getRecommendationTime(recommendation):
  return recommendation.getAttribute('time')
  
def getRecommendationPriority(recommendation):
  return recommendation.getAttribute('priority')
  
def getRecommendationType(recommendation):
  return recommendation.getAttribute('type')
  
  
#<TITLE> Attributes
def getRecommendationTitleDuration(title):
  return title.getAttribute('duration')
  
def getRecommendationTitleLanguage(title):
  return title.getAttribute('language')
  
def getRecommendationTitleCaptions(title):
  return title.getAttribute('captions')
  
def getRecommendationTitleSubtitles(title):
  return title.getAttribute('subtitles')
  
def getRecommendationTitlePlaylist(title):
  return title.getAttribute('playlist')
  
  
#<IMAGE> Attributes
def getRecommendationImageCreator(image):
  return image.getAttribute('creator')
  
def getRecommendationImagePublisher(image):
  return image.getAttribute('publisher')
  
def getRecommendationImageFormat(image):
  return image.getAttribute('format')
  
def getRecommendationImageRights(image):
  return image.getAttribute('rights')
  
def getRecommendationImageRightsHolder(image):
  return image.getAttribute('rightsHolder')


#<LINK> Attributes
def getRecommendationLinkShortlink(link):
  return link.getAttribute('shortlink')
  
def getRecommendationLinkLocation(link):
  return link.getAttribute('location')
  
def getRecommendationLinkMobile(link):
  return link.getAttribute('mobile')
  
def getRecommendationLinkMobiledownload(link):
  return link.getAttribute('mobiledownload')
  
def getRecommendationLinkMobilestream(link):
  return link.getAttribute('mobilestream')

def getRecommendationLinkCreator(link):
  return link.getAttribute('creator')
  
def getRecommendationLinkPublisher(link):
  return link.getAttribute('publisher')
  
def getRecommendationLinkFormat(link):
  return link.getAttribute('format')
  
def getRecommendationLinkRights(link):
  return link.getAttribute('rights')
  
def getRecommendationLinkRightsHolder(link):
  return link.getAttribute('rightsHolder')
  

#<DESCRIPTION> Attributes
def getRecommendationDescriptionRating(description):
  return description.getAttribute('rating')
  
def getRecommendationDescriptionReview(description):
  return description.getAttribute('review')
  
def getRecommendationDescriptionTags(description):
  return description.getAttribute('tags')  
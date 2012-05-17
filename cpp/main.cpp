#include <stdio.h>    // to get "printf" function
#include <stdlib.h>   // to get "free" function
#include <iostream>   // for testing, output to console
#include "include/xmlParser.h" //link to XML Parser lib

using namespace std;

int main(int argc, char **argv)
{
  cout << "OpenRecommender v1.0" << endl;
  // this open and parse the XML file:
  XMLNode xMainNode = XMLNode::openFileHelper("recommendations.xml");

  // this gets ROOT node "<recommendations>"
  XMLNode recommendations = xMainNode.getChildNode("recommendations");
  int n = recommendations.nChildNode("recommendation");
  // this prints the "coefficient" value for all the "NumericPredictor" tags:
  for (int i=0; i < n; i++) {
    XMLNode recommendation = recommendations.getChildNode("recommendation",i);
      printf("\n%i).RECOMMENDATION_ID: %i\n",i+1,atoi(recommendation.getAttribute("identifier")));
    XMLNode title = recommendation.getChildNode("title");
      printf("TITLE: '%s'\n", title.getText());
      printf("@lang: '%s'\n", title.getAttribute("language"));
    XMLNode image = recommendation.getChildNode("image");
      printf("IMAGE: '%s'\n", image.getText());
      printf("@format: '%s'\n", image.getAttribute("format"));
    XMLNode link = recommendation.getChildNode("link");
      printf("LINK: '%s'\n", link.getText());
      printf("@mobile: '%s'\n", link.getAttribute("mobile"));
    XMLNode description = recommendation.getChildNode("description");
      printf("DESC: '%s'\n", description.getText());
      printf("@rating: '%s'\n", description.getAttribute("rating"));
  }

  return 0;
}

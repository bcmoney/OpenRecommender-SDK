#include <stdio.h>
#include "ezxml.h"
 
 
int main(void)
{
    ezxml_t recommendations = ezxml_parse_file("recommendations.xml"), recommendation, title, image, link, desc;
    const char *id, *sender, *receiver;

    for (recommendation = ezxml_child(recommendations, "recommendation"); recommendation; recommendation = recommendation->next) {
        id = ezxml_attr(recommendation, "identifier");
        sender = ezxml_attr(recommendation,"sender");
        receiver = ezxml_attr(recommendation,"receiver");
      title = ezxml_child(recommendation, "title");
      image = ezxml_child(recommendation, "image");
      link = ezxml_child(recommendation, "link");
      desc = ezxml_child(recommendation, "description");
      printf("\n--RECOMMENDATION %s %s %s\n", id, sender, receiver);
        printf("----TITLE: %s\n%s | %s | %s\n", title->txt, ezxml_attr(title,"duration"), ezxml_attr(title,"language"), ezxml_attr(title,"playlist"));
        printf("----IMAGE: %s\n%s | %s | %s\n", image->txt, ezxml_attr(image,"creator"), ezxml_attr(image,"publisher"), ezxml_attr(image,"format"));
        printf("----LINK: %s\n%s | %s | %s\n", link->txt, ezxml_attr(link,"format"), ezxml_attr(link,"shortlink"), ezxml_attr(link,"mobile"));
        printf("----DESC: %s\n%s | %s \n", desc->txt, ezxml_attr(desc,"rating"), ezxml_attr(desc,"review"), ezxml_attr(desc,"tags"));
    }

    ezxml_free(recommendations);
    
    return 0;
}
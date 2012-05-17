#include <stdio.h>
#include "ezxml.h"
 
 
int main(void)
{
    ezxml_t f1 = ezxml_parse_file("formula1.xml"), team, driver;
    const char *teamname;

    for (team = ezxml_child(f1, "team"); team; team = team->next) {
      teamname = ezxml_attr(team, "name");
      for (driver = ezxml_child(team, "driver"); driver; driver = driver->next) {
          printf("%s, %s: %s\n", ezxml_child(driver, "name")->txt, teamname,
                 ezxml_child(driver, "points")->txt);
      }
    }

    ezxml_free(f1);


    printf("hello, world\n");
    return 0;
}
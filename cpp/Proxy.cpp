#include <iostream>
#include "curl/curl.h"

/*
 * This is a very simple example of how to use libcurl + curlpp from within
 * a C++ program to achieve a server-side web proxy. 
 *
 * USAGE:
 *    MyCurl::get("http://example.com");
 * 
 * @author Bryan Copeland
 * @author Amado Martinez
 * @author Todd Papaioannou
 */ 


class MyCurl
{
private:

public:
// Write any errors in here

static int writer(char *data, size_t size, size_t nmemb, std::string *buffer_in)
{

// Is there anything in the buffer?
if (buffer_in != NULL)
{
// Append the data to the buffer
buffer_in->append(data, size * nmemb);

// How much did we write?
return size * nmemb;
}

return 0;
}

static std::string get(const char* url)
{
CURL *curl;
CURLcode result;

// Create our curl handle
curl = curl_easy_init();

char errorBuffer[CURL_ERROR_SIZE];
// Write all expected data in here
std::string buffer;

if (curl)
{
// Now set up all of the curl options
curl_easy_setopt(curl, CURLOPT_ERRORBUFFER, errorBuffer);
curl_easy_setopt(curl, CURLOPT_URL, url);
curl_easy_setopt(curl, CURLOPT_HEADER, 0);
curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1);
curl_easy_setopt(curl, CURLOPT_COOKIEJAR, "cookies.txt");
curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, MyCurl::writer);
curl_easy_setopt(curl, CURLOPT_WRITEDATA, &buffer);

// Attempt to retrieve the remote page
result = curl_easy_perform(curl);

// Always cleanup
curl_easy_cleanup(curl);
}

if(result == CURLE_OK)
return buffer;
return std::string();
}
};
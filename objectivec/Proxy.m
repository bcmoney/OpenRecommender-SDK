#############################################
#   URLParser.h
#############################################
//  @author Dimitris (09/02/2010)
//  @author BCmoney (05/01/2009) 
#import <Foundation/Foundation.h>
 
@interface URLParser : NSObject {
	NSArray *variables;
}
 
@property (nonatomic, retain) NSArray *variables;
 
- (id)initWithURLString:(NSString *)url;
- (NSString *)valueForVariable:(NSString *)varName;
 
@end



#############################################
#   URLParser.m
#############################################
//  @author Dimitris (09/02/2010)
//  @author BCmoney (05/01/2009) 
#import "URLParser.h"

@implementation URLParser
@synthesize variables;
 
- (id) initWithURLString:(NSString *)url{
	self = [super init];
	if (self != nil) {
		NSString *string = url;
		NSScanner *scanner = [NSScanner scannerWithString:string];
		[scanner setCharactersToBeSkipped:[NSCharacterSet characterSetWithCharactersInString:@"&?"]];
		NSString *tempString;
		NSMutableArray *vars = [NSMutableArray new];
		[scanner scanUpToString:@"?" intoString:nil];		//ignore the beginning of the string and skip to the vars
		while ([scanner scanUpToString:@"&" intoString:&tempString]) {
			[vars addObject:[tempString copy]];
		}
		self.variables = vars;
		[vars release];
	}
	return self;
}
 
- (NSString *)valueForVariable:(NSString *)varName {
	for (NSString *var in self.variables) {
		if ([var length] > [varName length]+1 && [[var substringWithRange:NSMakeRange(0, [varName length]+1)] isEqualToString:[varName stringByAppendingString:@"="]]) {
			NSString *varValue = [var substringFromIndex:[varName length]+1];
			return varValue;
		}
	}
	return nil;
}
 
- (void) dealloc{
	self.variables = nil;
	[super dealloc];
}
 
@end




#############################################
#   Proxy.h
#############################################
//  @author BCmoney (05/01/2009) 
#import <Foundation/Foundation.h>
 
@interface Proxy : NSObject {
	NSArray *params;
}
 
@property (nonatomic, retain) NSArray *variables;
 
- (id)makeRequest:(NSString *)url;
- (NSString *)valueForVariable:(NSString *)url;
- (NSString *)valueForVariable:(NSString *)f;
- (NSString *)valueForVariable:(NSString *)e;
 
@end


#############################################
# Proxy.m
#############################################
//  @author BCmoney (05/01/2009) 

@implementation Proxy

//parse URL
URLParser *parser = [[[URLParser alloc] initWithURLString:@"http://example.com/action.php?url=100&f=text/xml&e=utf-8"] autorelease];
NSString *url = [parser valueForVariable:@"url"];
NSLog(@"%@", var);   //URL to make HTTP request to
NSString *format = [parser valueForVariable:@"f"];
NSLog(@"%@", format);   //Format expected of response
NSString *encoding = [parser valueForVariable:@"e"];
NSLog(@"%@", encoding);   //Encoding expected of response (defaults to UTF-8)


- (id) makeRequest:(NSString *)url{
	//prepare request
	NSString *urlString = [NSString stringWithFormat:parser];
	NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] init] autorelease];
	[request setURL:[NSURL URLWithString:urlString]];
	[request setHTTPMethod:@"POST"];
	
	//set headers
	NSString *contentType = [NSString stringWithFormat:format];
	[request addValue:contentType forHTTPHeaderField: @"Content-Type"];

	//create the POST body (optional)
	NSMutableData *postBody = [NSMutableData data];
	[postBody appendData:[[NSString stringWithFormat:@"<message>"] dataUsingEncoding:NSUTF8StringEncoding]];
	[postBody appendData:[[NSString stringWithFormat:@"<service/>"] dataUsingEncoding:NSUTF8StringEncoding]];
	[postBody appendData:[[NSString stringWithFormat:@"</message>"] dataUsingEncoding:NSUTF8StringEncoding]];
	
    //do POST
	[request setHTTPBody:postBody];
	
	//get response
	NSHTTPURLResponse* urlResponse = nil;  
	NSError *error = [[NSError alloc] init];  
	NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&urlResponse error:&error];  
	NSString *result = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
	NSLog(@"Response Code: %d", [urlResponse statusCode]);
	if ([urlResponse statusCode] >= 200 && [urlResponse statusCode] < 300) {
		NSLog(@"Response: %@", result);	                        
		printf(result); //here we output the response
	}

	[parser release];
}

@end
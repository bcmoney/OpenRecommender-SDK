//
//  XMLParser.m
//  XMLParserTutorial
//
//  Created by Kent Franks on 5/6/11.
//  Copyright 2011 TheAppCodeBlog. All rights reserved.
//

#import "XMLParser.h"
#import "Tweet.h"

@implementation XMLParser
@synthesize tweets;

-(id) loadXMLByURL:(NSString *)urlString
{
	tweets			= [[NSMutableArray alloc] init];
	NSURL *url		= [NSURL URLWithString:urlString];
	NSData	*data   = [[NSData alloc] initWithContentsOfURL:url];
	parser			= [[NSXMLParser alloc] initWithData:data];
	parser.delegate = self;
	[parser parse];
	return self;
}

- (void) parser:(NSXMLParser *)parser didStartElement:(NSString *)elementname namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict
{
	if ([elementname isEqualToString:@"status"]) 
	{
		currentTweet = [Tweet alloc];
	}
}

- (void) parser:(NSXMLParser *)parser didEndElement:(NSString *)elementname namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName
{
	if ([elementname isEqualToString:@"text"]) 
	{
		currentTweet.content = currentNodeContent;
	}
	if ([elementname isEqualToString:@"created_at"]) 
	{
		currentTweet.dateCreated = currentNodeContent;
	}
	if ([elementname isEqualToString:@"status"]) 
	{
		[tweets addObject:currentTweet];
		[currentTweet release];
		currentTweet = nil;
		[currentNodeContent release];
		currentNodeContent = nil;
	}
}

- (void) parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
	currentNodeContent = (NSMutableString *) [string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (void) dealloc
{
	[parser release];
	[super dealloc];
}

@end

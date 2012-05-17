//
//  XMLParser.h
//  XMLParserTutorial
//
//  Created by Kent Franks on 5/6/11.
//  Copyright 2011 TheAppCodeBlog. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Tweet.h"


@interface XMLParser : NSObject <NSXMLParserDelegate>
{

	NSMutableString	*currentNodeContent;
	NSMutableArray	*tweets;
	NSXMLParser		*parser;
	Tweet			*currentTweet;
	
}

@property (readonly, retain) NSMutableArray	*tweets;

-(id) loadXMLByURL:(NSString *)urlString;


@end

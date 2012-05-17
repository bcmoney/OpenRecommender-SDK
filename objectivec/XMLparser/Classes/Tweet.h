//
//  Tweet.h
//  XMLParserTutorial
//
//  Created by Kent Franks on 5/6/11.
//  Copyright 2011 TheAppCodeBlog. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Tweet : NSObject 
{
	NSString	 *content;
	NSString	 *dateCreated;
	
}

@property (nonatomic, retain) NSString	 *content;
@property (nonatomic, retain) NSString	 *dateCreated;

@end

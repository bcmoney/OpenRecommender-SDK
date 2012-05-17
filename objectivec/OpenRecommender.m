- (void)openXMLFile {


    NSArray *fileTypes = [NSArray arrayWithObject:@"xml"];

    NSOpenPanel *oPanel = [NSOpenPanel openPanel];

    NSString *startingDir = [[NSUserDefaults standardUserDefaults] objectForKey:@"StartingDirectory"];

    if (!startingDir)

        startingDir = NSHomeDirectory();

    [oPanel setAllowsMultipleSelection:NO];

    [oPanel beginSheetForDirectory:startingDir file:nil types:fileTypes

      modalForWindow:[self window] modalDelegate:self

      didEndSelector:@selector(openPanelDidEnd:returnCode:contextInfo:)

      contextInfo:nil];

}

 

- (void)openPanelDidEnd:(NSOpenPanel *)sheet returnCode:(int)returnCode contextInfo:(void *)contextInfo {

    NSString *pathToFile = nil;

    if (returnCode == NSOKButton) {

        pathToFile = [[[sheet filenames] objectAtIndex:0] copy];

    }

    if (pathToFile) {

        NSString *startingDir = [pathToFile stringByDeletingLastPathComponent];

        [[NSUserDefaults standardUserDefaults] setObject:startingDir forKey:@"StartingDirectory"];

        [self parseXMLFile:pathToFile];

    }

}


- (void)parseXMLFile:(NSString *)pathToFile {

    BOOL success;

    NSURL *xmlURL = [NSURL fileURLWithPath:pathToFile];

    if (addressParser) // addressParser is an NSXMLParser instance variable

        [addressParser release];

    addressParser = [[NSXMLParser alloc] initWithContentsOfURL:xmlURL];

    [addressParser setDelegate:self];

    [addressParser setShouldResolveExternalEntities:YES];

    success = [addressParser parse]; // return value not used

                // if not successful, delegate is informed of error

}


- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {

    if ( [elementName isEqualToString:@"recommendations"]) {
        // recommendations is an NSMutableArray instance variable
       if (!recommendations)

             recommendations = [[NSMutableArray alloc] init];

        return;
    }

 

    if ( [elementName isEqualToString:@"person"] ) {

        // currentPerson is an ABPerson instance variable
        currentPerson = [[ABPerson alloc] init];

        return;
    }

 
    if ( [elementName isEqualToString:@"lastName"] ) {

        [self setCurrentProperty:kABLastNameProperty];

        return;
    }

    // .... continued for remaining elements ....
}


- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {

    if (!currentStringValue) {

        // currentStringValue is an NSMutableString instance variable

        currentStringValue = [[NSMutableString alloc] initWithCapacity:50];

    }

    [currentStringValue appendString:string];

}

//Element parsing
- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {

    // ignore root and empty elements

    if (( [elementName isEqualToString:@"recommendations"]) ||

        ( [elementName isEqualToString:@"recommendation"] )) return;

 

    if ( [elementName isEqualToString:@"title"] ) {

        // recommendations and current recommendation Title are instance variables
        [recommendations addObject:currentTitle];

        [currentPerson release];

        return;

    }

    NSString *prop = [self currentProperty];

    // ... here ABMultiValue objects are dealt with ...
    if (( [prop isEqualToString:kABLastNameProperty] ) ||

        ( [prop isEqualToString:kABFirstNameProperty] )) {

        [currentPerson setValue:(id)currentStringValue forProperty:prop];

    }

    // currentStringValue is an instance variable
    [currentStringValue release];

    currentStringValue = nil;

}

//Attribute parsing
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {

    if ( [elementName isEqualToString:@"recommendation"]) {

        // recommendations is an NSMutableArray instance variable
        if (!recommendations)
            recommendations = [[NSMutableArray alloc] init];
        NSString *thisOwner = [attributeDict objectForKey:@"identifier"];

        if (thisOwner)

            [self setOwner:thisOwner forrecommendations:recommendations];

        return;

    // ... continued ...

}}
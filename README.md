-------------------------------------------------------------------------------
  ____                ___                                       __       
 / __ \___  ___ ___  / _ \___ _______  __ _  __ _  ___ ___  ___/ /__ ____
/ /_/ / _ \/ -_) _ \/ , _/ -_) __/ _ \/  ' \/  ' \/ -_) _ \/ _  / -_) __/
\____/ .__/\__/_//_/_/|_|\__/\__/\___/_/_/_/_/_/_/\__/_//_/\_,_/\__/_/   
    /_/                                                                  
    
OpenRecommender is a project aiming to build the world's leading open source Recommendation Engine.
Each folder/directory denotes a specific programming language implementation.

No guarantee of code quality is made, and these are simply provided as a basic starting point for your own implementations.
Knowledge of the programming language chosen will be required for running any of the client demos. For each example though, as long as the basic pre-requisites are setup on your host machine, they should be capable of being successfully compiled, run and/or viewed as required.
-------------------------------------------------------------------------------


OpenRecommender-SDK
===================

OpenRecommender Software Development Kit (SDK) companion libraries for quick integration.


-----------------------------------------------------------------------------------
*NOTE that some client examples may be incomplete, may not up-to-date with the latest version of the OpenRecommender or introduce some other errors or omissions.
-----------------------------------------------------------------------------------

The following is the complete list of language implementations and their requirements:
c/
  - gcc  GNU C Compiler or equivalent ANSI-C (1999) compatible compiler
  - make or nmake
   For more info, see:
   http://gcc.gnu.org/
   
cpp/ (C++)
  - g++  GNU C++ Compiler or equivalent ISO C++ standard (2011) compatible compiler
   For more info, see:
   http://gcc.gnu.org/projects/cxx0x.html
   For more info on installing in Windows, see:
   http://www.claremontmckenna.edu/pages/faculty/alee/g++/g++.html
   
csharp/
  - csc  C#(C-sharp) Compiler - depends on environment but requires either Visual Studio or open source implementation such as Mono
   For more info, see:
   http://msdn.microsoft.com/en-us/vstudio/
   For more open compiler options, see:
   http://www.mono-project.com/

flash/
  - To support basic in-player recommendations, only JW Player 3.16, or, JW Player 5.8+ can be used
  - JW Player versions 4.0 to 5.7 are not supported (since they had not fully implemented the JavaScript playlist and recommendation APIs) 
  - For standalone recommendations, must be able to compile ActionScript 3.0.
   For more info, see: 
   http://opensource.adobe.com/wiki/display/flexsdk/Flex+SDK

java/
  - Browser with Java Runtime Environment (JRE) native support, or, Java 1.6+ equivalent plugin
  - Operating System with Java Virtual Machine (JVM)
   For more info, see:
   http://www.java.com

javascript/
  - Browser with Javascript version 1.5+
  - Full implementation of the JavaScript DOM API
  - Native JSON support or permission to load a JSON library via JavaScript
   For more info, see:
   http://en.wikipedia.org/wiki/JavaScript#Versions
   To download the latest json2.js, see:
   http://github.com/douglascrockford/JSON-js

jquery/
  - jQuery-compatible Browser, for list see: 
   http://docs.jquery.com/Browser_Compatibility
   For more info, see:
   http://www.jquery.com

objectivec/
  - iOS device (iPhone, iPod, iPad, etc)
  - Apple Developer account
  - Xcode to compile
   For more info, see:
   http://developer.apple.com/devcenter/ios
   On Win32 or Linux systems, you could try: 
   http://gnustep.org/

perl/
  - Permission to install Perl modules on localhost
  - To run from command-line only, suggest:  Perl v5.12.4+
  - To view in browser, suggest Apache 2.2+ with:  mod_perl/2.0.4, Perl/v5.10.1
   For more info, see:
   http://www.perl.org

php/
  - To run from command-line only, suggest:  PHP v5.3.2+
  - To view in browser, suggest Apache 2.2+ with:  PHP/5.3.4
   For more info, see:
   http://www.php.net

python/
  - To run from command-line only, suggest:  Python v2.7.2
  - To view in browser, suggest Apache 2.2+ with:  mod_mod_wsgi/3.3, Python/2.7.2
   For more info, see:
   http://www.python.org

ruby/
  - To run from command-line only, suggest:  Ruby v1.9.2+
  - To view in browser, suggest Apache 2.2+ with:  
   For more info, see:
   http://www.ruby-lang.org/
   For help running in Apache Web Server on a Win32 system, see: http://editrocket.com/articles/ruby_apache_windows.html
   For help running in Apache Web Server on a Mac system, see: http://www.editrocket.com/articles/ruby_apache_mac.html
   For help running in Apache Web Server on a Linux system, see: http://wiki.gxtechnical.com/commwiki/servlet/hwiki?Linux+setup+for+running+Ruby

scala/
  - To run from command-line only, suggest: Scala v2.9.1+
  - To view in browser, suggest Apache Tomcat 5.5.26+ with:
  - "scala-library.jar" (the scala runtime) will need to be imported into project or included in Build Path
   For more info, see:
   http://www.scala-lang.org/
   For some hints on running within Apache Tomcat, see:
   http://www.softwaresecretweapons.com/jspwiki/run-scala-in-apache-tomcat-in-10-minutes

vb/
  - Win32 OS (95, 98, ME, XP, Vista, Windows 7, etc...)
  - Visual Studio compiler (express or pro)
   For more info, see: 
   http://msdn.microsoft.com/en-us/library/aa201750(v=office.10).aspx

xslt/
  - Browser with XSL 2.0+ support
   For more info, see:
   http://www.w3schools.com/xsl/xsl_browsers.asp


-----------------------------------------------------------------------------------
Win32 and Unix version tester scripts are provided to help check language versions:
 versions.bat
 versions.sh

-------------------------------------------------------------------------------
For more information, please refer to the project's website and community at:
http://openrecommender.org
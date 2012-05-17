#!C:\xampp\perl\bin\perl.exe
#WINDOWS
#!C:/Perl64/bin/perl.exe
#########################
#UNIX
#!/usr/bin/perl
#########################
use OpenRecommender;  # import all functions in OpenRecommener module
use CGI;  # Common Gateway Interface, for making the script accessible within a browser...

my $PERL_STYLE = "<style>img{border:0;vertical-align:middle;} ul{list-style-type:none;} li{background:#025486;border-bottom:1px dashed #fff;color:#ccc} li:hover {background:#ccc;color:#000} li:hover a{color:#025486} a{color:#f3f7fc}</style>";

my $cl = $ARGV[0]; #command-line
my $qp = CGI::param('url'); #query parameter

#default to some URL, try to use query parameter first, then command-line parameter
my $url = (defined($qp) && !($qp eq '')) ? $qp : (defined($cl) && !($cl eq '')) ? $cl : 'http://openrecommender.org/schema/XML/recommendations.xml';

##DEBUG:  print('Query Param: ' . $qp . ' | Command-line: ' . $cl . ' | URL: ' . $url . "\n\n");

######################################################################
## JSON - remote file, accessed via browser
if (defined($url) && ($url =~ m/http/i || $url =~ m/https/i) && $url =~ m/json/i) {
  my $recommendationList = openrecommenderJSON($url); # 'http://openrecommender.org/schema/JSON/recommendations.json'
  print "Content-type: text/html;";
  print "\n\n";
  print "<html><body>";
  print $PERL_STYLE;
  print $recommendationList;
  print "</body></html>";
}
######################################################################
## XML - remote file, accessed via browser
elsif (defined($url) && ($url =~ m/http/i || $url =~ m/https/i) && $url =~ m/xml/i) {
  my $recommendationList = openrecommenderXML($url); # 'http://openrecommender.org/schema/XML/recommendations.xml'
  print "Content-type: text/html;";
  print "\n\n";
  print "<html><head><link rel=\"shortcut icon\" href=\"./favicon.ico\" />";
  print $PERL_STYLE;
  print "<body>";    
  print $recommendationList;
  print "</body></html>";
}
######################################################################
## JSON - local file, accessed via command-line or script/job scheduler
elsif (defined($ARGV[0]) && defined($url) && $url =~ m/json/i) {
  print openrecommenderJSON($url); # 'recommendations.json'
}
######################################################################
## XML - local file, accessed via command-line or script/job scheduler
else {
  print openrecommenderXML($url);  # 'recommendations.xml'
}

package OpenRecommender;

use 5.010001;
use Data::Dumper; # useful for Debugging
use LWP::Simple; #HTTP interface for simplified access to libwww
use XML::Simple; # use Perl's SimpleXML module: http://search.cpan.org/~grantm/XML-Simple-2.18/lib/XML/Simple.pm
use JSON -support_by_pp; # native Perl JSON parser lib

require Exporter; #allow packaging of this file as a Perl Module (.pm)

our @ISA = qw(Exporter);
# Items to export into callers namespace by default. Note: do not export
# names by default without a very good reason. Use EXPORT_OK instead.
# Do not simply export all your public functions/methods/constants.

# This allows declaration	use OpenRecommender ':all';
# If you do not need this, moving things directly into @EXPORT or @EXPORT_OK
# will save memory.
our %EXPORT_TAGS = ( 'all' => [ qw(openrecommenderXML openrecommenderJSON makeRequest) ] );
our @EXPORT_OK = ( @{ $EXPORT_TAGS{'all'} } );
our @EXPORT = qw(openrecommenderXML openrecommenderJSON);

our $VERSION = '0.02';

##
# openrecommenderXML
#   process and display an OpenRecommender formatted XML
# @return recommend String representation of an OpenRecommender human-readable (HTML) set of recommendations
#
sub openrecommenderXML {
   
  # get this subroutine's calling parameter
	my ($xml_url) = @_;
  
	# create XML object
	my $xml = new XML::Simple(KeyAttr=>[]);
	
  my $recommendations = '';
	# load XML file into object
  if($xml_url =~ m/http/i || $xml_url =~ m/https/i) {
    ##DEBUG:  print "LOADING REMOTE XML FILE:\n ".makeRequest($xml_url);    
    $recommendations = $xml->XMLin(makeRequest($xml_url));
  }
  else {
    ##DEBUG:  print "LOADING LOCAL XML FILE:\n ".$xml_url;
    $recommendations = $xml->XMLin($xml_url);
  }		
	##DEBUG:  print Dumper($recommendations);
	
    # parse XML elements
  my $recommend = "";
	my $type = ref( $recommendations->{recommendation} );  
	if ($type ne ARRAY)	{  
	    $recommend .= "<ul>";
		    $recommend .= "<li>";
        $recommend .= "<a title='", $recommendations->{recommendation}->{description}, "' ";
        $recommend .= "href='", $recommendations->{recommendation}->{link}, "'>";
        $recommend .= "<img src='", $recommendations->{recommendation}->{image}, "' width='120' height='90' /> ";
        $recommend .= $recommendations->{recommendation}->{title};
        $recommend .= "</a> (", getRecommendationTitleDuration($title), ")";
		    $recommend .= "</li>";		  
      $recommend .= "</ul>";
	}
	else {
    $recommend .= "<ul>";
		foreach $recommendation(@{$recommendations->{recommendation}}) {
		   $title = getRecommendationTitle($recommendation);
		   $image = getRecommendationImage($recommendation);
		   $link = getRecommendationLink($recommendation);
		   $description = getRecommendationDescription($recommendation);		   
          $recommend .= "<li>";
          $recommend .= "<a id='" . getRecommendationIdentifier($recommendation) . "' ";
          $recommend .= "title='" . $description->{content} . "' ";
          $recommend .= "href='" . $link->{content} . "'>";
          $recommend .= "<img src='". $image->{content} . "' width='120' height='90' /> ";
          $recommend .= $title->{content};
          $recommend .= "</a> (" . getRecommendationTitleDuration($title) . ")";
            $recommend .= " Sender: ".getRecommendationSender($recommendation)." | ";
            $recommend .= "    Receiver: ".getRecommendationReceiver($recommendation)." | ";
            $recommend .= "    Date: ".getRecommendationDate($recommendation)." | ";			
            $recommend .= "    Time: ".getRecommendationTime($recommendation)." | ";
            $recommend .= "    Priority: ".getRecommendationPriority($recommendation)." | ";
            $recommend .= "    Type: ".getRecommendationType($recommendation);            
        $recommend .= "</li>";
		}
    $recommend .= "</ul>";
	}
	return $recommend;
}



####################################################################
## XML and JSON Parser
####################################################################
#<TITLE> Element getter
sub getRecommendationTitle {
	my $recommendation = $_[0];
  return $recommendation->{title};
}


#<IMAGE> Element getter
sub getRecommendationImage {
	my $recommendation = $_[0];
  return $recommendation->{image};
}

#<LINK> Element getter
sub getRecommendationLink {
	my $recommendation = $_[0];
  return $recommendation->{link};
}

#<DESCRIPTION> Element getter
sub getRecommendationDescription {
	my $recommendation = $_[0];
  return $recommendation->{description};
}


#<RECOMMENDATION> Attribute getter subroutines
sub getRecommendationIdentifier {
	my $recommendation = $_[0];
  return $recommendation->{identifier};
}

sub getRecommendationSender {
	my $recommendation = $_[0];
  return $recommendation->{sender};
}

sub getRecommendationReceiver {
	my $recommendation = $_[0];
  return $recommendation->{receiver};
}

sub getRecommendationDate {
	my $recommendation = $_[0];
  return $recommendation->{date};
}

sub getRecommendationTime {
	my $recommendation = $_[0];
  return $recommendation->{time};
}

sub getRecommendationPriority {
	my $recommendation = $_[0];
  return $recommendation->{priority};
}

sub getRecommendationType {
	my $recommendation = $_[0];
  return $recommendation->{type};
}

#<TITLE> Attribute getter subroutines
sub getRecommendationTitleDuration {
	my $title = $_[0];
  return $title->{duration};
}

sub getRecommendationTitleLanguage {
	my $title = $_[0];
  return $title->{language};
}

sub getRecommendationTitleCaptions {
	my $title = $_[0];
  return $title->{captions};
}

sub getRecommendationTitleSubtitles {
	my $title = $_[0];
  return $title->{subtitles};
}

sub getRecommendationTitlePlaylist {
	my $title = $_[0];
  return $title->{playlist};
}

#<IMAGE> Attribute getter subroutines
sub getRecommendationImageCreator {
	my $image = $_[0];
  return $image->{creator};
}

sub getRecommendationImagePublisher {
	my $image = $_[0];
  return $image->{publisher};
}

sub getRecommendationImageFormat {
	my $image = $_[0];
  return $image->{format};
}

sub getRecommendationImageRights {
	my $image = $_[0];
  return $image->{rights};
}

sub getRecommendationImageRightsHolder {
	my $image = $_[0];
  return $image->{rightsHolder};
}

#<LINK> Attribute getter subroutines
sub getRecommendationLinkShortlink {
	my $link = $_[0];
  return $link->{shortlink};
}

sub getRecommendationLinkLocation {
	my $link = $_[0];
  return $link->{location};
}

sub getRecommendationLinkMobile {
	my $link = $_[0];
  return $link->{mobile};
}

sub getRecommendationLinkMobileDownload {
	my $link = $_[0];
  return $link->{mobiledownload};
}

sub getRecommendationLinkMobileStream {
	my $link = $_[0];
  return $link->{mobilestream};
}

sub getRecommendationLinkOembed {
	my $link = $_[0];
  return $link->{oembed};
}

sub getRecommendationLinkCreator {
	my $link = $_[0];
  return $link->{creator};
}

sub getRecommendationLinkPublisher {
	my $link = $_[0];
  return $link->{publisher};
}

sub getRecommendationLinkFormat {
	my $link = $_[0];
  return $link->{format};
}

sub getRecommendationLinkRights {
	my $link = $_[0];
  return $link->{rights};
}

sub getRecommendationLinkRightsHolder {
	my $link = $_[0];
  return $link->{rightsHolder};
}

#<DESCRIPTION> Attribute getter subroutines
sub getRecommendationDescriptionRating {
	my $description = $_[0];
  return $description->{rating};
}

sub getRecommendationDescriptionReview {
	my $description = $_[0];
  return $description->{review};
}

sub getRecommendationDescriptionTags {
	my $description = $_[0];
  return $description->{tags};
}


##
# openrecommenderJSON
#   process and display an OpenRecommender formatted JSON
# @return recommend String representation of an OpenRecommender human-readable (HTML) set of recommendations
#
sub openrecommenderJSON {
	my $url = $_[0];

  my $json_string = '';
  # web request to fetch data remotely
  if($url =~ m/http/i || $url =~ m/https/i) {
    ##DEBUG:  print "LOADING REMOTE JSON FILE:\n ".makeRequest($url);
    $json_string = makeRequest($url);
  }
  # fetch data from local file
  else {
    ##DEBUG:  print "LOADING LOCAL JSON FILE:\n ";  
    $json_string;
    {
      local $/; #enable slurp
      open my $fh, "<", $url;
      $json_string = <$fh>;	  
    }    
  }
  my $json = from_json($json_string);
	
	##DEBUG json string:	print "JSON string: ".$json_string."\n";
	##DEBUG json object: print Dumper($json)."\n";	
	
  # parse JSON properties
  my $recommend = "";
	my $type = ref( $json->{recommendations}->{recommendation} );	  
	if ($type ne ARRAY)	{
	   my $title = $json->{recommendations}->{recommendation}->{title};
	   my $image = $json->{recommendations}->{recommendation}->{image};
	   my $link = $json->{recommendations}->{recommendation}->{link};
	   my $description = $json->{recommendations}->{recommendation}->{description};	   
	    $recommend .= "<ul>";
		    $recommend .= "<li>";
        $recommend .= "<a title='", $description->data, "' ";
        $recommend .= "href='", $link->data, "'>";
        $recommend .= "<img src='", $image->data, "' width='120' height='90' /> ";
        $recommend .= $title->data;
        $recommend .= "</a> (", getRecommendationTitleDuration($title), ")";
		    $recommend .= "</li>";		  
      $recommend .= "</ul>";        
	}
	else {
    $recommend .= "<ul>";    
		foreach $recommendation(@{$json->{recommendations}->{recommendation}}) {
		   $title = getRecommendationTitle($recommendation);
		   $image = getRecommendationImage($recommendation);
		   $link = getRecommendationLink($recommendation);
		   $description = getRecommendationDescription($recommendation);
          $recommend .= "<li>";
          $recommend .= "<a id='" . getRecommendationIdentifier($recommendation) . "' ";
          $recommend .= "title='" . $description->{data} . "' ";
          $recommend .= "href='" . $link->{data} . "'>";
          $recommend .= "<img src='". $image->{data} . "' width='120' height='90' /> ";
          $recommend .= $title->{data};
          $recommend .= "</a> (" . getRecommendationTitleDuration($title) . ")";
            $recommend .= " Sender: ".getRecommendationSender($recommendation)." | ";
            $recommend .= "    Receiver: ".getRecommendationReceiver($recommendation)." | ";
            $recommend .= "    Date: ".getRecommendationDate($recommendation)." | ";			
            $recommend .= "    Time: ".getRecommendationTime($recommendation)." | ";
            $recommend .= "    Priority: ".getRecommendationPriority($recommendation)." | ";
            $recommend .= "    Type: ".getRecommendationType($recommendation);
        $recommend .= "</li>";
		}
    $recommend .= "</ul>";  
	}
	return $recommend;
}


####################################################################
## URL parameters and command-line handling
####################################################################
sub makeRequest {
  my $url = $_[0]; #subroutine parameters

	my ($type, $length, $mod) = head($url);
	my $content = get($url);
	die "Can't GET $url" if (!defined $content);
  
	return $content; #"Content-type: ",$type,"\n\n",$content;
}



1;
__END__

=head1 NAME

OpenRecommender - Perl extension for generating Recommendations

=head1 SYNOPSIS

  use OpenRecommender;
  print openrecommenderXML('recommendations.xml');

=head1 DESCRIPTION

OpenRecommender is an open source Recommendation Engine and piece of 
Enterprise Intregation middleware that hooks into Mahout to provide 
highly relevant recommendations in real-time.

=head2 EXPORT

openrecommenderXML by default.



=head1 SEE ALSO

Mention other useful documentation such as the documentation of
related modules or operating system documentation (such as man pages
in UNIX), or any relevant external documentation such as RFCs or
standards.

To join the mailing list, see: http://openrecommender.org/register/

For more information see: http://openrecommender.org

=head1 AUTHOR

BCmoney, E<lt>bc@bcmoney-mobiletv.comE<gt>

=head1 COPYRIGHT AND LICENSE

Copyright (C) 2011 by BCmoney

This library is free software; you can redistribute it and/or modify
it under the same terms as Perl itself, either Perl version 5.12.4 or,
at your option, any later version of Perl 5 you may have available.

=cut

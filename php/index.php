<?php

require_once "OpenRecommender.class.php";

$openrecommender = new OpenRecommender(); //parsing XML
	// parsing JSON:  $openrecommender = new OpenRecommender("recommendations.json", "json"); 
//DEBUG (string):	echo $openrecommender->display(); 
//DEBUG (object): echo $openrecommender->output();

$php_style = '<style>img{border:0;vertical-align:middle;} ul{list-style-type:none;} li{background:#5664a3;border-bottom:1px dashed #fff} li:hover {background:#dddce9} li:hover a{color:#5664a3} a{color:#fff}</style>';

$recommendations = $openrecommender->getRecommendations();

$list = '';
if (count($recommendations) > 0) {
  $list .= '<ul>';
	foreach ($recommendations as $recommendation) {
		$list .= '<li><a href="'.$openrecommender->getRecommendationLink($recommendation).'" title="'.$openrecommender->getRecommendationDescription($recommendation).'"><img src="'.$openrecommender->getRecommendationImage($recommendation).'" width="120" height="90"/>'.$openrecommender->getRecommendationTitle($recommendation).'</a></li>';
	}
  $list .= '</ul>';
}

?>
<html><head><link rel="shortcut icon" href="./favicon.ico" />
<?php echo $php_style; ?>
</head>
<body>
<?php echo $list; ?>
</body>
</html>
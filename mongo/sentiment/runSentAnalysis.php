<?php
header('Content-Type: text/html; charset=UTF-8');
include_once "stemmer-es1.0/stemm_es.php";

ini_set('mongo.native_long', 1);

//Open Mongo connection
$conn = new Mongo();
$db = $conn->selectDB('sentiment');

$tweetsCur = $db->president->find(array(), array("text"=>true));

foreach ($tweetsCur as $tweet) {
    echo $tweet['text']."<br/>";
    
    $words = explode(' ', $tweet['text']);
    $scoreTweet = 0;
    $negation = false;
    
    foreach ($words as $index=>$word) {
        if (startsWith($word, '@') || startsWith($word, '"@') || startsWith($word, 'http://')) {
            unset($words[$index]);
            continue;
        }
        
        if (strtolower($word) == "no") {
            $negation = true;
        }
        
        $word = cleanSpecialChars($word);
        $word = cleanEmoticonsValues($word);
        $words[$index] = stemm_es::stemm(preg_replace('/[^[:alnum:]]/', '', $word));
        
        $entry = $db->lexicon->findOne(array('stem1'=>$words[$index]));
        if ($entry != null) {
            $polarity = $entry['priorpolarity'];
            $scoreLocal = 0;
            $type = $entry['type'];
            
            if (startsWith($type, 'weak')) {
                $scoreLocal = 1;
            } else if (startsWith($type, 'strong') >= 0) {
                $scoreLocal = 3;
            }

            if ($polarity == "negative") {
                $scoreLocal *= -1;
            } else if ($polarity != "positive") {
                $scoreLocal = 0;
            }

            $scoreTweet += $scoreLocal;
            $words[$index] .= "(".$scoreLocal.")";
        }
            
    }
    
    if ($negation) {
        $scoreTweet *= -1;
    }
    
    //
    echo (implode(' ', $words))."<br/>";
    echo $scoreTweet;
    if ($scoreTweet > 0) {
        echo " :-)";
    }
    else if ($scoreTweet < 0) {
        echo " :-(";
    }
    else {
        echo " :-|";
    }
    echo "<hr/>";
}


/**
 * Starts with as in Java
 * @param type $haystack
 * @param type $needle
 * @return type 
 */
function startsWith($haystack, $needle)
{
    $length = strlen($needle);
    return (substr($haystack, 0, $length) === $needle);
}

/**
 * Cleans spanish accent punctuation
 * @param type $s
 * @return type 
 */
function cleanSpecialChars($s) {
    $s = str_replace("á","a",$s);
    $s = str_replace("Á","a",$s);
    $s = str_replace("é","e",$s);
    $s = str_replace("É","e",$s);
    $s = str_replace("í","i",$s);
    $s = str_replace("Í","i",$s);
    $s = str_replace("ó","o",$s);
    $s = str_replace("Ó","o",$s);
    $s = str_replace("ú","u",$s);
    $s = str_replace("Ú","u",$s);
    $s = str_replace("ñ","n",$s);
    $s = str_replace("Ñ","n",$s);

    return $s;
}


function cleanEmoticonsValues($s) {
    $s = str_replace(":-)","emotPosWeak",$s);
    $s = str_replace(":)","emotPosWeak",$s);
    $s = str_replace(":-D","emotPosStrong",$s);
    $s = str_replace(":D","emotPosStrong",$s);
    
    $s = str_replace(":-(","emotNegWeak",$s);
    $s = str_replace(":(","emotNegWeak",$s);
    
    return $s;
}
<?php
ini_set('error_reporting', E_ALL);
header('Content-Type: text/html; charset=UTF-8');

$word = $_GET['word'];
$strength = $_GET['strength'];
$pos = $_GET['pos'];
$polarity = $_GET['polarity'];
echo $word;

// Get translation
$url = "http://api.wordreference.com/f6bf8/json/enes/".$word;

//Sends request
$json = file_get_contents_utf8($url, true);
$translation = json_decode($json, true);
$wordsSpanish = array();

//Extracts spanish translations
if (isset($translation['term0'])) {
    if (isset($translation['term0']['PrincipalTranslations'])) {
        foreach ($translation['term0']['PrincipalTranslations'] as $t) {
            $w = explode(",", $t['FirstTranslation']['term']);

            foreach($w as $wordTr) {
                array_push($wordsSpanish, trim($wordTr));
            }

        }
    }

    if (isset($translation['term0']['Entries'])) {
        foreach ($translation['term0']['Entries'] as $t) {
            $w = explode(",", $t['FirstTranslation']['term']);

            foreach($w as $wordTr) {
                array_push($wordsSpanish, trim($wordTr));
            }
        }
    }
}


//Insert word
echo insertEntry($wordsSpanish, trim($strength), trim($pos), trim($polarity));


function insertEntry($wordsSpanish, $type, $pos, $polarity) {
    
    if (sizeof($wordsSpanish) > 0) {

        //Open Mongo connection
        $conn = new Mongo();
        $db = $conn->selectDB('sentiment');

        foreach ($wordsSpanish as $wordSpanish) {
    
            $entry = array('type' => $type, 'len' => 1, 'word1' => $wordSpanish,
                'pos1' => $pos, 'stemmed1' => 'y', 'priorpolarity' => $polarity);
            
            $countExists = $db->lexicon->find(array('word1'=>$wordSpanish))->count();
            
            if ($countExists == 0) {
                $db->lexicon->save($entry);
            }
        }
        return ":success";
    } else { 
        return "error";
    }
}
       
function file_get_contents_utf8($fn) { 
    $opts = array( 
        'http' => array( 
            'method'=>"GET", 
            'header'=>"Content-Type: text/html; charset=utf-8" 
        ) 
    ); 

    $context = stream_context_create($opts); 
    $result = @file_get_contents($fn,false,$context); 
    return $result; 
} 

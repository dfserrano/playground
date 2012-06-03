<?php
header('Content-Type: text/html; charset=UTF-8');
include_once "stemmer-es1.0/stemm_es.php";

if (isset($_POST['word'])) {
    $word = $_POST['word'];
    $strength = $_POST['strength'];
    $polarity = $_POST['type'];
    $stem = stemm_es::stemm($word);
    
    $entry = array('type' => $polarity, 'len' => 1, 'word1' => $word,
                'stemmed1' => 'y', 'priorpolarity' => $polarity, 'stem1' => $stem);
    
    //Open Mongo connection
    $conn = new Mongo();
    $db = $conn->selectDB('sentiment');
    
    $db->lexicon->save($entry);
}
?>
<html>
    <head>
        
    </head>
    <body>
        <form id="form" name="form" method="post">
            Word
            <input type="text" id="word" name="word"/>
            <br/>
            Strength
            <select id="strength" name="strength">
                <option value="weak">Weak</option>
                <option value="strong">Strong</option>
            </select>
            <br/>
            Polarity
            <select id="type" name="type">
                <option value="positive">Positive</option>
                <option value="negative">Negative</option>
            </select>
            <input type="submit" value="Submit" />
        </form>
    </body>
</html>
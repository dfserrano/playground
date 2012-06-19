<?php
ini_set('max_execution_time', 3000);

require_once "util/collections/Queue.php";
$seedUsername = "dfserrano";
$seedUserId = 68494040;

$queue = new Queue;

//get followers from seed
$url = "https://api.twitter.com/1/followers/ids.json?cursor=-1&screen_name=".$seedUsername;

//Sends request
$json = file_get_contents_utf8($url, true);
$followers = json_decode($json, true);

if (isset($followers['ids'])) {
    $followers = $followers['ids'];
    
    foreach ($followers as $follower) {
        $queue->enqueue($follower);
        echo $seedUserId.' '.$follower.'<br/>';
    }
}

while (! $queue->isEmpty()) {
    $user = $queue->dequeue();
    
    //Followers
    $url = "https://api.twitter.com/1/followers/ids.json?cursor=-1&user_id=".$user;
    $json = file_get_contents_utf8($url, true);
    $followers = json_decode($json, true);

    if (isset($followers['ids'])) {
        $followers = $followers['ids'];

        foreach ($followers as $follower) {
            echo $user.' '.$follower.'<br/>';
        }
    } else {
        echo "Could not retrieve followers from $user <br/>";
    }
    
    //Friends
    $url = "https://api.twitter.com/1/friends/ids.json?cursor=-1&user_id=".$user;
    $json = file_get_contents_utf8($url, true);
    $friends = json_decode($json, true);

    if (isset($friends['ids'])) {
        $friends = $friends['ids'];

        foreach ($friends as $friend) {
            echo $friend.' '.$user.'<br/>';
        }
    } else {
        echo "Could not retrieve friends from $user <br/>";
    }
}

echo "END";
?>

<?php
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
?>
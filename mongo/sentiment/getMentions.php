<?php
header('Content-Type: text/html; charset=UTF-8');
?>
<html>
    <head>
        <script type="text/JavaScript">
        function timedRefresh(timeoutPeriod) {
            //Reload page
            setTimeout("location.reload(true);",timeoutPeriod);
        }
        </script>
    </head>
    <body onload="JavaScript:timedRefresh(60*5*1000);">
        <script>
            var objToday = new Date(),
            weekday = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'),
            dayOfWeek = weekday[objToday.getDay()],
            domEnder = new Array( 'th', 'st', 'nd', 'rd', 'th', 'th', 'th', 'th', 'th', 'th' ),
            dayOfMonth = today + (objToday.getDate() < 10) ? '0' + objToday.getDate() + domEnder[objToday.getDate()] : objToday.getDate() + domEnder[parseFloat(("" + objToday.getDate()).substr(("" + objToday.getDate()).length - 1))],
            months = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'),
            curMonth = months[objToday.getMonth()],
            curYear = objToday.getFullYear(),
            curHour = objToday.getHours() > 12 ? objToday.getHours() - 12 : (objToday.getHours() < 10 ? "0" + objToday.getHours() : objToday.getHours()),
            curMinute = objToday.getMinutes() < 10 ? "0" + objToday.getMinutes() : objToday.getMinutes(),
            curSeconds = objToday.getSeconds() < 10 ? "0" + objToday.getSeconds() : objToday.getSeconds(),
            curMeridiem = objToday.getHours() > 12 ? "PM" : "AM";
            var today = curHour + ":" + curMinute + "." + curSeconds + curMeridiem + " " + dayOfWeek + " " + dayOfMonth + " of " + curMonth + ", " + curYear;
            document.write(today);
        </script>
        <?php
        // default user (our president)
        $userMentioned = "@JManuelSantos";
        if ($_GET['user']) {
            $userMentioned = '@'.$_GET['user'];
        }
        
        ini_set('mongo.native_long', 1);

        //Open Mongo connection
        $conn = new Mongo();
        $db = $conn->selectDB('sentiment');

        //Find last id
        $lastTweetCur = $db->president->find(array(), array("id"=>true))->sort(array("id"=>-1))->limit(1);
        $sinceId = 0;
        if ($lastTweetCur->hasNext()) {
            $lastTweet = $lastTweetCur->getNext();
            $sinceId = $lastTweet['id'];
        }

        //Sets url
        $url = "http://search.twitter.com/search.json?q=".$userMentioned."&include_entities=false&result_type=mixed";

        if ($sinceId != 0) {
            $url .= "&since_id=".$sinceId;
            echo "<br/>Fetching from URL= ".$url."<br/><br/>"; 
        }

        //Sends request
        $json = file_get_contents_utf8($url, true);
        $tweets = json_decode($json, true);

        if (isset($tweets['results'])) {
            foreach($tweets['results'] as $tweet) {
                $id = $tweet['id'];
                $date = $tweet['created_at'];
                $user = $tweet['from_user'];
                $text = $tweet['text'];

                $tweet = array('id'=>$id, 'created_at'=>$date, 'from_user'=>$user, 'text'=>$text);

                echo $tweet['id'].'<br/>';
                echo $tweet['created_at'].'<br/>';
                echo $tweet['from_user'].'<br/>';
                echo $tweet['text'].'<br/><hr/><br/>';

                $db->president->save($tweet);
            }
        }
        ?>
    </body>
</html>

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
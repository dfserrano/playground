<?php
ini_set('max_execution_time', 3000);
ini_set('memory_limit','512M');
error_reporting(E_ALL);
ini_set("display_errors", 1);

require_once dirname(__FILE__) . '/Graph.php';
require_once dirname(__FILE__) . '/algorithms/ranking/Pagerank.php';

$graph = new Graph;

echo "Reading test data <br/>";

$file_handle = fopen("test-data", "r");
while (!feof($file_handle)) {
    $line = fgets($file_handle);

    $users = explode(' ', $line);
    $source = strval(trim($users[0])); 
    $target = strval(trim($users[1]));

    $graph->addLink($source, $target, 1);
    //break;
}
fclose($file_handle);
echo "Finished reading test file<br/>";

echo "Calculating Pagerank<br/>";
$pagerank = new Pagerank($graph);
$scores = $pagerank->compute();
echo "Finish calculation<br/>";

echo "Sort scores<br/>";
echo "=================<br/>";
arsort($scores, SORT_NUMERIC);

foreach ($scores as $id=>$score) {
    echo $graph->identifierToString($id)." = ".$score."<br/>";
}
?>

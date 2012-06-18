<?php
require_once dirname(__FILE__) . '/Graph.php';
require_once dirname(__FILE__) . '/algorithms/ranking/Pagerank.php';

$graph = new Graph;

$graph->addLink("A", "B", 1);
$graph->addLink("A", "C", 1);
$graph->addLink("B", "C", 1);
$graph->addLink("C", "A", 1);

echo $graph;

$pagerank = new Pagerank($graph);
$pagerank->compute();

echo "<br/>A = ".$pagerank->pageRankByString("A");
echo "<br/>B = ".$pagerank->pageRankByString("B");
echo "<br/>C = ".$pagerank->pageRankByString("C");
?>

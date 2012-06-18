<?php
require_once dirname(__FILE__) . '/../../Graph.php';
require_once dirname(__FILE__) . '/../../util/collections/Stack.php';
require_once dirname(__FILE__) . '/../../util/collections/Queue.php';
require_once dirname(__FILE__) . '/../../util/collections/BinaryHeap.php';
require_once dirname(__FILE__) . '/../../util/collections/comparators/ComparableNode.php';

class Betweenness {
    
    private $graph;
    private $scores;
    
    public function __construct($graph) {
        if (!$graph instanceof Graph) {
            //Exception
        }
        $this->graph = $graph;
        $this->scores = array();
    }
    
    private function initialize() {
        $nodes = $this->graph->getNodes();
        $nodes = array_keys($nodes);
        $this->scores = array();
        
        foreach ($nodes as $u) {
            $this->scores[$u] = 0;
        }
    }
    
    
    public function compute() {
        $this->initialize();
        
        $nodes = $this->graph->getNodes();
        $nodes = array_keys($nodes);
        
        foreach ($nodes as $s) {
            //Initialization
            foreach ($nodes as $i) {
                $previous[$i] = array();
                $distance[$i] = PHP_INT_MAX;
                $shortestpaths[$i] = 0;
            }
            $shortestpaths[$s] = 1;
            $distance[$s] = 0;
            
            //Modified BFS
            $queue = new Queue;
            $queue->enqueue($s);
            
            $stack = new Stack;
            
            while (!$queue->isEmpty()) {
                //vertex in Q with smallest distance in dist[]
                $v = $queue->dequeue();
                $stack->push($v);
                
                // for each neighbor w of v where v has not yet been removed from Q.
                $neighbors = $this->graph->outLinksById($v);
                
                foreach ($neighbors as $w=>$weight) {
                    if ($distance[$w] == PHP_INT_MAX) {
                        $queue->enqueue($w);
                        $distance[$w] = $distance[$v] + 1;
                    }
                    
                    if ($distance[$w] == $distance[$v] + 1) {
                        $shortestpaths[$w] += $shortestpaths[$v];
                        $previous[$w][] = $v;
                    }
                    
                }
            }
            
            //Initilize partial dependencies
            $partialDependency = array();
            foreach ($nodes as $i) {
                $partialDependency[$i] = 0;
            }
            
            // Set partial dependencies
            while (!$stack->isEmpty()) {
                $w = $stack->pop();
                foreach ($previous[$w] as $v) {
                    $partialDependency[$v] +=  ($shortestpaths[$v]/$shortestpaths[$w]) * (1 + $partialDependency[$w]);
                }
                
                if ($w != $s) {
                    $this->scores[$w] += $partialDependency[$w];
                }
            }
        }
        
        return $this->scores;
    }
    
}


// Check Examples here 
// http://reference.wolfram.com/mathematica/ref/BetweennessCentrality.html
$graph1 = new Graph();
addBiLink($graph1, "A", "B", 1);
addBiLink($graph1, "B", "C", 1);
$d = new Betweenness($graph1);
$d->compute();

$graph2 = new Graph();
addBiLink($graph2, "A", "B", 1);
addBiLink($graph2, "B", "C", 1);
addBiLink($graph2, "D", "E", 1);
addBiLink($graph2, "E", "F", 1);
$d = new Betweenness($graph2);
$d->compute();

$graph3 = new Graph();
$graph3->addLink("A", "B", 1);
$graph3->addLink("B", "C", 1);
$graph3->addLink("C", "A", 1);
$d = new Betweenness($graph3);
$d->compute();

$graph4 = new Graph();
$graph4->addLink("A", "B", 1);
$graph4->addLink("B", "C", 1);
$graph4->addLink("C", "A", 1);
$graph4->addLink("D", "C", 1);
$graph4->addLink("D", "E", 1);
$graph4->addLink("E", "F", 1);
$graph4->addLink("F", "D", 1);
$d = new Betweenness($graph4);
$d->compute();

function addBiLink(&$g, $source, $target, $weight) {
    $g->addLink($source, $target, $weight);
    $g->addLink($target, $source, $weight);
}
?>

<?php
require_once dirname(__FILE__) . '/../../Graph.php';
require_once dirname(__FILE__) . '/../../util/collections/Stack.php';
require_once dirname(__FILE__) . '/../../util/collections/Queue.php';
require_once dirname(__FILE__) . '/../../util/collections/BinaryHeap.php';
require_once dirname(__FILE__) . '/../../util/collections/comparators/ComparableNode.php';

class BetweennessWeighted {
    
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
    
        foreach ($nodes as $i) {
            $this->scores[$i] = 0;
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
            
            //Modified Dijkstra
            $queue = new BinaryHeap;
        
            // All nodes in the graph are unoptimized - thus are in Q
            foreach ($distance as $id=>$dist) {
                $queue->add(new ComparableNode($id, $dist));
            }
            
            $stack = new Stack;
            
            while (!$queue->isEmpty()) {
                //vertex in Q with smallest distance in dist[]
                $uNode = $queue->remove();
                $u = $uNode->getId();
                $stack->push($u);
                
                // all remaining vertices are inaccessible from source
                if ($distance[$u] == PHP_INT_MAX) {
                    break;
                }

                // for each neighbor v of u where v has not yet been removed from Q.
                $neighbors = $this->graph->outLinksById($u);
                foreach ($neighbors as $v=>$weight) {
                    $pos = $queue->indexOf($v);
                    if ($pos != -1) {
                        $alt = $distance[$u] + $weight;

                        // Relax
                        if ($alt <= $distance[$v]) {
                            if ($alt < $distance[$v]) {
                                $distance[$v] = $alt;
                                $previous[$v] = array($u);
                                $shortestpaths[$v] = $shortestpaths[$u];

                                // Reorder v in the Queue
                                $queue->decreaseKey($pos, $alt);  
                            } else {
                                // =
                                $shortestpaths[$v] += $shortestpaths[$u];
                                $previous[$v][] = $u;
                            }
                        }
                    }
                }
            }
            //---
            
            $partialDependency = array();
            foreach ($nodes as $i) {
                $partialDependency[$i] = 0;
            }
            
            while (!$stack->isEmpty()) {
                $w = $stack->pop();
                foreach ($previous[$w] as $v) {
                    $partialDependency[$v] +=  ($shortestpaths[$v]/$shortestpaths[$w]) * (1 + $partialDependency[$w]);
                }
                
                if ($w!=$s) {
                    
                    $this->scores[$w] += $partialDependency[$w];
                }
            }
        }
        
        return $this->scores;
    }
}



$graph = new Graph();
addBiLink($graph, "A", "B", 1);
addBiLink($graph, "B", "C", 1);
addBiLink($graph, "B", "D", 1);
addBiLink($graph, "C", "E", 1);
addBiLink($graph, "D", "E", 1);

$d = new BetweennessWeighted($graph);
$d->compute();

function addBiLink(&$g, $source, $target, $weight) {
    $g->addLink($source, $target, $weight);
    $g->addLink($target, $source, $weight);
}
?>

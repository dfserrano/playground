<?php
require_once dirname(__FILE__) . '../../util/collections/Stack.php';
require_once dirname(__FILE__) . '/../../util/collections/Queue.php';
require_once dirname(__FILE__) . '/../../util/collections/BinaryHeap.php';
require_once dirname(__FILE__) . '/../../util/collections/comparators/ComparableNode.php';
require_once dirname(__FILE__) . '/../../Graph.php';
require_once dirname(__FILE__) . '/ShortestPathsAlgorithm.php';

class Dijkstra extends ShortestPathsAlgorithm {
    
    public function __construct($graph) {
        parent::__construct($graph);
    }
    
    private function initialize() {
        $nodes = $this->graph->getNodes();
        
        foreach ($nodes as $id=>$string) {
            // Unknown distance function from source to v
            $this->distance[$id] = PHP_INT_MAX;
            
            // Previous node in optimal path from source
            $this->previous[$id] = PHP_INT_MAX;
        }
    }
    
    private function initializePriorityQueue() {
        $heap = new BinaryHeap;
        
        // All nodes in the graph are unoptimized - thus are in Q
        foreach ($this->distance as $id=>$dist) {
            $heap->add(new ComparableNode($id, $dist));
        }
        
        return $heap;
    }
    
    public function compute($sourceId) {
        
        $this->initialize();
        
        // Distance from source to source
        $this->distance[$sourceId] = 0;
        
        $q = $this->initializePriorityQueue();

        while (!$q->isEmpty()) {
            //vertex in Q with smallest distance in dist[]
            $uNode = $q->remove();
            $u = $uNode->getId();
            
            // all remaining vertices are inaccessible from source
            if ($this->distance[$u] == PHP_INT_MAX) {
                break;
            }
            
            // for each neighbor v of u where v has not yet been removed from Q.
            $neighbors = $this->graph->outLinksById($u);
            foreach ($neighbors as $v=>$weight) {
                $pos = $q->indexOf($v);
                if ($pos != -1) {
                    $alt = $this->distance[$u] + $weight;
                    
                    // Relax
                    if ($alt < $this->distance[$v]) {
                        $this->distance[$v] = $alt;
                        $this->parent[$v] = $u;
                        
                        // Reorder v in the Queue
                        $q->decreaseKey($pos, $alt);                    
                    }
                }
            }
        }
        
        $this->printDistances();
    }
    
    private function printDistances() {
        foreach ($this->distance as $id=>$dist) {
            echo $this->graph->identifierToString($id)." = ".$dist."<br/>";
        }
    }
}


$graph = new Graph();
addBiLink($graph, "A", "B", 2);
addBiLink($graph, "A", "D", 1);
addBiLink($graph, "A", "C", 5);
addBiLink($graph, "B", "C", 3);
addBiLink($graph, "B", "D", 2);
addBiLink($graph, "C", "D", 3);
addBiLink($graph, "C", "E", 1);
addBiLink($graph, "C", "F", 5);
addBiLink($graph, "D", "E", 1);
addBiLink($graph, "E", "F", 2);

$d = new Dijkstra($graph);
$d->compute(1);

function addBiLink(&$g, $source, $target, $weight) {
    $g->addLink($source, $target, $weight);
    $g->addLink($target, $source, $weight);
}
?>
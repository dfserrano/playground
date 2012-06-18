<?php
abstract class ShortestPathsAlgorithm {
    
    protected $graph;
    protected $distance = array();
    protected $previous = array();
    
    public function __construct($graph) {
        $this->graph = $graph;
    }
    
    abstract public function compute($sourceId);
    
    public function getDistance() {
        return $this->distance;
    }
    
    public function getPrevious() {
        return $this->previous;
    }
}
?>

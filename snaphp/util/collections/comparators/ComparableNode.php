<?php
require_once 'Comparable.php';

class ComparableNode implements Comparable {
    
    private $nodeId;
    private $score;
    
    public function __construct($nodeId, $score) {
        $this->nodeId = $nodeId;
        $this->score = $score;
    }
    
    public function compareTo($other) {
        if ($this->score > $other->score) {
            return 1;
        } else if ($this->score < $other->score) {
            return -1;
        } 
        
        return 0;
    }
    
    public function getId() {
        return $this->nodeId;
    }

    public function setId($nodeId) {
        $this->nodeId = $nodeId;
    }

    public function getValue() {
        return $this->score;
    }

    public function setValue($score) {
        $this->score = $score;
    }
    
    public function __toString() {
        return $this->nodeId." (".$this->score.")";
    }
}
?>

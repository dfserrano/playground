<?php

class ComparableNumber {
    public $x;
    
    public function __construct($x) {
        $this->x = $x;
    }
    
    public function compareTo($number) {
        if ($this->x > $number->x) {
            return 1;
        } else if ($this->x < $number->x) {
            return -1;
        } 
        
        return 0;
    }
    
    public function __toString() {
        return strval($this->x);
    }
}
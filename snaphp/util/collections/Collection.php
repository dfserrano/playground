<?php

class Collection {
    
    protected $list = array();
    
    public function size() {
        return sizeof($this->list);
    }
    
    public function isEmpty() {
        return (sizeof($this->list) == 0)? true : false;
    }
    
    public function contains($object) {
        foreach ($this->list as $element) {
            if ($element->compareTo($object) == 0) {
                return true;
            }
        }
    }
}
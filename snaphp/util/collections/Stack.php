<?php
require_once dirname(__FILE__) . '/Collection.php';

class Stack extends Collection {
    
    public function push($element) {
        array_push($this->list, $element);
    }
    
    public function pop() {
        return array_pop($this->list);
    }
    
    public function __toString() {
        $string = "";
        foreach ($this->list as $e) {
            $string .= $e." - ";
        }
        $string .= ">>";
        return $string;
    }
}

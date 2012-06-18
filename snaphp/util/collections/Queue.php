<?php
require_once dirname(__FILE__) . '/Collection.php';

class Queue extends Collection {
    
    public function enqueue($element) {
        array_push($this->list, $element);
    }
    
    public function dequeue() {
        return array_shift($this->list);
    }
}

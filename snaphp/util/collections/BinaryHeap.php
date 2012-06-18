<?php
require_once dirname(__FILE__) . '/Collection.php';

class BinaryHeap extends Collection {
    
    public $list = array();
    
    public function __construct($nodes = null) {
        if ($nodes != null) {
            foreach ($nodes as $node) {
                $this->list[] = $node;
            }
            
            for ($pos = sizeof($this->list)/2 - 1; $pos>=0; $pos--) {
                $this->moveDown($pos);
            }
        }
    }
    
    
    public function add($node) {
        $this->list[] = $node;
        $this->moveUp(sizeof($this->list)-1);
    }
    
    
    public function moveUp($pos) {
        while ($pos > 0) {
            $parent = ($pos - 1) / 2;
            
            if ($this->list[$pos]->compareTo($this->list[$parent]) >= 0) {
                break;
            }
            
            //swap
            $temp = $this->list[$pos];
            $this->list[$pos] = $this->list[$parent];
            $this->list[$parent] = $temp;
            
            $pos = $parent;
        }
    }
    
    
    public function remove() {
        $removedNode = $this->list[0];
        $lastNode = array_pop($this->list);
        
        if (sizeof($this->list) > 0) {
            $this->list[0] = $lastNode;
            $this->moveDown(0);
        }
        
        return $removedNode;
    }
    
    
    public function moveDown($pos) {
        while ($pos < sizeof($this->list) / 2) {
            $child = 2 * $pos + 1;
            
            if ($child < sizeof($this->list)-1 && 
                    $this->list[$child]->compareTo($this->list[$child+1]) > 0) {
                ++$child;
            }
            
            if (isset($this->list[$child]) && $this->list[$pos]->compareTo($this->list[$child]) <= 0) {
                break;
            }
            
            //swap
            if (isset($this->list[$child])) {
                $temp = $this->list[$pos];
                $this->list[$pos] = $this->list[$child];
                $this->list[$child] = $temp;
            }
            
            $pos = $child;
        }
    }
    
    /**
     * Gets position of the node with the specified id
     * @param type $id
     * @return int Position of the node with the specified id.  Return -1 if not found
     */
    public function indexOf($id) {
        for ($i=0; $i<sizeof($this->list); $i++) {
            if ($this->list[$i]->getId() == $id) {
                return $i;
            }
        }
        
        return -1;
    }
    
    
    public function decreaseKey($pos, $newValue) {
        $this->list[$pos]->setValue($newValue);
        $this->moveUp($pos);
    }
    
    public function increaseKey($pos, $newValue) {
        $this->list[$pos]->setValue($newValue);
        $this->moveDown($pos);
    }
    
    public function isEmpty() {
        return (sizeof($this->list) == 0)? true : false;
    }
    
    public function __toString() {
        $string = "";
        for ($i=0; $i<sizeof($this->list); $i++) {
            $string .= $this->list[$i]." - ";            
        }
        return $string;
    }
}

/*$heap = new BinaryHeap;
$numElements = rand(5, 30);

for ($i=0; $i<$numElements; $i++) {
    $heap->add(new ComparableNumber(rand(1, 100)));
}

// print elements in sorted order
for ($i=0; $i<$numElements; $i++) {
    $x = $heap->remove();
    echo "<br/>--$x--<br/>";
}*/

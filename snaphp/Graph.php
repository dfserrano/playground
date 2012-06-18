<?php
/**
 * This class implements a memory Data Structure for storing graphs.</p><p>
 * 
 * This class provides the methods needed to efficiently compute with graphs and to
 * experiment with such algorithms, using main memory for storage.
 *
 * @author Diego Serrano
 * Adapted from Bruno Martins implementation for WebLA
 */
class Graph {

    /**
     * An associative array (map) storing relationships from numeric identifiers 
     * to strings
     */
    private $identifierToString;

    /**
     * An associative array (map) storing relationships from strings
     * to numeric identifiers.
     */
    private $stringToIdentifier;

    /**
     * An associative array (map) storing inLinks. For each identifier (the key), 
     * another array is stored, containing for each inlink an associated weight
     */
    private $inLinks;

    /**
     * An associative array (map) storing outLinks. For each identifier (the key), 
     * another array is stored, containing for each inlink an associated weight
     */
    private $outLinks;

    /**
     * The number of nodes in the graph 
     */
    private $nodeCount;

    /**
     * Constructor for WebGraph
     *
     */
    public function __construct() {
        $this->identifierToString = array();
        $this->stringToIdentifier = array();
        $this->inLinks = array();
        $this->utLinks = array();
        $this->nodeCount = 0;
    }

    /**
     * Returns the identifier associated with a given string
     * 
     * @param string The string
     * @return The identifier associated with the given string
     */
    public function stringToIdentifier($string) {
        if (!is_string($string))
            return null;
        
        if (!isset($this->stringToIdentifier[$string]))
            return null;
        
        return $this->stringToIdentifier[$string];
    }

    /**
     * Returns the string associated with a given identifier
     * 
     * @param id The identifier
     * @return The string associated with the given identifier
     */
    public function identifierToString($id) {
        if (!is_numeric($id))
            return null;
        
        if (!isset($this->identifierToString[$id]))
            return null;

        return $this->identifierToString[$id];
    }

    /**
     *  Adds a node to the graph
     * 
     * @param String associated with the added node
     */
    public function addNode($string) {
        $id = $this->stringToIdentifier($string);

        if ($id == null) {
            $id = ++$this->nodeCount;

            $this->stringToIdentifier[$string] = $id;
            $this->identifierToString[$id] = $string;
            $this->inLinks[$id] = array();
            $this->outLinks[$id] = array();
        }
    }

    /**
     * Adds an association between two given nodes in the graph. If the 
     * corresponding nodes do not exists, this method creates them. If the
     * connection already exists, the strength value is updated.
     * 
     * @param fromLink The string for the source node in the graph 
     * @param toLink The string for the target node in the graph
     * @param weight The strength to associate with the connection.
     */
    public function addLink($from, $to, $weight = 1) {
        $this->addNode($from);
        $this->addNode($to);

        $id1 = $this->stringToIdentifier($from);
        $id2 = $this->stringToIdentifier($to);

        $this->addEdge($id1, $id2, $weight);
    }

    /**
     * Adds an association between two given nodes in the graph. If the 
     * corresponding nodes do not exists, this method creates them. If the
     * connection already exists, the strength value is updated.
     * 
     * @param fromLink The identifier for the source node in the graph 
     * @param fromLink The identifier for the target node in the graph
     * @param fromLink The strength to associate with the connection
     */
    private function addEdge($fromId, $toId, $weight) {
        $aux = 0;
        
        if ($weight < 1) {
            $weight = 1;
            //To-do: Exception
        }
        
        $this->inLinks[$toId][$fromId] = $weight;
        $this->outLinks[$fromId][$toId] = $weight;
    }

    /**
     * Returns an array of the nodes that connect to a given
     * node in the graph. Each mapping contains the identifier for a node
     * and the associated connection strength.  
     * 
     * @param string The stringfor the node in the graph 
     * @return An array of the nodes that connect to the given node in the graph.
     */
    public function inLinksByString($string) {
        $id = $this->stringToIdentifier($string);
        return $this->inLinksById($id);
    }

    /**
     * Returns an array of the nodes that connect to a given
     * node in the graph. Each mapping contains the identifier for a node
     * and the associated connection strength.  
     * 
     * @param id The identifier for the node in the graph 
     * @return An array of the nodes that connect to the given node in the graph.
     */
    public function inLinksById($id) {
        if ($id == null)
            return array();

        $aux = $this->inLinks[$id];
        return ($aux == null) ? array() : $aux;
    }

    /**
     * Returns an array of the nodes that connect to a given
     * node in the graph. Each mapping contains the identifier for a node
     * and the associated connection strength.   
     * 
     * @param string The string for the node in the graph 
     * @return An array of the nodes that connect to the given node in the graph.
     */
    public function outLinksByString($string) {
        $id = $this->stringToIdentifier($string);
        return $this->outLinksById($id);
    }

    /**
     * Returns an array of the nodes that connect to a given
     * node in the graph. Each mapping contains the identifier for a node
     * and the associated connection strength.  
     * 
     * @param id The identifier for the node in the graph 
     * @return An array of the nodes that connect to the given node in the graph.
     */
    public function outLinksById($id) {
        if ($id == null)
            return array();

        $aux = $this->outLinks[$id];
        return ($aux == null) ? array() : $aux;
    }

    /**
     * Returns the connection strength between two nodes, assuming there is a
     * connection from the first to the second. If no connection exists, a link
     * strength of zero is returned.
     * 
     * @param from The source 
     * @param to  The target 
     * @return The strenght for the connection between from and to ( from -> to )
     * @see inLink
     */
    public function getInLinkWeight($from, $to) {
        $id1 = $this->stringToIdentifier($from);
        $id2 = $this->stringToIdentifier($to);
        return $this->getInLinkWeightById($id1, $id2);
    }

    /**
     * Returns the connection strength between two nodes, assuming there is a
     * connection from the first to the second. If no connection exists, a link
     * strength of zero is returned.
     * 
     * @param from The source 
     * @param to  The target 
     * @return The strenght for the connection between fromLink and toLink ( from -> to )
     * @see outLink
     */
    public function getOutLinkWeight($from, $to) {
        $id1 = $this->stringToIdentifier($from);
        $id2 = $this->stringToIdentifier($to);
        return $this->getOutLinkWeightById($id1, $id2);
    }

    /**
     * Returns the connection strength between two nodes, assuming there is a
     * connection from the first to the second. If no connection exists, a link
     * strength of zero is returned.
     * 
     * @param fromId An identifier for the source id
     * @param toId  An identifier for the target id
     * @return The strenght for the connection between fromId and toId ( fromId -> toId )
     * @see outLink
     */
    public function getInLinkWeightById($fromId, $toId) {
        if (!isset($this->inLinks[$toId])) {
            return 0;
        }

        $weight = $this->inLinks[$toId][$fromId];
        return ($weight == null) ? 0 : $weight;
    }

    /**
     * Returns the connection strength between two nodes, assuming there is a
     * connection from the first to the second. If no connection exists, a link
     * strength of zero is returned.
     * 
     * @param fromId An identifier for the source
     * @param toId  An identifier for the target
     * @return The strenght for the connection between fromId and toId ( fromId -> toId )
     * @see outLink
     */
    public function getOutLinkWeightById($fromId, $toId) {
        if (!isset($this->outLinks[$fromId])) {
            return 0;
        }

        $weight = $this->outLinks[$fromId][$toId];
        return ($weight == null) ? 0 : $weight;
    }

    /**
     * Returns the number of nodes in the graph
     * 
     * @return The number of nodes in the graph
     */
    public function numNodes() {
        return $this->nodeCount;
    }
    
    
    public function getNodes() {
        return $this->identifierToString;
    }
    
    public function __toString() {
        $string = "";
        foreach ($this->inLinks as $id=>$inlink) {
            $string .= $this->identifierToString($id) . " <-- ";
            
            foreach ($inlink as $fromId=>$weight) {
                $string .= $this->identifierToString($fromId)."(".$weight."), ";
            }
            $string .= "<br/>";
        }
        
        return $string;
    }
}

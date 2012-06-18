<?php

require_once dirname(__FILE__) . '/../../Graph.php';

class Pagerank {

    /** The value for the PageRank dampening factor */
    private $dampening = 0.85;

    /** The data structure containing the linkage graph */
    private $graph;

    /** A <code>Map</code> containing the PageRank values for each page */
    private $scores;

    /**
     * Constructor for PageRank
     * 
     * @param graph The data structure containing the Web linkage graph
     */
    public function __construct($graph) {
        if (!$graph instanceof Graph) {
            //Exception
        }
        $this->graph = $graph;
        $this->scores = array();

        $numNodes = $graph->numNodes();
        $faux = 1 / $numNodes;
        
        $nodes = $this->graph->getNodes();
        foreach($nodes as $id=>$string) {
            $this->scores[$id] = $faux;
        }
    }

    /**
     * Sets the value for the PageRank dampening factor. The amount of PageRank that
     * is transferred depends on a dampening factor which stands for “the probability 
     * that a random surfer will get bored”. The dampening factor generally is set to 0.85.
     * 
     * @param damp The dampening factor
     */
    public function setDampening($damp) {
        $this->dampening = $damp;
    }

    /**
     * Returns the dampening factor used for the PageRank Algorithm. The amount of PageRank that
     * is transferred depends on a dampening factor which stands for “the probability 
     * that a random surfer will get bored”. The dampening factor generally is set to 0.85.
     * 
     * @return The dampening factor
     */
    public function getDampening() {
        return $this->dampening;
    }

    /**
     * Returns the PageRank value associated with a given link
     * 
     * @param link The url for the link
     * @return The PageRank value associated with the given link
     */
    public function pageRankByString($string) {
        return $this->pageRankById($this->graph->stringToIdentifier($string));
    }

    /**
     * Returns the PageRank value associated with a given link identifyer.
     * Identifyers are Integer numberes, used in <code>WebGraph</code> to
     * represent the Web graph for efficiency reasons.
     * 
     * @param link An identifyer for the link
     * @return The PageRank value associated with the given link
     * @see WebGraph.IdentifyerToURL()
     */
    private function pageRankById($id) {
        return $this->scores[$id];
    }

    /**
     * Computes the PageRank value for all the nodes in the Web Graph.
     * In this method, the number of iterations of the algorithm is set accordingly to
     * the number of nodes in the Web graph.
     *
     */
    public function compute() {
        $n = $this->graph->numNodes();

        //           | Log(n) |     
        //Iter = ABS | ------ | + 1 
        //           | Log(10)|    
        $iter = ((int) abs(log($n) / log(10))) + 1;
        $iter = ($iter < 10)? 10 : $iter;
        $this->computePagerank($iter);
    }

    /**
     * Computes the PageRank value for all the nodes in the Web Graph.
     * The formula of Sergey Brin and Lawrence Page (founders of Google) can be
     * found in their <a href="http://www-db.stanford.edu/~backrub/google.html">original document</a>.
     * Essentially:
     * 
     * The amount of PageRank that is transferred depends on a dampening factor
     *  which stands for “the probability that a random surfer will get bored”. The dampening
     *  factor generally is set to 0.85. 
     *
     * The more outgoing links a web page has, the less PageRank of that page will
     *  be transferred to each of the pages it links to. Very simple: devide the real PageRank
     *  by the number of outgoing links and multiply it with the dampening factor to calculate
     *  the amount of PageRank that is transferred.
     * 
     * Do this for all pages that link to your page and you know your own PageRank.
     *
     * @param iter The number of iterations for the algorithm
     * 
     */
    public function computePagerank($iter) {
        $n = $this->graph->numNodes();
        $aux = (1 - $this->dampening);
        $ids = array_keys($this->scores);
        
        //TO-DO: Hacer converger con un epsilon
        while (($iter--) > 0) {
            $newScore = array();

            foreach ($ids as $i) {
                $inlinks = $this->graph->inLinksById($i);
                $weight2 = 0;
                
                foreach ($inlinks as $fromId => $weight) {
                    $outlinks = $this->graph->outLinksById($fromId);

                    if ($weight != null && $weight > 0 &&
                            $outlinks != null && sizeof($outlinks) > 0) {
                        $weight2 += ($weight * $this->scores[$fromId]) / sizeof($outlinks);
                    }
                }
                
                $weight2 = ($aux / $n) + ($this->dampening * $weight2);
                $newScore[$i] = $weight2;
            }

            foreach ($ids as $i) {
                $this->scores[$i] = $newScore[$i];
            }
        }
    }

}
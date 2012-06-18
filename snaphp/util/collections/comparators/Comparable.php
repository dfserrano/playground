<?php
interface Comparable {
    public function getId();
    public function setId($value);
    public function getValue();
    public function setValue($value);
    public function compareTo($other);
}
?>

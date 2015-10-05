<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 29.09.2015
 * Time: 16:48
 */

abstract class DBConnection {

    protected $connection;

    protected function __construct($DBName) {
        $this->connection = pg_connect("host=".DBConfig::HOST." port=".DBConfig::PORT." dbname=".$DBName." user=".DBConfig::USERNAME." password=".DBConfig::PASSWORD)
        or die("Unable to connect to the Database");
    }

    public function closeConnection() {
        pg_close($this->connection);
    }
}
?>
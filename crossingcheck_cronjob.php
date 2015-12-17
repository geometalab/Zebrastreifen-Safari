<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 27.11.2015
 * Time: 14:51
 */

require_once('connection/DBConfig.php');
require_once('connection/DBCrossing.php');

$crossingConnection = new DBCrossing();
$crossingConnection->updateCrossingStatus();
$crossingConnection->closeConnection();
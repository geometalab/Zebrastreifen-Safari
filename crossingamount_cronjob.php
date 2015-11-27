<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 13.11.2015
 * Time: 16:09
 */

require_once('connection/DBConfig.php');
require_once('connection/DBCrossing.php');

$crossingConnection = new DBCrossing();
$query = $crossingConnection->getCrossingAmount();
$crossingAmount = pg_fetch_all($query)[0]['amount'];
$crossingConnection->setCrossingAmount($crossingAmount);
$crossingConnection->closeConnection();
<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 13.11.2015
 * Time: 16:09
 */

require_once('connection/DBConfig.php');
require_once('connection/DBConnection.php');
require_once('connection/DBZebracrossing.php');
require_once('connection/DBGis.php');

$zebracrossingConnection = new DBZebracrossing();
$gisConnection = new DBGis();

$query = $gisConnection->getCrossingAmount();
$crossingAmount = pg_fetch_all($query)[0]['amount'];
echo $crossingAmount;
$zebracrossingConnection->setCrossingAmount($crossingAmount);

$gisConnection->closeConnection();
$zebracrossingConnection->closeConnection();
<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 17.12.2015
 * Time: 11:46
 */

require_once('connection/DBConfig.php');
require_once('connection/DBCrossing.php');

$ratingConnection = new DBCrossing();
$query = $ratingConnection->getRatingAmount();
$ratingAmount = pg_fetch_all($query)[0]['amount'];
$ratingConnection->setRatingAmount($ratingAmount);
$ratingConnection->closeConnection();
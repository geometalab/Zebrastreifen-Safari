<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 13.11.2015
 * Time: 16:09
 */

require_once('connection/DBCrossing.php');

$dbConnection = new DBCrossing();
$crossingQuery = $dbConnection->getCrossingAmount();
$crossingAmount = pg_fetch_all($crossingQuery)[0]['amount'];
$dbConnection->setCrossingAmount($crossingAmount);

$ratingQuery = $dbConnection->getRatingAmount();
$ratingAmount = pg_fetch_all($ratingQuery)[0]['amount'];
$dbConnection->setRatingAmount($ratingAmount);
$dbConnection->closeConnection();

?>

<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 05.10.2015
 * Time: 08:19
 */

require_once('connection/DBConfig.php');
require_once('connection/DBCrossing.php');

function crossingBarchartStatistic() {
    $statisticConnection = new DBCrossing();
    $resultset = getFormattedBarchartStatisticData($statisticConnection->getCrossingBarChartStatistic(getOffset($statisticConnection->getCrossingStatisticWeekAmount())));
    $statisticConnection->closeConnection();
    return $resultset;
}

function crossingLinechartStatistic() {
    $statisticConnection = new DBCrossing();
    $resultset = getFormattedLinechartStatisticData($statisticConnection->getCrossingLineChartStatistic());
    $statisticConnection->closeConnection();
    return $resultset;
}

function ratingBarchartStatistic() {
    $statisticConnection = new DBCrossing();
    $resultset = getFormattedBarchartStatisticData($statisticConnection->getRatingBarChartStatistic(getOffset($statisticConnection->getRatingStatisticWeekAmount())));
    $statisticConnection->closeConnection();
    return $resultset;
}

function getFormattedBarchartStatisticData($barchartQuery) {
    $barchartResultset = pg_fetch_all($barchartQuery);

    for ($i = 0; $i < count($barchartResultset) - 1; $i++) {
        $barchartResultset[$i]['week'] = date('d', strtotime($barchartResultset[$i + 1]['week']))."-".date('d.m.Y', strtotime("+6 day", strtotime($barchartResultset[$i + 1]['week'])));
        $barchartResultset[$i]['amount'] = $barchartResultset[$i + 1]['amount'] - $barchartResultset[$i]['amount'];
    }

    unset($barchartResultset[count($barchartResultset) - 1]);
    return $barchartResultset;
}

function getFormattedLinechartStatisticData($linechartQuery) {
    $linechartResultset = pg_fetch_all($linechartQuery);

    for ($i = 0; $i < count($linechartResultset); $i++) {
        $linechartResultset[$i]['week'] = date('d.m.Y', strtotime("+6 day", strtotime($linechartResultset[$i]['week'])));
        $linechartResultset[$i]['amount'] = intval($linechartResultset[$i]['amount']);
    }

    return $linechartResultset;
}

function getOffset($query) {
    $resultset = pg_fetch_all($query)[0]['week_amount'];

    if ($resultset < 11) {
        $offset = 0;
    } else if ($resultset < 21) {
        $offset = $resultset - ($resultset - 10);
    } else {
        $offset = $resultset - 11;
    }

    return $offset;
}
?>
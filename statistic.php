<?php
/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 05.10.2015
 * Time: 08:19
 */

//Get the data for the barchart
function barchartStatistic() {
    $statisticConnection = new DBCrossing();
    $query = $statisticConnection->getBarChartStatistic(getOffset($statisticConnection));
    $resultset = pg_fetch_all($query);

    for ($i = 0; $i < count($resultset) - 1; $i++) {
        $resultset[$i]['week'] = date('Y-m-d', strtotime("+7 day", strtotime($resultset[$i]['week'])));
        $resultset[$i]['amount'] = $resultset[$i + 1]['amount'] - $resultset[$i]['amount'];
    }

    unset($resultset[count($resultset) - 1]);
    $statisticConnection->closeConnection();
    return $resultset;
}

//Get the data for the linechart
function linechartStatistic() {
    $statisticConnection = new DBCrossing();
    $query = $statisticConnection->getLineChartStatistic();
    $resultset = pg_fetch_all($query);

    for ($i = 0; $i < count($resultset); $i++) {
        $resultset[$i]['week'] = date('Y-m-d', strtotime("+7 day", strtotime($resultset[$i]['week'])));
        $resultset[$i]['amount'] = intval($resultset[$i]['amount']);
    }

    $statisticConnection->closeConnection();
    return $resultset;
}

function getOffset($statisticConnection) {
    $query = $statisticConnection->getWeekAmount();
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
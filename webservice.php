<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 28.09.2015
 * Time: 15:23
 */

header('Content-type: application/json');
header('Access-Control-Allow-Origin: *');

require_once('statistic.php');
require_once('crossing.php');

if (!empty($_GET['chart'])) {
    $type = pg_escape_string($_GET['chart']);

    if ($type == 'crossinglinechart') {
        echo json_encode(crossingLinechartStatistic());
    } else if ($type == 'crossingbarchart') {
        echo json_encode(crossingBarchartStatistic());
    } else if ($type == 'ratingbarchart') {
        echo json_encode(ratingBarchartStatistic());
    } else {
        echo json_encode(array("error" => 404, "reason" => 'Parameter "chart" has an invalid value.'));
    }
} else if (!empty($_GET['crosswalk'])) {
    echo json_encode(crossingDetail(pg_escape_string($_GET['crosswalk'])));
} else if (!empty($_GET['crosswalks'])) {
    echo json_encode(crossingPoints(pg_escape_string($_GET['bounds']), pg_escape_string($_GET['maxamount'])));
} else {
    echo json_encode(array("error" => 404, "reason" => 'Missing required parameter.'));
}
?>
<?php
header('Content-type: application/json');
header('Access-Control-Allow-Origin: *');
/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 28.09.2015
 * Time: 15:23
 */

require_once('data.php');

if (!empty($_GET['type'])) {
    $type = pg_escape_string($_GET['type']);

    if ($type == 'linechart') {
        echo json_encode(linechartStatistic());
    } else if ($type == 'barchart') {
        echo json_encode(barchartStatistic());
    } else if ($type == 'crosswalks') {
        echo json_encode(zebracrossingPoints());
    } else {
        echo json_encode(array("error" => 404, "reason" => 'Parameter "type" has an invalid value.'));
    }
} else if (!empty($_GET['crosswalk'])) {
    echo json_encode(zebracrossingDetail(pg_escape_string($_GET['crosswalk'])));
} else {
    echo json_encode(array("error" => 404, "reason" => 'Missing required parameter.'));
}
?>
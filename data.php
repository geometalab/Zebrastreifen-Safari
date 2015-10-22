<?php
/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 05.10.2015
 * Time: 08:19
 */

require_once('connection/DBConfig.php');
require_once('connection/DBConnection.php');
require_once('connection/DBZebracrossing.php');
require_once('connection/DBGis.php');

//Get the data for the barchart
function barchartStatistic() {
    $statisticConnection = new DBZebracrossing();
    $query = $statisticConnection->getBarChartStatistic();
    $resultset = pg_fetch_all($query);

    for ($i = 0; $i < count($resultset); $i++) {
        //Formate the date
        $resultset[$i]['week'] = /*date('Y-m-d', strtotime($resultset[$i]['week'])).' - '.*/date('Y-m-d', strtotime("+7 day", strtotime($resultset[$i]['week'])));

        if ($i != 0) {
            //Makes automatically an integer
            //Adds this week to all the previous ones
            $resultset[$i]['amount'] += $resultset[$i - 1]['amount'];
        } else {
            //First one needs to be casted to an integer
            $resultset[$i]['amount'] = intval($resultset[$i]['amount']);
        }
    }

    $statisticConnection->closeConnection();
    /* testoutput
    echo "<table border='1'><th>Week</th><th>Amount</th>";
    foreach ($resultset as $result) {
        echo "<tr>";
        echo "<td>";
        echo $result['week'];
        echo "</td>";
        echo "<td>";
        echo $result['amount'];
        echo "</td>";
        echo "</tr>";
    }
    echo "</table>";
    /**/
    return $resultset;
}

//Get the data for the linechart
function linechartStatistic() {
    $statisticConnection = new DBZebracrossing();
    $query = $statisticConnection->getLineChartStatistic();
    $resultset = pg_fetch_all($query);

    for ($i = 0; $i < count($resultset); $i++) {
        //Formate the date
        $resultset[$i]['week'] = /*date('Y-m-d', strtotime($resultset[$i]['week'])).' - '.*/date('Y-m-d', strtotime("+7 day", strtotime($resultset[$i]['week'])));

        //Needs to be casted to an integer
        $resultset[$i]['total'] = intval($resultset[$i]['total']);
    }

    $statisticConnection->closeConnection();
    /* testoutput
    echo "<table border='1'><th>Week</th><th>Amount</th>";
    foreach ($resultset as $result) {
        echo "<tr>";
        echo "<td>";
        echo $result['week'];
        echo "</td>";
        echo "<td>";
        echo $result['total'];
        echo "</td>";
        echo "</tr>";
    }
    echo "</table>";
    /**/
    return $resultset;
}

//Get all zebracrossings and their coordinates in a geojson file
function zebracrossingPoints() {
    $zebracrossings = array(
        "type" => "FeatureCollection"
    );

    $zebracrossingConnection = new DBZebracrossing();
    $gisConnection = new DBGis();
    $query = $zebracrossingConnection->getAllZebracrossings();

    while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
        $zebracrossings['features'][] = array(
            "type" => "Feature",
            "geometry" => array(
                "type" => "Point"
            ),
            "properties" => array(
                "node" => doubleval($row['node'])
            )
        );
    }

    for ($i = 0; $i < count($zebracrossings['features']); $i++) {
        //Get the x and y coordinates of the zebracrossing
        $query = $gisConnection->getCoordinates($zebracrossings['features'][$i]['properties']['node']);

        while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
            $zebracrossings['features'][$i]['geometry']['coordinates'] = array(
                doubleval($row['x']),
                doubleval($row['y'])
            );
        }

        //Get all the osm data and write it into the array
        $zebracrossings['features'][$i]['properties'] += getOsmData($zebracrossings['features'][$i]['properties']['node'], $gisConnection);
    }

    $zebracrossingConnection->closeConnection();
    $gisConnection->closeConnection();

    /* testoutput
    echo "<table border='1'><th>Node</th><th>X</th><th>Y</th><th>image</th><th>traffic_signals</th><th>island</th><th>unmarked</th><th>button_operated</th><th>slooped_curb</th><th>tactile_paving</th><th>traffic_signals_vibration</th><th>traffic_signals_sound</th><th>rating</th>";
    foreach ($zebracrossings['features'] as $zebracrossing) {
        echo "<tr>";
        echo "<td>";
        echo $zebracrossing['properties']['node'];
        echo "</td>";
        echo "<td>";
        echo $zebracrossing['geometry']['coordinates'][0];
        echo "</td>";
        echo "<td>";
        echo $zebracrossing['geometry']['coordinates'][1];
        echo "</tr>";
    }
    echo "</table>";
    /**/
    return $zebracrossings;
}

//Get the details about a specific zebracrossing
function zebracrossingDetail($node) {
    if (!is_numeric($node)) {
        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $zebracrossingConnection = new DBZebracrossing();
    $gisConnection = new DBGis();
    $query = $zebracrossingConnection->getZebracrossing($node);
    $resultset = pg_fetch_all($query);

    if (!$resultset) {
        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $zebracrossing = array(
        "id" => $resultset[0]['zebracrossing_id'],
        "node" => doubleval($resultset[0]['node']),
        "image" => $resultset[0]['image']
    );

    //Get all the osm data and write it into the array
    $zebracrossing['osm'] = getOsmData($zebracrossing['node'], $gisConnection);

    //Get the ratings of the zebracrossing
    $query = $zebracrossingConnection->getRating($zebracrossing['id']);
    unset($zebracrossing['id']);

    while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
        $zebracrossing['ratings'][] = array(
            "comment" => $row['comment'],
            "username" => $row['name'],
            "overview" => $row['overview_value'],
            "illumination" => $row['illumination_value'],
            "traffic" => $row['traffic_value']
        );
    }

    $zebracrossingConnection->closeConnection();
    $gisConnection->closeConnection();

    /* testoutput
    echo "<table border='1'><th>Node</th><th>image</th><th>traffic_signals</th><th>island</th><th>unmarked</th><th>button_operated</th><th>slooped_curb</th><th>tactile_paving</th><th>traffic_signals_vibration</th><th>traffic_signals_sound</th><th>rating</th>";
    echo "<tr>";
    echo "<td>";
    echo $zebracrossing['node'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['image'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['osm']['traffic_signals'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['osm']['island'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['osm']['unmarked'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['osm']['button_operated'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['osm']['slooped_curb'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['osm']['tactile_paving'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['osm']['traffic_signals_vibration'];
    echo "</td>";
    echo "<td>";
    echo $zebracrossing['osm']['traffic_signals_sound'];
    echo "</td>";
    echo "<td>";

    echo "<table border='1'><th>comment</th><th>username</th><th>overview</th><th>illumination</th><th>traffic</th>";
    foreach ($zebracrossing['ratings'] as $rating) {
        echo "<tr>";
        echo "<td>";
        echo $rating['comment'];
        echo "</td>";
        echo "<td>";
        echo $rating['username'];
        echo "</td>";
        echo "<td>";
        echo $rating['overview'];
        echo "</td>";
        echo "<td>";
        echo $rating['illumination'];
        echo "</td>";
        echo "<td>";
        echo $rating['traffic'];
        echo "</td>";
        echo "</tr>";
    }
    echo "</table>";

    echo "</td>";
    echo "</tr>";
    echo "</table>";
    /**/
    return $zebracrossing;
}

function getOsmData($node, $gisConnection) {
    //Get the sloped_curb state
    //Default value
    $sloped_curb = "no";

    //If there's a sloped_curb on one side
    $sloped_curb_query = $gisConnection->getState("sloped_curb", "one", $node);
    $resultset = pg_fetch_all($sloped_curb_query);

    if ($resultset) {
        $sloped_curb = "one";
    } else {
        //If there's a sloped_curb on both sides
        $sloped_curb_query = $gisConnection->getState("sloped_curb", "both", $node);
        $resultset = pg_fetch_all($sloped_curb_query);

        if ($resultset) {
            $sloped_curb = "both";
        }
    }

    $query = $gisConnection->getGisData($node);
    $resultset = pg_fetch_all($query);
    return array(
        "traffic_signals" => $resultset[0]['traffic_signals'] == "f" ? false : true,
        "island" => $resultset[0]['island'] == "f" ? false : true,
        "unmarked" => $resultset[0]['unmarked'] == "f" ? false : true,
        "button_operated" => $resultset[0]['button_operated'] == "f" ? false : true,
        "slooped_curb" => $sloped_curb,
        "tactile_paving" => $resultset[0]['tactile_paving'] == "f" ? false : true,
        "traffic_signals_vibration" => $resultset[0]['traffic_signals_vibration'] == "f" ? false : true,
        "traffic_signals_sound" => $resultset[0]['traffic_signals_sound'] == "f" ? false : true
    );
}
?>
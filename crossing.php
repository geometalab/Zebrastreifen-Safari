<?php
/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 20.11.2015
 * Time: 11:54
 */

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
                "osm_node_id" => doubleval($row['osm_node_id']),
                "status" => intval($row['status'])
            )
        );
    }

    for ($i = 0; $i < count($zebracrossings['features']); $i++) {
        //Get the x and y coordinates of the zebracrossing
        $query = $gisConnection->getCoordinates($zebracrossings['features'][$i]['properties']['osm_node_id']);

        while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
            $zebracrossings['features'][$i]['geometry']['coordinates'] = array(
                doubleval($row['x']),
                doubleval($row['y'])
            );
        }

        //Get all the osm data and write it into the array
        $zebracrossings['features'][$i]['properties'] += getOsmData($zebracrossings['features'][$i]['properties']['osm_node_id'], $gisConnection);
    }

    $zebracrossingConnection->closeConnection();
    $gisConnection->closeConnection();
    return $zebracrossings;
}

//Get the details about a specific zebracrossing
function zebracrossingDetail($osm_node_id) {
    if (!is_numeric($osm_node_id)) {
        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $zebracrossingConnection = new DBZebracrossing();
    $gisConnection = new DBGis();
    $query = $zebracrossingConnection->getZebracrossing($osm_node_id);
    $resultset = pg_fetch_all($query);

    if (!$resultset) {
        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $zebracrossing = array(
        "id" => $resultset[0]['id'],
        "osm_node_id" => doubleval($resultset[0]['osm_node_id']),
        "status" => intval($resultset[0]['status'])
    );

    //Get all the osm data and write it into the array
    $zebracrossing['osm'] = getOsmData($zebracrossing['osm_node_id'], $gisConnection);

    //Get the ratings of the zebracrossing
    $query = $zebracrossingConnection->getRating($zebracrossing['id']);
    unset($zebracrossing['id']);

    while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
        $zebracrossing['ratings'][] = array(
            "spatial_clarity" => $row['sc_value'],
            "illumination" => $row['i_value'],
            "traffic" => $row['t_value'],
            "image_weblink" => $row['image_weblink'],
            "comment" => $row['comment'],
            "username" => $row['name']
        );
    }

    $zebracrossingConnection->closeConnection();
    $gisConnection->closeConnection();
    return $zebracrossing;
}

function getOsmData($osm_node_id, $gisConnection) {
    //Get the sloped_curb state
    //Default value
    $sloped_curb = "no";

    //If there's a sloped_curb on one side
    $sloped_curb_query = $gisConnection->getState("sloped_curb", "one", $osm_node_id);
    $resultset = pg_fetch_all($sloped_curb_query);

    if ($resultset) {
        $sloped_curb = "one";
    } else {
        //If there's a sloped_curb on both sides
        $sloped_curb_query = $gisConnection->getState("sloped_curb", "both", $osm_node_id);
        $resultset = pg_fetch_all($sloped_curb_query);

        if ($resultset) {
            $sloped_curb = "both";
        }
    }

    $query = $gisConnection->getGisData($osm_node_id);
    $resultset = pg_fetch_all($query);
    return array(
        "traffic_signals" => $resultset[0]['traffic_signals'] == "f" ? false : true,
        "island" => $resultset[0]['island'] == "f" ? false : true,
        "unmarked" => $resultset[0]['unmarked'] == "f" ? false : true,
        "button_operated" => $resultset[0]['button_operated'] == "f" ? false : true,
        "sloped_curb" => $sloped_curb,
        "tactile_paving" => $resultset[0]['tactile_paving'] == "f" ? false : true,
        "traffic_signals_vibration" => $resultset[0]['traffic_signals_vibration'] == "f" ? false : true,
        "traffic_signals_sound" => $resultset[0]['traffic_signals_sound'] == "f" ? false : true
    );
}
?>
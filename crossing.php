<?php
/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 20.11.2015
 * Time: 11:54
 */

function crossingPoints($zoom, $bounds) {
    $crossingConnection = new DBCrossing();
    $query = $crossingConnection->getAllCrossings();
    $bounds = split(",", $bounds);

    $crossings = array(
        "type" => "FeatureCollection"
    );

    while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
        $crossings['features'][] = array(
            "type" => "Feature",
            "geometry" => array(
                "type" => "Point",
                "coordinates" => [doubleval($row['x']), doubleval($row['y'])]
            ),
            "properties" => getOsmDetails($row)
        );
    }

    $crossingConnection->closeConnection();
    return $crossings;
}

function crossingDetail($osmNodeId) {
    if (!is_numeric($osmNodeId)) {
        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $crossingConnection = new DBCrossing();
    $query = $crossingConnection->getCrossing($osmNodeId);
    $resultset = pg_fetch_all($query)[0];

    if (!$resultset) {
        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $crossing = getOsmDetails($resultset);
    $crossing['ratings'] = getRatings($resultset['id'], $crossingConnection);

    $crossingConnection->closeConnection();
    return $crossing;
}

function getOsmDetails($row) {
    return array(
        "osm_node_id" => doubleval($row['osm_node_id']),
        "status" => intval($row['status']),
        "traffic_signals" => boolval($row['traffic_signals']),
        "island" => boolval($row['island']),
        "unmarked" => boolval($row['unmarked']),
        "button_operated" => boolval($row['button_operated']),
        "sloped_curb" => $row['sloped_curb'],
        "tactile_paving" => boolval($row['tactile_paving']),
        "traffic_signals_vibration" => boolval($row['traffic_signals_vibration']),
        "traffic_signals_sound" => boolval($row['traffic_signals_sound'])
    );
}

function getRatings($crossingId, $crossingConnection) {
    $query = $crossingConnection->getRating($crossingId);

    while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
        $ratings[] = array(
            "spatial_clarity" => $row['sc_value'],
            "illumination" => $row['i_value'],
            "traffic" => $row['t_value'],
            "image_weblink" => $row['image_weblink'],
            "comment" => $row['comment'],
            "username" => $row['name']
        );
    }

    return $ratings;
}
?>
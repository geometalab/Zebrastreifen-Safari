<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 20.11.2015
 * Time: 11:54
 */

require_once('connection/DBConfig.php');
require_once('connection/DBCrossing.php');

function crossingPoints($bounds, $maxCrossingAmount) {
    $bounds = explode(",", $bounds);

    for ($i = 0; $i < 4; $i++) {
        if (!is_numeric($bounds[$i])) {
            return http_response_code(404);
//            return array("error" => 404, "reason" => 'Parameter "bounds" has an invalid value');
        }
    }

    if (count($bounds) != 4) {
        return http_response_code(404);
//        return array("error" => 404, "reason" => 'Parameter "bounds" has an invalid amount of parameters');
    }

    if (!is_numeric($maxCrossingAmount)) {
        return http_response_code(404);
//        return array("error" => 404, "reason" => 'Parameter "maxamount" has an invalid value');
    }

    $crossingConnection = new DBCrossing();
    $query = $crossingConnection->getAllCrossings(getSnap($maxCrossingAmount, $bounds, $crossingConnection), $bounds);

    $crossings = array(
        "type" => "FeatureCollection"
    );

    while ($row = pg_fetch_array($query)) {
        $crossings['features'][] = array(
            "type" => "Feature",
            "geometry" => array(
                "type" => "Point",
                "coordinates" => [doubleval($row['x']), doubleval($row['y'])]
            ),
            "properties" => getOsmDetailsClustered($row)
        );
    }

    $crossingConnection->closeConnection();
    return $crossings;
}

function crossingDetail($osmNodeId) {
    if (!is_numeric($osmNodeId)) {
        return http_response_code(404);
//        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $crossingConnection = new DBCrossing();
    $query = $crossingConnection->getCrossing($osmNodeId);
    $resultset = pg_fetch_all($query)[0];

    if (!$resultset) {
        return http_response_code(404);
//        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $crossing = getOsmDetails($resultset);
    $crossing['ratings'] = getRatings($crossingConnection->getRating($resultset['id']));

    $crossingConnection->closeConnection();
    return $crossing;
}

function getOsmDetails($row) {
    return array(
        "osm_node_id" => doubleval($row['osm_node_id']),
        "status" => intval($row['status']),
        "traffic_signals" => myBoolval($row['traffic_signals']),
        "island" => myBoolval($row['island']),
        "unmarked" => myBoolval($row['unmarked']),
        "button_operated" => myBoolval($row['button_operated']),
        "sloped_curb" => $row['sloped_curb'],
        "tactile_paving" => myBoolval($row['tactile_paving']),
        "traffic_signals_vibration" => myBoolval($row['traffic_signals_vibration']),
        "traffic_signals_sound" => myBoolval($row['traffic_signals_sound']),
        "rated" => intval($row['status']) == 0 ? false : true
    );
}

function getOsmDetailsClustered($row) {
    if ($row['amount'] != "1") {
        return array("amount" => intval($row['amount']));
    }

    $row['osm_node_id'] = str_replace('{', '', str_replace('}', '', $row['osm_node_id']));
    $row['status'] = str_replace('{', '', str_replace('}', '', $row['status']));
    $row['traffic_signals'] = str_replace('{', '', str_replace('}', '', $row['traffic_signals']));
    $row['island'] = str_replace('{', '', str_replace('}', '', $row['island']));
    $row['unmarked'] = str_replace('{', '', str_replace('}', '', $row['unmarked']));
    $row['button_operated'] = str_replace('{', '', str_replace('}', '', $row['button_operated']));
    $row['sloped_curb'] = str_replace('{', '', str_replace('}', '', $row['sloped_curb']));
    $row['tactile_paving'] = str_replace('{', '', str_replace('}', '', $row['tactile_paving']));
    $row['traffic_signals_vibration'] = str_replace('{', '', str_replace('}', '', $row['traffic_signals_vibration']));
    $row['traffic_signals_sound'] = str_replace('{', '', str_replace('}', '', $row['traffic_signals_sound']));
    return getOsmDetails($row);
}

function getRatings($query) {
    while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
        $ratings[] = array(
            "spatial_clarity" => $row['sc_value'],
            "illumination" => $row['i_value'],
            "traffic" => $row['t_value'],
            "image_weblink" => $row['image_weblink'],
            "comment" => $row['comment'],
            "username" => $row['name'],
            "last_changed" => date('H:i d.m.Y', strtotime($row['last_changed'])),
            "creation_time" => $row['creation_time'] == null ? null : date('H:i d.m.Y', strtotime($row['creation_time']))
        );
    }

    return $ratings;
}

function myBoolval($var) {
    if ($var == "f") {
        return false;
    }

    return boolval($var);
}

//$crossingConnection = new DBCrossing();
//echo getSnap(11, array(978116.8778227222,5978804.028866864,984642.6891125691,5980027.021319423), $crossingConnection);
//$crossingConnection->closeConnection();

function getSnap($maxAmount, $bounds, $crossingConnection) {
    $numbers = range(-1000, 1000000, 1000);
    $position = halve(count($numbers));
    $maxHeight = count($numbers);
    $minHeight = 0;

    while (true) {
        $oldMinHeight = $minHeight;
        $oldMaxHeight = $maxHeight;
        $query = $crossingConnection->getClusteredAmount($numbers[$position], $bounds);
        $queryAmount = pg_fetch_all($query)[0]['amount'];

        if ($queryAmount > $maxAmount) {
            $minHeight = $position;

            if ($maxHeight == $oldMaxHeight && $minHeight == $oldMinHeight) {
                break;
            }

            $position += halve($maxHeight - $position);

        } else if ($queryAmount < $maxAmount) {
            $maxHeight = $position;

            if ($maxHeight == $oldMaxHeight && $minHeight == $oldMinHeight) {
                break;
            }

            $position = halve($position - $minHeight);
        } else {
            break;
        }
    }

    return $numbers[$position];
}

function halve($number) {
    if ($number % 2 == 0) {
        return intval($number / 2);
    } else {
        return intval($number / 2 + 0.5);
    }
}
?>
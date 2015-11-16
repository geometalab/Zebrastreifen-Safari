<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 01.10.2015
 * Time: 10:08
 */

class DBGis extends DBConnection {

    public function __construct() {
        parent::__construct(DBConfig::GIS);
    }

    public function getAllZebracrossings() {

    }

    public function getGisData($osm_node_id) {
        return pg_query($this->connection, "SELECT
                                            tags @> hstore('crossing', 'traffic_signals') AS traffic_signals,
                                            tags @> hstore('crossing', 'island') AS island,
                                            tags @> hstore('crossing', 'unmarked') AS unmarked,
                                            tags @> hstore('button_operated', 'yes') AS button_operated,
                                            tags @> hstore('tactile_paving', 'yes') AS tactile_paving,
                                            tags @> hstore('traffic_signals:vibration', 'yes') AS traffic_signals_vibration,
                                            tags @> hstore('traffic_signals:sound', 'yes') AS traffic_signals_sound
                                            FROM osm_point
                                            WHERE tags @> hstore('highway', 'crossing')
                                            AND osm_id = $osm_node_id;");
    }

    public function getCoordinates($osm_node_id) {
        return pg_query($this->connection, "SELECT ST_X(way) AS x, ST_Y(way) AS y
                                            FROM osm_point
                                            WHERE tags @> hstore('highway', 'crossing')
                                            AND osm_id = $osm_node_id;");
    }

    public function getState($tag, $value, $osm_node_id) {
        return pg_query($this->connection, "SELECT tags @> hstore('$tag', '$value')
                                            FROM osm_point
                                            WHERE tags @> hstore('highway', 'crossing')
                                            AND tags @> hstore('$tag', '$value')
                                            AND osm_id = $osm_node_id;");
    }

    public function getYesNoState($tag, $osm_node_id) {
        return pg_query($this->connection, "SELECT tags @> hstore('$tag', 'yes') AS state
                                            FROM osm_point
                                            WHERE tags @> hstore('highway', 'crossing')
                                            AND osm_id = $osm_node_id;");
    }

    public function getCrossingAmount() {
        return pg_query($this->connection, "SELECT COUNT(*) AS amount
                                            FROM osm_point
                                            WHERE tags @> hstore('highway', 'crossing')");
    }

    public function getClusteredAmount($number) {
        return pg_query($this->connection, "SELECT COUNT(*) FROM
                                            (SELECT COUNT(way) FROM osm_point
                                            WHERE tags @> hstore('highway','crossing')
                                            GROUP BY ST_SnapToGrid(way, $number))
                                            AS tmp;");
    }
}
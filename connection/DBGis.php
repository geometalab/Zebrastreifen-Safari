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

    public function getGisData($node) {
        return pg_query($this->connection, "select
                                            tags @> hstore('crossing', 'traffic_signals') as traffic_signals,
                                            tags @> hstore('crossing', 'island') as island,
                                            tags @> hstore('crossing', 'unmarked') as unmarked,
                                            tags @> hstore('button_operated', 'yes') as button_operated,
                                            tags @> hstore('tactile_paving', 'yes') as tactile_paving,
                                            tags @> hstore('traffic_signals:vibration', 'yes') as traffic_signals_vibration,
                                            tags @> hstore('traffic_signals:sound', 'yes') as traffic_signals_sound
                                            from osm_point
                                            where tags @> hstore('highway', 'crossing')
                                            and osm_id = $node;");
    }

    public function getCoordinates($node) {
        return pg_query($this->connection, "select ST_X(way) as x, ST_Y(way) as y
                                            from osm_point
                                            where tags @> hstore('highway', 'crossing')
                                            and osm_id = $node;");
    }

    public function getState($tag, $value, $node) {
        return pg_query($this->connection, "select tags @> hstore('$tag', '$value')
                                            from osm_point
                                            where tags @> hstore('highway', 'crossing')
                                            and tags @> hstore('$tag', '$value')
                                            and osm_id = $node;");
    }

    public function getYesNoState($tag, $node) {
        return pg_query($this->connection, "select tags @> hstore('$tag', 'yes') as state
                                            from osm_point
                                            where tags @> hstore('highway', 'crossing')
                                            and osm_id = $node;");
    }
}
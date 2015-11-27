<?php
/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 01.10.2015
 * Time: 10:07
 */

class DBCrossing {

    private $connection;

    public function __construct() {
        $this->connection = pg_connect("host=".DBConfig::HOST." port=".DBConfig::PORT." dbname=".DBConfig::ZEBRACROSSING." user=".DBConfig::USERNAME." password=".DBConfig::PASSWORD)
        or die("Unable to connect to the Database");
    }

    public function closeConnection() {
        pg_close($this->connection);
    }

    public function getCrossing($osm_node_id) {
        return pg_query($this->connection, "SELECT
                                            crossing.id,
                                            crossing.osm_node_id,
                                            crossing.status,
                                            osm.traffic_signals,
                                            osm.island,
                                            osm.unmarked,
                                            osm.button_operated,
                                            osm.sloped_curb,
                                            osm.tactile_paving,
                                            osm.traffic_signals_vibration,
                                            osm.traffic_signals_sound
                                            FROM crossing.crossing AS crossing
                                            INNER JOIN crossing.osm_crossings AS osm
                                            ON crossing.osm_node_id = osm.osm_id
                                            WHERE crossing.osm_node_id = $osm_node_id;");
    }

    public function getAllCrossings() {
        return pg_query($this->connection, "SELECT
                                            crossing.osm_node_id,
                                            crossing.status,
                                            osm.traffic_signals,
                                            osm.island,
                                            osm.unmarked,
                                            osm.button_operated,
                                            osm.sloped_curb,
                                            osm.tactile_paving,
                                            osm.traffic_signals_vibration,
                                            osm.traffic_signals_sound,
                                            ST_X(osm.wkb_geometry) AS x,
                                            ST_Y(osm.wkb_geometry) AS y
                                            FROM crossing.crossing AS crossing
                                            INNER JOIN crossing.osm_crossings AS osm
                                            ON crossing.osm_node_id = osm.osm_id
                                            ORDER BY id;");
    }

    public function getRating($crossingId) {
        return pg_query($this->connection, "SELECT sc.value AS sc_value, i.value AS i_value, t.value AS t_value, rating.image_weblink, rating.comment, u.name FROM crossing.rating AS rating
                                            INNER JOIN crossing.user AS u
                                            ON user_id = u.id
                                            INNER JOIN crossing.spatial_clarity AS sc
                                            ON spatial_clarity_id = sc.id
                                            INNER JOIN crossing.illumination AS i
                                            ON illumination_id = i.id
                                            INNER JOIN crossing.traffic AS t
                                            ON traffic_id = t.id
                                            WHERE crossing_id = $crossingId;");
    }

    public function getLineChartStatistic() {
        return pg_query($this->connection, "SELECT date_trunc('week', enquiry_date) AS week, MAX(amount) AS amount FROM statistic.crossingstatistic
                                            GROUP BY week
                                            ORDER BY week ASC;");
    }

    public function getWeekAmount() {
        return pg_query($this->connection, "SELECT COUNT(DISTINCT date_trunc('week', enquiry_date)) AS week_amount FROM statistic.crossingstatistic;");
    }

    public function getBarChartStatistic($offset) {
        return pg_query($this->connection, "SELECT date_trunc('week', enquiry_date) AS week, MAX(amount) AS amount FROM statistic.crossingstatistic
                                            GROUP BY week
                                            ORDER BY week ASC
                                            OFFSET $offset;");
    }

    public function getClusteredAmount($number) {
        return pg_query($this->connection, "SELECT COUNT(*) AS amount FROM
                                            (SELECT COUNT(wkb_geometry) FROM crossing.osm_crossings
                                            GROUP BY ST_SnapToGrid(wkb_geometry, $number))
                                            AS tmp;");
    }

    public function getCrossingAmount() {
        return pg_query($this->connection, "SELECT COUNT(*) AS amount
                                            FROM crossing.osm_crossings;");
    }

    public function setCrossingAmount($amount) {
        pg_query($this->connection, "INSERT INTO statistic.crossingstatistic (amount) VALUES ($amount);");
    }
}
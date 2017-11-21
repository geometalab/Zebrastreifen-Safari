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
        $this->connection = pg_connect(
            "host=".getenv('DB_HOST')
            ." port=".getenv('DB_PORT')
            ." dbname=".getenv('DB_NAME')
            ." user=".getenv('DB_USER')
            ." password=".getenv('DB_PASSWORD')
        )
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
                                            osm.traffic_signals_sound,
                                            ST_X(osm.wkb_geometry) AS x,
                                            ST_Y(osm.wkb_geometry) AS y
                                            FROM crossing.crossing AS crossing
                                            INNER JOIN crossing.osm_crossings AS osm
                                            ON crossing.osm_node_id = osm.osm_id
                                            WHERE crossing.osm_node_id = $osm_node_id;");
    }

    public function getAllCrossings($snap, $bounds) {
        return pg_query($this->connection, "SELECT
                                            array_agg(osm.osm_id) AS osm_node_id,
                                            array_agg(crossing.status) AS status,
                                            array_agg(osm.traffic_signals) AS traffic_signals,
                                            array_agg(osm.island) AS island,
                                            array_agg(osm.unmarked) AS unmarked,
                                            array_agg(osm.button_operated) AS button_operated,
                                            array_agg(osm.sloped_curb) AS sloped_curb,
                                            array_agg(osm.tactile_paving) AS tactile_paving,
                                            array_agg(osm.traffic_signals_vibration) AS traffic_signals_vibration,
                                            array_agg(osm.traffic_signals_sound) AS traffic_signals_sound,
                                            ST_X(ST_Centroid(ST_Collect(osm.wkb_geometry))) AS x,
                                            ST_Y(ST_Centroid(ST_Collect(osm.wkb_geometry))) AS y,
                                            count(*) AS amount
                                            FROM crossing.osm_crossings AS osm
                                            LEFT JOIN crossing.crossing AS crossing
                                            ON osm.osm_id = crossing.osm_node_id
                                            WHERE ST_Contains(ST_GeomFromText('POLYGON(($bounds[0] $bounds[1],$bounds[0] $bounds[3],$bounds[2] $bounds[3],$bounds[2] $bounds[1],$bounds[0] $bounds[1]))', 3857), osm.wkb_geometry)
                                            GROUP BY ST_SnapToGrid(osm.wkb_geometry, $snap);");
    }

    public function getRating($crossingId) {
        return pg_query($this->connection, "SELECT sc.value AS sc_value, i.value AS i_value, t.value AS t_value, rating.image_weblink, rating.comment, u.name, rating.last_changed, rating.creation_time FROM crossing.rating AS rating
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

    public function getCrossingBarChartStatistic($offset) {
        return pg_query($this->connection, "SELECT date_trunc('week', enquiry_date) AS week, max(amount) AS amount FROM statistic.crossingstatistic GROUP BY week ORDER BY week ASC OFFSET $offset;");
    }

    public function getCrossingStatisticWeekAmount() {
        return pg_query($this->connection, "SELECT count(DISTINCT date_trunc('week', enquiry_date)) AS week_amount FROM statistic.crossingstatistic;");
    }

    public function getCrossingLineChartStatistic() {
        return pg_query($this->connection, "SELECT date_trunc('week', enquiry_date) AS week, max(amount) AS amount FROM statistic.crossingstatistic GROUP BY week ORDER BY week ASC;");
    }

    public function getRatingBarChartStatistic($offset) {
        return pg_query($this->connection, "SELECT date_trunc('week', enquiry_date) AS week, max(amount) AS amount FROM statistic.ratingstatistic GROUP BY week ORDER BY week ASC OFFSET $offset;");
    }

    public function getRatingStatisticWeekAmount() {
        return pg_query($this->connection, "SELECT count(DISTINCT date_trunc('week', enquiry_date)) AS week_amount FROM statistic.ratingstatistic;");
    }

    public function getRatingLineChartStatistic() {
        return pg_query($this->connection,  "SELECT date_trunc('week', enquiry_date) AS week, max(amount) AS amount FROM statistic.ratingstatistic GROUP BY week ORDER BY week ASC;");
    }

    public function getClusteredAmount($size, $bounds) {
        return pg_query($this->connection, "SELECT count(*) AS amount FROM (SELECT count(osm.wkb_geometry) FROM crossing.osm_crossings AS osm
                                            WHERE ST_Contains(ST_GeomFromText('POLYGON(($bounds[0] $bounds[1],$bounds[0] $bounds[3],$bounds[2] $bounds[3],$bounds[2] $bounds[1],$bounds[0] $bounds[1]))', 3857), osm.wkb_geometry)
                                            GROUP BY ST_SnapToGrid(osm.wkb_geometry, $size)) AS tmp;");
    }

    public function getCrossingAmount() {
        return pg_query($this->connection, "SELECT count(*) AS amount FROM crossing.osm_crossings;");
    }

    public function setCrossingAmount($amount) {
        pg_query($this->connection, "INSERT INTO statistic.crossingstatistic (amount) VALUES ($amount);");
    }

    public function getRatingAmount() {
        return pg_query($this->connection, "SELECT count(*) AS amount FROM crossing.rating AS rating INNER JOIN crossing.crossing AS crossing ON crossing.id = rating.crossing_id WHERE crossing.status = 1;");
    }

    public function setRatingAmount($amount) {
        pg_query($this->connection, "INSERT INTO statistic.ratingstatistic (amount) VALUES ($amount);");
    }

    public function updateCrossingStatus() {
        pg_query($this->connection, "UPDATE crossing.crossing SET status = 0 WHERE status != 0 AND osm_node_id NOT IN (SELECT osm_id FROM crossing.osm_crossings);");
        pg_query($this->connection, "UPDATE crossing.crossing SET status = 1 WHERE status != 1 AND osm_node_id IN (SELECT osm_id FROM crossing.osm_crossings);");
    }
}

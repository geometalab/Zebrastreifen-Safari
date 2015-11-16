<?php
/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 01.10.2015
 * Time: 10:07
 */

class DBZebracrossing extends DBConnection {

    public function __construct() {
        parent::__construct(DBConfig::ZEBRACROSSING);
    }

    public function getZebracrossing($osm_node_id) {
        return pg_query($this->connection, "SELECT zebra.id, zebra.osm_node_id, zebra.status FROM crossing.crossing AS zebra
                                            WHERE zebra.osm_node_id = $osm_node_id;");
    }

    public function getAllZebracrossings() {
        return pg_query($this->connection, "SELECT zebra.osm_node_id, zebra.status FROM crossing.crossing AS zebra
                                            ORDER BY id;");
    }

    public function getRating($id) {
        return pg_query($this->connection, "SELECT sc.value AS sc_value, i.value AS i_value, t.value AS t_value, rating.image_weblink, rating.comment, u.name FROM crossing.rating AS rating
                                            INNER JOIN crossing.user AS u
                                            ON user_id = u.id
                                            INNER JOIN crossing.spatial_clarity AS sc
                                            ON spatial_clarity_id = sc.id
                                            INNER JOIN crossing.illumination AS i
                                            ON illumination_id = i.id
                                            INNER JOIN crossing.traffic AS t
                                            ON traffic_id = t.id
                                            WHERE crossing_id = $id;");
    }

    public function getLineChartStatistic() {
        return pg_query($this->connection, "SELECT date_trunc('week', enquiry_date) AS week, MAX(amount) AS amount FROM statistic.crossingstatistic
                                            GROUP BY week
                                            ORDER BY week ASC;");
    }

    public function getBarChartStatistic() {
        return pg_query($this->connection, "SELECT date_trunc('week', enquiry_date) AS week, MAX(amount) AS amount FROM statistic.crossingstatistic
                                            GROUP BY week
                                            ORDER BY week ASC
                                            offset (SELECT COUNT(DISTINCT date_trunc('week', enquiry_date)) FROM statistic.crossingstatistic) - 11;");
    }

    public function setCrossingAmount($amount) {
        pg_query($this->connection, "INSERT INTO statistic.crossingstatistic (amount) VALUES ($amount);");
    }
}
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
        return pg_query($this->connection, "select zebra.id, zebra.osm_node_id, zebra.status from crossing as zebra
                                            where zebra.osm_node_id = $osm_node_id;");
    }

    public function getAllZebracrossings() {
        return pg_query($this->connection, "select zebra.osm_node_id, zebra.status from crossing as zebra
                                            order by id;");
    }

    public function getRating($id) {
        return pg_query($this->connection, "select sc.value as sc_value, i.value as i_value, t.value as t_value, rating.image_weblink, rating.comment, u.name from rating as rating
                                            inner join crossinguser as u
                                            on user_id = u.id
                                            inner join spatial_clarity as sc
                                            on spatial_clarity_id = sc.id
                                            inner join illumination as i
                                            on illumination_id = i.id
                                            inner join traffic as t
                                            on traffic_id = t.id
                                            where crossing_id = $id;");
    }

    public function getLineChartStatistic() {
        //TODO: not the sum of the amount! ask for implementation to get the last available row of the week
        return pg_query($this->connection, "select date_trunc('week', enquiry_date) as week, max(amount) as amount from crossingstatistic
                                            group by week
                                            order by week asc;");
    }

    public function getBarChartStatistic() {
        //TODO: not the sum of the amount! ask for implementation to get the last available row of the week
        return pg_query($this->connection, "select date_trunc('week', enquiry_date) as week, max(amount) as amount from crossingstatistic
                                            group by week
                                            order by week asc
                                            offset (select count(distinct date_trunc('week', enquiry_date)) from crossingstatistic) - 11;");
    }
}
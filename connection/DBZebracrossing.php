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

    public function getZebracrossing($node) {
        return pg_query($this->connection, "select zebra.zebracrossing_id, zebra.node, zebra.image from crossing.zebracrossing as zebra
                                            where zebra.node = $node
                                            order by zebracrossing_id;");
    }

    public function getAllZebracrossings() {
        return pg_query($this->connection, "select zebra.zebracrossing_id, zebra.node, zebra.image from crossing.zebracrossing as zebra
                                            order by zebracrossing_id;");
    }

    public function getRating($id) {
        return pg_query($this->connection, "select rating.comment, u.name, o.overview_value, i.illumination_value, t.traffic_value from crossing.rating as rating
                                            inner join crossing.user as u
                                            on user_fk = u.user_id
                                            inner join crossing.overview as o
                                            on overview_fk = o.overview_id
                                            inner join crossing.illumination as i
                                            on illumination_fk = i.illumination_id
                                            inner join crossing.traffic as t
                                            on traffic_fk = t.traffic_id
                                            where zebracrossing_fk = $id;");
    }

    public function getLineChartStatistic() {
        return pg_query($this->connection, "select date_trunc('week', date) as week, sum(zebracrossing_amount) as total from statistic.zebracrossingstatistic
                                            group by week
                                            order by week asc;");
    }

    public function getBarChartStatistic() {
        return pg_query($this->connection, "select date_trunc('week', date) as week, sum(zebracrossing_amount) as amount from statistic.zebracrossingstatistic
                                            group by week
                                            order by week asc
                                            offset (select count(distinct date_trunc('week', date)) from statistic.zebracrossingstatistic) - 10;");
    }
}
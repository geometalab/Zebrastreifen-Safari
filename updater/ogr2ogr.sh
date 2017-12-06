#!/bin/sh
psql -c 'DROP TABLE IF EXISTS crossing.osm_crossings;'

ogr2ogr -f PostgreSQL \
  PG:"dbname='${PGDATABASE}' host='${PGHOST}' port='${PGPORT}' user='${PGUSER}' password='${PGPASSWORD}'" \
  PG:"dbname='gis_db' host='${PGHOST}' port='${PGPORT}' user='${PGUSER}' password='${PGPASSWORD}'" \
  -sql "SELECT cr.osm_id, CASE WHEN cr.tags @> hstore('sloped_curb', 'one') THEN 'one' \
    WHEN cr.tags @> hstore('sloped_curb', 'both') THEN 'both' ELSE 'no' END as sloped_curb, cr.way as geom, \
    coalesce(cr.tags->'crossing' LIKE '%traffic_signals%', false) as traffic_signals, coalesce(cr.tags->'crossing' \
    LIKE '%island%', false) as island, coalesce(cr.tags->'crossing' LIKE '%unmarked%', true) as unmarked, \
    cr.tags @> hstore('button_operated', 'yes') as button_operated, cr.tags @> hstore('tactile_paving', 'yes') as tactile_paving, \
    cr.tags @> hstore('traffic_signals:vibration', 'yes') as traffic_signals_vibration, cr.tags @> hstore('traffic_signals:sound', 'yes') as traffic_signals_sound \
    FROM osm_poi cr, (SELECT way FROM osm_polygon WHERE osm_id = -51701) AS sw WHERE cr.tags @> hstore('highway','crossing') AND ST_Within(cr.way, sw.way)" \
  -nln "osm_crossings" \
  -overwrite \
  -lco SCHEMA=crossing \
  -lco OVERWRITE=YES

package ch.hsr.ifs.zebrastreifensafari;

import ch.hsr.ifs.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.ifs.zebrastreifensafari.service.crossing.CrossingDataService;
import ch.hsr.ifs.zebrastreifensafari.service.crossing.ICrossingDataService;
import ch.hsr.ifs.zebrastreifensafari.service.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Locale;

/**
 * @author aeugster
 */
public final class TestJDBC {

    private Connection c;
    private ICrossingDataService crossingDataService;

    public TestJDBC() {
        connect();
        createTables();
        crossingDataService = new CrossingDataService("ZebraPUTest");
        DataServiceLoader.provideCrossingData(crossingDataService);
        Properties.setLanguage(Locale.GERMAN);
    }

    public static void main(String args[]) {
        new TestJDBC();
    }

    private void connect() {
        c = null;
        EntityManager entityManager = Persistence.createEntityManagerFactory("ZebraPUTest").createEntityManager();
        entityManager.getTransaction().begin();
        c = entityManager.unwrap(Connection.class);
        entityManager.getTransaction().commit();

        if (c != null) {
            System.out.println("Opened database successfully");
        }
    }

    private void createTables() {
        String createString = "\n"
                + "-- drop --\n"
                + "drop table if exists crossing.rating;"
                + "drop table if exists crossing.crossing;"
                + "drop table if exists crossing.user;"
                + "drop table if exists crossing.illumination;"
                + "drop table if exists crossing.traffic;"
                + "drop table if exists crossing.spatial_clarity;"
                + "\n"
                + "drop schema if exists crossing;"
                + "create schema crossing;"
                + "\n"
                + "-- create --\n"
                + "-- crossing schema --\n"
                + "create table crossing.user ("
                + "	id serial primary key not null,"
                + "	name varchar(45) not null,"
                + "	initials varchar(4) not null"
                + ");"
                + "\n"
                + "create table crossing.spatial_clarity ("
                + "	id serial primary key not null,"
                + "	value varchar(10) not null"
                + ");"
                + "\n"
                + "create table crossing.traffic ("
                + "	id serial primary key not null,"
                + "	value varchar(10) not null"
                + ");"
                + "\n"
                + "create table crossing.illumination ("
                + "	id serial primary key not null,"
                + "	value varchar(10) not null"
                + ");"
                + "\n"
                + "create table crossing.crossing ("
                + "	id serial primary key not null,"
                + "	osm_node_id bigint not null,"
                + "	status int not null default 1"
                + ");"
                + "\n"
                + "create table crossing.rating ("
                + " id serial primary key not null,"
                + "	crossing_id integer references crossing.crossing(id) on delete cascade,"
                + "	spatial_clarity_id integer references crossing.spatial_clarity(id),"
                + "	traffic_id integer references crossing.traffic(id),"
                + "	illumination_id integer references crossing.illumination(id),"
                + "	image_weblink varchar(256),"
                + "	comment varchar(500),"
                + "	user_id integer references crossing.user(id),"
                + "	last_changed timestamp not null default CURRENT_TIMESTAMP,"
                + " creation_time timestamp"
                + ");"
                + "\n"
                + "create unique index osm_node_id_ix on crossing.crossing (osm_node_id);"
                + "create unique index image_weblink_ix on crossing.rating (image_weblink);"
                + "\n"
                + "\n"
                + "-- insert --\n"
                + "insert into crossing.user (name, initials) values"
                + "	('Mike Marti', 'MM'),"
                + "	('Joël Schwab', 'JS'),"
                + "	('Alex Eugster', 'AE'),"
                + "	('Fabienne König', 'FK');"
                + "	\n"
                + "insert into crossing.spatial_clarity (value) values"
                + "	('Gut'),"
                + "	('Mittel'),"
                + "	('Schlecht');"
                + "	\n"
                + "insert into crossing.traffic (value) values"
                + "	('Wenig'),"
                + "	('Mittel'),"
                + "	('Viel');"
                + "	\n"
                + "insert into crossing.illumination (value) values"
                + "	('Gut'),"
                + "	('Mittel'),"
                + "	('Schlecht'),"
                + "	('keine');"
                + "\n"
                + "insert into crossing.crossing (osm_node_id) values"
                + "	(906080382),"
                + "	(277946452),"
                + "	(2205108358),"
                + "	(3179298669),"
                + "	(471607659),"
                + "	(3011400408),"
                + "	(817599603),"
                + "	(29773948),"
                + "	(2025691126),"
                + "	(2192517681),"
                + "	(448041072);"
                + "	\n"
                + "insert into crossing.rating (crossing_id, spatial_clarity_id, traffic_id, illumination_id, image_weblink, comment, user_id) values"
                + "	(1, 3, 1, 2, 'https://hello.com/uiodfhh09', null, 2),"
                + "	(1, 1, 2, 4, 'https://hello.com/u52df', 'good illumination', 1),"
                + "	(2, 2, 1, 3, 'https://hello.com/26sdaf', 'a lot of traffic', 1),"
                + "	(3, 2, 3, 4, 'https://hello.com/adfah134ad', null, 4),"
                + "	(4, 1, 1, 3, 'https://hello.com/adfq35z3zqda', null, 3),"
                + "	(5, 2, 2, 1, 'https://hello.com/q3tfadva', 'bearly any light', 4),"
                + "	(6, 1, 2, 1, 'https://hello.com/rtq3df', null, 1),"
                + "	(6, 3, 3, 2, 'https://hello.com/q3zqqadfa', null, 3),"
                + "	(6, 3, 1, 4, 'https://hello.com/af2345af', null, 2),"
                + "	(7, 1, 3, 2, 'https://hello.com/juer6e', 'right around a corner', 4),"
                + "	(8, 2, 3, 2, 'https://hello.com/fnbgjd6', null, 2),"
                + "	(9, 2, 2, 3, 'https://hello.com/argfadc', null, 1),"
                + "	(9, 1, 1, 1, 'https://hello.com/05rr8', null, 3),"
                + "	(10, 3, 1, 4, 'https://hello.com/wu42th', null, 1),"
                + "	(11, 1, 1, 2, 'https://hello.com/rhw46uejt', 'right next to a corner. but theres not a lot of traffic there eighter', 4);";
        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate(createString);
            System.out.println("Tables created successuflly");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return c;
    }

    public ICrossingDataService getCrossingDataService() {
        return crossingDataService;
    }

    public void addUser(String name, String initials) {
        connect();
        try (Statement stmt = c.createStatement()) {
            String createString = "insert into crossing.user (name, initials) values"
                    + "	('" + name + "', '" + initials + "');";

            stmt.executeUpdate(createString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

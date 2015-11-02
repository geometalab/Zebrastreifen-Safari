package ch.hsr.zebrastreifensafari;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.Statement;

/*
 * @author aeugster
 */
public class TestJDBC {

    private Connection c;

    public TestJDBC() {
        this.connect();
        this.createTables();
    }

    public void connect() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("ZebraPUTest").createEntityManager();
        entityManager.getTransaction().begin();
        c = entityManager.unwrap(Connection.class);
        entityManager.getTransaction().commit();

        if (c != null) {
            System.out.println("Opened database successfully");
        }
    }

    public void createTables() {
        String createString = ""
                + "-- drop --\n"
                + "drop table if exists crossing.rating;\n"
                + "drop table if exists crossing.crossing;\n"
                + "drop table if exists crossing.user;\n"
                + "drop table if exists crossing.illumination;\n"
                + "drop table if exists crossing.traffic;\n"
                + "drop table if exists crossing.spatial_clarity;\n"
                + "drop table if exists statistic.crossingstatistic;\n"
                + "\n"
                + "drop schema if exists crossing;"
                + "drop schema if exists statistic;"
                + "create schema crossing;"
                + "create schema statistic;"
                + "\n"
                + "-- create --\n"
                + "-- crossing schema --\n"
                + "create table crossing.user (\n"
                + "	id serial primary key not null,\n"
                + "	name varchar(45) not null,\n"
                + "	initials varchar(4) not null\n"
                + ");\n"
                + "\n"
                + "create table crossing.spatial_clarity (\n"
                + "	id serial primary key not null,\n"
                + "	value varchar(10) not null\n"
                + ");\n"
                + "\n"
                + "create table crossing.traffic (\n"
                + "	id serial primary key not null,\n"
                + "	value varchar(10) not null\n"
                + ");\n"
                + "\n"
                + "create table crossing.illumination (\n"
                + "	id serial primary key not null,\n"
                + "	value varchar(10) not null\n"
                + ");\n"
                + "\n"
                + "create table crossing.crossing (\n"
                + "	id serial primary key not null,\n"
                + "	osm_node_id bigint not null,\n"
                + "	status int not null default 1\n"
                + ");\n"
                + "\n"
                + "create table crossing.rating (\n"
                + "	id serial primary key not null,\n"
                + "	crossing_id integer references crossing.crossing(id) on delete cascade,\n"
                + "	spatial_clarity_id integer references crossing.spatial_clarity(id),\n"
                + "	traffic_id integer references crossing.traffic(id),\n"
                + "	illumination_id integer references crossing.illumination(id),\n"
                + "	image_weblink varchar(256),\n"
                + "	comment varchar(500),\n"
                + "	user_id integer references crossing.user(id),\n"
                + "	last_changed timestamp not null default CURRENT_TIMESTAMP\n"
                + ");\n"
                + "\n"
                + "create unique index osm_node_id_ix on crossing.crossing (osm_node_id);\n"
                + "create unique index image_weblink_ix on crossing.rating (image_weblink);\n"
                + "\n"
                + "-- statistic schema --\n"
                + "-- comment on table statistic.crossingstatistic is 'my comment';\n"
                + "create table statistic.crossingstatistic (\n"
                + "	id serial primary key not null,\n"
                + "	enquiry_date date not null,\n"
                + "	amount integer\n"
                + ");\n"
                + "\n"
                + "create unique index enquiry_date_ix on statistic.crossingstatistic(enquiry_date);\n"
                + "\n"
                + "\n"
                + "-- insert --\n"
                + "-- crossing schema --\n"
                + "insert into crossing.user (name, initials) values\n"
                + "	('Mike Marti', 'MM'),\n"
                + "	('Joël Schwab', 'JS'),\n"
                + "	('Alex Eugster', 'AE'),\n"
                + "	('Fabienne König', 'FK');\n"
                + "	\n"
                + "insert into crossing.spatial_clarity (value) values\n"
                + "	('Gut'),\n"
                + "	('Mittel'),\n"
                + "	('Schlecht');\n"
                + "	\n"
                + "insert into crossing.traffic (value) values\n"
                + "	('Wenig'),\n"
                + "	('Mittel'),\n"
                + "	('Viel');\n"
                + "	\n"
                + "insert into crossing.illumination (value) values\n"
                + "	('Gut'),\n"
                + "	('Mittel'),\n"
                + "	('Schlecht'),\n"
                + "	('keine');\n"
                + "\n"
                + "insert into crossing.crossing (osm_node_id) values\n"
                + "	(906080382),\n"
                + "	(277946452),\n"
                + "	(2205108358),\n"
                + "	(3179298669),\n"
                + "	(471607659),\n"
                + "	(3011400408),\n"
                + "	(817599603),\n"
                + "	(29773948),\n"
                + "	(2025691126),\n"
                + "	(2192517681),\n"
                + "	(448041072);\n"
                + "	\n"
                + "-- spatial_clarity 1-3\n"
                + "-- traffic 1-3\n"
                + "-- illumination 1-4\n"
                + "-- user 1-4\n"
                + "insert into crossing.rating (crossing_id, spatial_clarity_id, traffic_id, illumination_id, image_weblink, comment, user_id) values\n"
                + "	(1, 3, 1, 2, 'https://hello.com/uiodfhh09', null, 2),\n"
                + "	(1, 1, 2, 4, 'https://hello.com/u52df', 'good illumination', 1),\n"
                + "	(2, 2, 1, 3, 'https://hello.com/26sdaf', 'a lot of traffic', 1),\n"
                + "	(3, 2, 3, 4, 'https://hello.com/adfah134ad', null, 4),\n"
                + "	(4, 1, 1, 3, 'https://hello.com/adfq35z3zqda', null, 3),\n"
                + "	(5, 2, 2, 1, 'https://hello.com/q3tfadva', 'bearly any light', 4),\n"
                + "	(6, 1, 2, 1, 'https://hello.com/rtq3df', null, 1),\n"
                + "	(6, 3, 3, 2, 'https://hello.com/q3zqqadfa', null, 3),\n"
                + "	(6, 3, 1, 4, 'https://hello.com/af2345af', null, 2),\n"
                + "	(7, 1, 3, 2, 'https://hello.com/juer6e', 'right around a corner', 4),\n"
                + "	(8, 2, 3, 2, 'https://hello.com/fnbgjd6', null, 2),\n"
                + "	(9, 2, 2, 3, 'https://hello.com/argfadc', null, 1),\n"
                + "	(9, 1, 1, 1, 'https://hello.com/05rr8', null, 3),\n"
                + "	(10, 3, 1, 4, 'https://hello.com/wu42th', null, 1),\n"
                + "	(11, 1, 1, 2, 'https://hello.com/rhw46uejt', 'right next to a corner. but theres not a lot of traffic there eighter', 4);\n"
                + "\n"
                + "	\n"
                + "-- statistic schema --\n"
                + "insert into statistic.crossingstatistic (enquiry_date, amount) values\n"
                + "	('1999-02-21', 50),\n"
                + "	('2000-10-10', 84),\n"
                + "	('2005-08-02', 99),\n"
                + "	('2005-08-03', 154),\n"
                + "	('2005-08-04', 278),\n"
                + "	('2005-08-05', 687),\n"
                + "	('2005-08-06', 876),\n"
                + "	('2005-12-10', 889),\n"
                + "	('2006-08-10', 945),\n"
                + "	('2006-10-05', 991),\n"
                + "	('2006-12-17', 1237),\n"
                + "	('2009-06-02', 1755),\n"
                + "	('2010-01-22', 1945),\n"
                + "	('2010-12-15', 2278),\n"
                + "	('2011-01-01', 2378),\n"
                + "	('2011-06-30', 2687),\n"
                + "	('2012-05-05', 3105),\n"
                + "	('2015-08-06', 3746);";
        try {
            c.setAutoCommit(true);
            Statement stmt = c.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();

            //c.commit();
            c.close();

            System.out.println("Tables created successuflly");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return c;
    }

    public static void main(String args[]) {
        TestJDBC tj = new TestJDBC();
    }

}

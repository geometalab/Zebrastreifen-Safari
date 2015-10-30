package ch.hsr.zebrastreifensafari;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteConnection;

/*
 * @author aeugster
 */
public class TestJDBC {

    private SQLiteConnection c;

    public TestJDBC() {
        this.connect();
        this.createTables();
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = new SQLiteConnection("jdbc:sqlite:Zebrastreifensafari", "crossing.db");
            System.out.println("Opened database successfully");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public void createTables() {
        String createString = "Begin transaction;"
                + "drop table if exists rating;"
                + "drop table if exists crossing;"
                + "drop table if exists crossinguser;"
                + "drop table if exists illumination;"
                + "drop table if exists traffic;"
                + "drop table if exists spatial_clarity;"
                + "drop table if exists crossingstatistic;"
                + "CREATE TABLE crossinguser (\n"
                + "	id integer primary key autoincrement,\n"
                + "	name varchar(45) not null,\n"
                + "	initials varchar(4) not null\n"
                + ");\n"
                + "INSERT INTO crossinguser (id,name,initials) VALUES (1,'Mike Marti','MM');\n"
                + "INSERT INTO crossinguser (id,name,initials) VALUES (2,'Joël Schwab','JS');\n"
                + "INSERT INTO crossinguser (id,name,initials) VALUES (3,'Alex Eugster','AE');\n"
                + "INSERT INTO crossinguser (id,name,initials) VALUES (4,'Fabienne König','FK');\n"
                + "CREATE TABLE traffic (\n"
                + "	id integer primary key autoincrement,\n"
                + "	value varchar(10) not null\n"
                + ");\n"
                + "INSERT INTO traffic (id,value) VALUES (1,'Wenig');\n"
                + "INSERT INTO traffic (id,value) VALUES (2,'Mittel');\n"
                + "INSERT INTO traffic (id,value) VALUES (3,'Viel');\n"
                + "CREATE TABLE spatial_clarity (\n"
                + "	id integer primary key autoincrement,\n"
                + "	value varchar(10) not null\n"
                + ");\n"
                + "INSERT INTO spatial_clarity (id,value) VALUES (1,'Gut');\n"
                + "INSERT INTO spatial_clarity (id,value) VALUES (2,'Mittel');\n"
                + "INSERT INTO spatial_clarity (id,value) VALUES (3,'Schlecht');\n"
                + "CREATE TABLE rating (\n"
                + "	id integer primary key autoincrement,\n"
                + "	crossing_id integer references crossing(id) on delete cascade,\n"
                + "	spatial_clarity_id integer references spatial_clarity(id),\n"
                + "	traffic_id integer references traffic(id),\n"
                + "	illumination_id integer references illumination(id),\n"
                + "	image_weblink varchar(256),\n"
                + "	comment varchar(500),\n"
                + "	user_id integer references crossinguser(id),\n"
                + "	last_changed timestamp not null default CURRENT_TIMESTAMP\n"
                + ");\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (1,1,3,1,2,'https://hello.com/uiodfhh09',NULL,2,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (2,1,1,2,4,'https://hello.com/u52df','good illumination',1,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (3,2,2,1,3,'https://hello.com/26sdaf','a lot of traffic',1,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (4,3,2,3,4,'https://hello.com/adfah134ad',NULL,4,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (5,4,1,1,3,'https://hello.com/adfq35z3zqda',NULL,3,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (6,5,2,2,1,'https://hello.com/q3tfadva','bearly any light',4,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (7,6,1,2,1,'https://hello.com/rtq3df',NULL,1,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (8,6,3,3,2,'https://hello.com/q3zqqadfa',NULL,3,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (9,6,3,1,4,'https://hello.com/af2345af',NULL,2,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (10,7,1,3,2,'https://hello.com/juer6e','right around a corner',4,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (11,8,2,3,2,'https://hello.com/fnbgjd6',NULL,2,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (12,9,2,2,3,'https://hello.com/argfadc',NULL,1,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (13,9,1,1,1,'https://hello.com/05rr8',NULL,3,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (14,10,3,1,4,'https://hello.com/wu42th',NULL,1,'2015-10-29 11:49:02');\n"
                + "INSERT INTO rating (id,crossing_id,spatial_clarity_id,traffic_id,illumination_id,image_weblink,comment,user_id,last_changed) VALUES (15,11,1,1,2,'https://hello.com/rhw46uejt','right next to a corner. but theres not a lot of traffic there eighter',4,'2015-10-29 11:49:02');\n"
                + "CREATE TABLE illumination (\n"
                + "	id integer primary key autoincrement,\n"
                + "	value varchar(10) not null\n"
                + ");\n"
                + "INSERT INTO illumination (id,value) VALUES (1,'Gut');\n"
                + "INSERT INTO illumination (id,value) VALUES (2,'Mittel');\n"
                + "INSERT INTO illumination (id,value) VALUES (3,'Schlecht');\n"
                + "INSERT INTO illumination (id,value) VALUES (4,'keine');\n"
                + "CREATE TABLE crossingstatistic (\n"
                + "	id integer primary key autoincrement,\n"
                + "	enquiry_date date not null,\n"
                + "	amount integer\n"
                + ");\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (1,'1999-02-21',50);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (2,'2000-10-10',84);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (3,'2005-08-02',99);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (4,'2005-08-03',154);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (5,'2005-08-04',278);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (6,'2005-08-05',687);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (7,'2005-08-06',876);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (8,'2005-12-10',889);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (9,'2006-08-10',945);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (10,'2006-10-05',991);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (11,'2006-12-17',1237);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (12,'2009-06-02',1755);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (13,'2010-01-22',1945);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (14,'2010-12-15',2278);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (15,'2011-01-01',2378);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (16,'2011-06-30',2687);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (17,'2012-05-05',3105);\n"
                + "INSERT INTO crossingstatistic (id,enquiry_date,amount) VALUES (18,'2015-08-06',3746);\n"
                + "CREATE TABLE crossing (\n"
                + "	id integer primary key autoincrement,\n"
                + "	osm_node_id bigint not null,\n"
                + "	status int not null default 1\n"
                + ");\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (1,906080382,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (2,277946452,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (3,2205108358,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (4,3179298669,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (5,471607659,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (6,3011400408,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (7,817599603,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (8,29773948,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (9,2025691126,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (10,2192517681,1);\n"
                + "INSERT INTO crossing (id,osm_node_id,status) VALUES (11,448041072,1);\n"
                + "CREATE UNIQUE INDEX osm_node_id_ix on crossing (osm_node_id);\n"
                + "CREATE UNIQUE INDEX image_weblink_ix on rating (image_weblink);\n"
                + "CREATE UNIQUE INDEX enquiry_date_ix on crossingstatistic(enquiry_date);\n"
                + "COMMIT;";
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

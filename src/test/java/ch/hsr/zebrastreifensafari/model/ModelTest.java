package ch.hsr.zebrastreifensafari.model;

import ch.hsr.zebrastreifensafari.TestJDBC;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.crossing.CrossingDataService;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 13:38
 * @date : 21.10.2015
 */
public class ModelTest {

    TestJDBC db;
    Model model;

    @Before
    public void setUp() throws Exception {
        db = new TestJDBC();
        DataServiceLoader.provideCrossingData(new CrossingDataService("ZebraPUTest"));
        model = new Model();
    }

    @After
    public void tearDown() throws Exception {
        try {

            File file = new File("crossing.db");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReloadCrossing() throws Exception {

    }

    @Test
    public void testReloadRating() throws Exception {

    }

    @Test
    public void testReloadUsers() throws Exception {

    }

    @Test
    public void testGetUserByValidName() throws Exception {
        assertEquals("Alex Eugster", model.getUser("Alex Eugster").getName());
    }

    @Test
    public void testGetCrossing() throws Exception {

    }

    @Test
    public void testGetCrossing1() throws Exception {

    }

    @Test
    public void testGetRating() throws Exception {

    }

    @Test
    public void testGetIllumination() throws Exception {

    }

    @Test
    public void testGetSpatialClarity() throws Exception {

    }

    @Test
    public void testGetTraffic() throws Exception {

    }

    @Test
    public void testGetUsers() throws Exception {

    }

    @Test
    public void testGetCrossings() throws Exception {

    }

    @Test
    public void testGetRatings() throws Exception {

    }

    @Test
    public void testIsRatingMode() throws Exception {

    }

    @Test
    public void testSetRatingMode() throws Exception {

    }

}

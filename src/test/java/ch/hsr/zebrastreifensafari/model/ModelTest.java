package ch.hsr.zebrastreifensafari.model;

import ch.hsr.zebrastreifensafari.TestJDBC;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.crossing.CrossingDataService;
import java.io.File;
import java.util.Date;
import java.util.List;
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
        db.getConnection().close();
//        try {
//
//            File file = new File("crossing.db");
//            file.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testReloadCrossing() throws Exception {
        assertEquals(null, model.getCrossing((long)87));
        Crossing c = new Crossing(null, 87, 1, 1);
        DataServiceLoader.getCrossingData().addCrossing(c);
        //Rating r = new Rating(null, "test Comment", model.getIllumination(1), model.getSpatialClarity(1), model.getTraffic(1), model.getUser("Alex Eugster"), c, null, new Date());
        //DataServiceLoader.getCrossingData().addRating(r);
        model.reloadCrossing();
        assertEquals(87, model.getCrossing((long)87).getOsmNodeId());

    }

//    @Test
//    public void testReloadRating() throws Exception {
//        assertEquals(null, model.getRating(16));
//        Rating r = new Rating(0, "test Comment", model.getIllumination(1), model.getSpatialClarity(1), model.getTraffic(1), model.getUser("Alex Eugster"), model.getCrossing(1), "", new Date());
//        DataServiceLoader.getCrossingData().addRating(r);
//        model.reloadRating(model.getCrossing(1));
//        assertEquals("test Comment", model.getRating(16).getComment());
//
//    }

//    @Test
//    public void testReloadUsers() throws Exception {
//        assertEquals(null, model.getUser("Bob Ford"));
//        db.addUser("Bob Ford", "BF");
//        model.reloadUsers();
//        assertEquals("Bob Ford", model.getUser("Bob Ford").getName());
//    }
//
//    @Test
//    public void testGetUserByValidExistingName() throws Exception {
//        assertEquals("Alex Eugster", model.getUser("Alex Eugster").getName());
//    }
//
//    @Test
//    public void testGetUserByValidNonExistingName() throws Exception {
//        assertEquals(null, model.getUser("Max Meier"));
//    }
//
//    @Test
//    public void testGetUserByInvalidName() throws Exception {
//        assertEquals(null, model.getUser(null));
//    }
//
//    @Test
//    public void testGetCrossing() throws Exception {
//
//    }
//
//    @Test
//    public void testGetCrossing1() throws Exception {
//
//    }
//
//    @Test
//    public void testGetRating() throws Exception {
//
//    }
//
//    @Test
//    public void testGetIllumination() throws Exception {
//
//    }
//
//    @Test
//    public void testGetSpatialClarity() throws Exception {
//
//    }
//
//    @Test
//    public void testGetTraffic() throws Exception {
//
//    }
//
//    @Test
//    public void testGetUsers() throws Exception {
//
//    }
//
//    @Test
//    public void testGetCrossings() throws Exception {
//
//    }
//
//    @Test
//    public void testGetRatings() throws Exception {
//
//    }
//
//    @Test
//    public void testIsRatingMode() throws Exception {
//
//    }
//
//    @Test
//    public void testSetRatingMode() throws Exception {
//
//    }

}

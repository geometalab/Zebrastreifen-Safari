package ch.hsr.zebrastreifensafari.service.crossing;

import ch.hsr.zebrastreifensafari.TestJDBC;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import java.util.ArrayList;
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
 * @time : 13:40
 * @date : 21.10.2015
 */
public class CrossingDataServiceTest {

    private TestJDBC db;
    private CrossingDataService cds;

    @Before
    public void setUp() throws Exception {
        db = new TestJDBC();
        cds = new CrossingDataService("ZebraPUTest");
        DataServiceLoader.provideCrossingData(cds);
    }

    @After
    public void tearDown() throws Exception {
        db.getConnection().close();
    }

    @Test
    public void testGetUsers() throws Exception {
        assertEquals(4, cds.getUsers().size());
    }

    @Test
    public void testGetCrossings() throws Exception {
        assertEquals(11, cds.getCrossings().size());
    }

    @Test
    public void testGetRatings() throws Exception {
        assertEquals(1, cds.getRatings(cds.getCrossings().get(2)).size());
    }

    @Test
    public void testGetIlluminations() throws Exception {
        assertEquals(4, cds.getIlluminations().size());
    }

    @Test
    public void testGetSpatialClaritys() throws Exception {
        assertEquals(3, cds.getSpatialClaritys().size());
    }

    @Test
    public void testGetTraffics() throws Exception {
        assertEquals(3, cds.getTraffics().size());
    }

    @Test
    public void testRemoveCrossing() throws Exception {
        assertEquals(11, cds.getCrossings().size());
        cds.removeCrossing(6);
        assertEquals(10, cds.getCrossings().size());
    }

    @Test
    public void testAddCrossing() throws Exception {
        assertEquals(11, cds.getCrossings().size());
        cds.addCrossing(new Crossing(), new Model());
        assertEquals(12, cds.getCrossings().size());
    }

    @Test
    public void testAddRating() throws Exception {
        assertEquals(1, cds.getRatings(cds.getCrossings().get(2)).size());
        cds.addRating(new Rating(null, "test Comment", cds.getIlluminations().get(1), cds.getSpatialClaritys().get(1),
                cds.getTraffics().get(1), cds.getUsers().get(1), cds.getCrossings().get(2), "", new Date()));
        assertEquals(2, cds.getRatings(cds.getCrossings().get(2)).size());
    }

    @Test
    public void testRemoveRating() throws Exception {
        assertEquals(3, cds.getRatings(cds.getCrossings().get(7)).size());
        cds.removeRating(7);
        assertEquals(2, cds.getRatings(cds.getCrossings().get(7)).size());

    }

    @Test
    public void testUpdateRating() throws Exception {

    }

    @Test
    public void testUpdateCrossing() throws Exception {

    }
}

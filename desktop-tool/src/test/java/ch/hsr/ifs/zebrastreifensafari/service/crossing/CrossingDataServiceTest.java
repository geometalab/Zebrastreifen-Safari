package ch.hsr.ifs.zebrastreifensafari.service.crossing;

import ch.hsr.ifs.zebrastreifensafari.TestJDBC;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Crossing;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Rating;
import ch.hsr.ifs.zebrastreifensafari.model.Model;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 13:40
 * @date : 21.10.2015
 */
public class CrossingDataServiceTest {

    private TestJDBC db;

    @Before
    public void setUp() throws Exception {
        db = new TestJDBC();
    }

    @After
    public void tearDown() throws Exception {
        db.getConnection().close();
    }

    @Test
    public void testGetUsers() throws Exception {
        Assert.assertEquals(4, db.getCrossingDataService().getUsers().size());
    }

    @Test
    public void testGetCrossings() throws Exception {
        Assert.assertEquals(11, db.getCrossingDataService().getCrossings().size());
    }

    @Test
    public void testGetRatings() throws Exception {
        Crossing crossing = new Model().getCrossing(2);
        Assert.assertEquals(1, db.getCrossingDataService().getRatings(crossing).size());
    }

    @Test
    public void testGetIlluminations() throws Exception {
        Assert.assertEquals(4, db.getCrossingDataService().getIlluminations().size());
    }

    @Test
    public void testGetSpatialClaritys() throws Exception {
        Assert.assertEquals(3, db.getCrossingDataService().getSpatialClarities().size());
    }

    @Test
    public void testGetTraffics() throws Exception {
        Assert.assertEquals(3, db.getCrossingDataService().getTraffics().size());
    }

    @Test
    public void testRemoveCrossing() throws Exception {
        Assert.assertEquals(11, db.getCrossingDataService().getCrossings().size());
        db.getCrossingDataService().removeCrossing(6);
        Assert.assertEquals(10, db.getCrossingDataService().getCrossings().size());
    }

    @Test
    public void testAddCrossing() throws Exception {
        Assert.assertEquals(11, db.getCrossingDataService().getCrossings().size());
        db.getCrossingDataService().createCrossing(new Crossing());
        Assert.assertEquals(12, db.getCrossingDataService().getCrossings().size());
    }

    @Test
    public void testAddRating() throws Exception {
        Crossing crossing = new Model().getCrossing(2);
        Assert.assertEquals(1, db.getCrossingDataService().getRatings(crossing).size());
        db.getCrossingDataService().createRating(new Rating(null, "test Comment", db.getCrossingDataService().getIlluminations().get(1), db.getCrossingDataService().getSpatialClarities().get(1),
                db.getCrossingDataService().getTraffics().get(1), db.getCrossingDataService().getUsers().get(1), crossing, "", new Date(), new Date()));
        Assert.assertEquals(2, db.getCrossingDataService().getRatings(crossing).size());
    }

    @Test
    public void testRemoveRating() throws Exception {
        Crossing crossing = new Model().getCrossing(6);
        Assert.assertEquals(3, db.getCrossingDataService().getRatings(crossing).size());
        db.getCrossingDataService().removeRating(db.getCrossingDataService().getRatings(crossing).get(0).getId());
        Assert.assertEquals(2, db.getCrossingDataService().getRatings(crossing).size());

    }

    @Test
    public void testEditRating() throws Exception {

    }

    @Test
    public void testEditCrossing() throws Exception {

    }
}

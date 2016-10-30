package ch.hsr.zebrastreifensafari.model;

import ch.hsr.zebrastreifensafari.TestJDBC;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 13:38
 * @date : 21.10.2015
 */
public class ModelTest {

    private TestJDBC db;
    private Model model;

    @Before
    public void setUp() throws Exception {
        db = new TestJDBC();
        model = new Model();
    }

    @After
    public void tearDown() throws Exception {
        db.getConnection().close();

    }

    //
    @Test
    public void testReloadCrossing() throws Exception {
        assertEquals(null, model.getCrossing((long) 87));
        Crossing c = new Crossing(null, 87, 1, 1);

        DataServiceLoader.getCrossingData().createCrossing(c);
        model.loadCrossing();
        assertEquals(87, model.getCrossing((long) 87).getOsmNodeId());

    }

    @Test
    public void testReloadRating() throws Exception {
        assertEquals(null, model.getRating(16));
        Rating r = new Rating(0, "test Comment", model.getIllumination(1), model.getSpatialClarity(1), model.getTraffic(1), model.getUser("Alex Eugster"), model.getCrossing(1), "", new Date(), new Date());
        DataServiceLoader.getCrossingData().createRating(r);
        model.loadRating(model.getCrossing(1));
        assertEquals("test Comment", model.getRating(16).getComment());

    }

    @Test
    public void testReloadUsers() throws Exception {
        assertEquals(null, model.getUser("Bob Ford"));
        db.addUser("Bob Ford", "BF");
        model.loadUsers();
        assertEquals("Bob Ford", model.getUser("Bob Ford").getName());
    }

    @Test
    public void testGetUserByValidExistingName() throws Exception {
        assertEquals("Alex Eugster", model.getUser("Alex Eugster").getName());
    }

    @Test
    public void testGetUserByValidNonExistingName() throws Exception {
        assertEquals(null, model.getUser("Max Meier"));
    }

    @Test
    public void testGetUserByInvalidName() throws Exception {
        assertEquals(null, model.getUser(null));
    }

}

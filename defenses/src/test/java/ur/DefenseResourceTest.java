package ur;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import org.glassfish.grizzly.http.server.HttpServer;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ur.controller.DefenseController;
import ur.model.Defense;
import ur.model.DefenseDAOImpl;
import ur.model.IDefensetDAO;

/**
 * TODO more tests
 *
 */
public class DefenseResourceTest {

	private HttpServer server;
	private WebTarget target;
	/** reference to the test defense created during the tests **/
	Defense test_subject = null;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();
		// create the client
		Client c = ClientBuilder.newClient();

		target = c.target(Main.BASE_URI);
		// Creation of test Defense
		test_subject = DefenseController.newDefense();
		
	}

	@After
	public void tearDown() throws Exception {
		server.shutdownNow();
		try (Session session = SessionFactoryHolder.getSessionFactory().openSession()) {
			IDefensetDAO dao = new DefenseDAOImpl(session);
			dao.deleteDefense(test_subject);
		}
	}

	/**
	 * check the database returns more than one object of the expected class
	 */
	@Test
	public void testListDefense() {
		List<Defense> response = target.path("defense").request().get(
				new GenericType<List<Defense>>(){});
		assertEquals(response.get(0).getClass(), Defense.class);
	}

	@Test
	public void testGetDefense() {
		int defense_id = test_subject.getId();
		Defense response = target.path("defense/" + defense_id).request().get(Defense.class);
		assertEquals(response.getId(), test_subject.getId());
	}

}

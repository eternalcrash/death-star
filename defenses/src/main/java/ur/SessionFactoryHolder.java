package ur;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Class to contain the sessionFactory object.
 * There should be only one instance.
 *  <code>closeSessionFactory()<code> method should be called on application shutdown
 */
public class SessionFactoryHolder {
	private static SessionFactory sessionFactory;

	static {
		try {
			sessionFactory= new Configuration().configure().buildSessionFactory();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new ExceptionInInitializerError(t);
		}
	}

	public static void closeSessionFactory() {
		sessionFactory.close();
	}
	
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
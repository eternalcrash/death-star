package ur.model;

import java.util.List;

import org.hibernate.Session;


public class DefenseDAOImpl implements IDefensetDAO{
	private Session session;
	
	/**
	 * @param session this Dao should have a open hiberante Session to work
	 */
	public DefenseDAOImpl(Session session) {
		this.session = session;
	}
	
	@Override
	public List<Defense> list() {
		@SuppressWarnings("unchecked")
		List<Defense> defenses =  (List<Defense>) session.createQuery("FROM ur.model.Defense").list();
		return defenses;
	}

	@Override
	public Defense getDefense(int id) {
		return session.get(Defense.class, id);
	}

	@Override
	public void persistDefense(Defense defense) {
		session.beginTransaction();
		session.saveOrUpdate(defense);
		session.getTransaction().commit();
	}

	@Override
	public void deleteDefense(Defense defense) {
		session.beginTransaction();
		session.delete(defense);
		session.flush();
		session.getTransaction().commit();
	}

}

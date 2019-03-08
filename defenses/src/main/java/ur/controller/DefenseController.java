package ur.controller;

import java.util.List;

import org.hibernate.Session;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import ur.SessionFactoryHolder;
import ur.model.Defense;
import ur.model.DefenseDAOImpl;
import ur.model.DefenseStatus;
import ur.model.MaterialCost;

/**
 * This class manages the construction of the Defenses
 *
 */
public class DefenseController {

	// Endpoint for the materials API , will be read from the env variable
	public static String materials_endpoint = "http://0.0.0.0:8001/materials/";
	static {
		String env_endpoint = System.getenv("MATERIALS_ENDPOINT");
		materials_endpoint = env_endpoint == null ? materials_endpoint : env_endpoint;
	}

	public static List<Defense> list_defenses() {
		try (Session session = SessionFactoryHolder.getSessionFactory().openSession()) {
			DefenseDAOImpl dao = new DefenseDAOImpl(session);
			return dao.list();
		}
	}

	public static Defense getDefense(int id) {
		try (Session session = SessionFactoryHolder.getSessionFactory().openSession()) {
			DefenseDAOImpl dao = new DefenseDAOImpl(session);
			return dao.getDefense(id);
		}
	}

	public static Defense newDefense() {
		Defense d;
		try (Session session = SessionFactoryHolder.getSessionFactory().openSession()) {
			DefenseDAOImpl dao = new DefenseDAOImpl(session);
			d = new Defense();
			dao.persistDefense(d);
		}
		return d;
	}

	/**
	 * This method performs all the necesary operations to check if building is
	 * possible
	 * 
	 * 1- Check the materials in the materials API 2- Check if the target
	 * DefenseStatus is valid
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public static boolean canBuild(int id, DefenseStatus target) {
		Defense d = getDefense(id);
		if (d == null) {
			return false; // invalid defense id
		}
		// 1 - get defined material costs
		List<MaterialCost> costs = d.getStatus().getTargetCost(target);
		if (costs == null) {
			return false; // no valid target if costs are null
		}
		// now consult the materials API to check if we have sufficent materials
		for (MaterialCost cost : costs) {
			try {
				String url = materials_endpoint + (materials_endpoint.endsWith("/") ? "" : "/");
				int saved = Unirest.get(url + cost.getMaterial()).asJson().getBody().getObject().getInt("value");
				if (saved < cost.getValue()) {
					return false;
				}

			} catch (UnirestException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true; // if we get to this point all requirements pass
	}

	/**
	 * This method performs the building (queues the building request in the Empire
	 * work Queue)
	 * 
	 * @param id
	 * @param target
	 * @return
	 */
	public static Defense buildDefense(Defense d, DefenseStatus target) {
		// 1 - get defined material costs
		List<MaterialCost> costs = d.getStatus().getTargetCost(target);
		if (costs == null) {
			return null; // no valid target if costs are null
		}
		// now POST the materials API to spend the materials
		// before that we make a GET to check if there is enough
		// NOTE: this is not very safe for a multiple consumer scenario!
		for (MaterialCost cost : costs) {
			try {
				String url = materials_endpoint + (materials_endpoint.endsWith("/") ? "" : "/");
				int saved = Unirest.get(url + cost.getMaterial()).asJson().getBody().getObject().getInt("value");
				if (saved >= cost.getValue()) {
					int left = saved - cost.getValue();
					Unirest.put(url + cost.getMaterial()).field("value", left).asJson();
				} else {
					return null; // TODO rollback the costs and maybe custom exception
				}

			} catch (UnirestException e) {
				e.printStackTrace();
				return null;
			}
		}
		// queue the job if required
		if(target.buildingQueue()!=null) {
			WorkQueueController.queueBuildJob(target.buildingQueue(), d, target);
		}
		// set the status to the target and update the defense instance
		return updateDefense(d, target);

	}

	private static Defense updateDefense(Defense d, DefenseStatus status) {
		try (Session session = SessionFactoryHolder.getSessionFactory().openSession()) {
			DefenseDAOImpl dao = new DefenseDAOImpl(session);
			d.setStatus(status);
			dao.persistDefense(d);
			return d;
		}
		
	}

	/**
	 * Method to be called by the WorkQueue when the building job is finished
	 * The defense is updated from buildingXXXX to XXXX
	 * @param defense_id
	 * @param target
	 */
	public static void job_complete(int defense_id, DefenseStatus target) {
		Defense d = getDefense(defense_id);
		// get the final status by removing "Building" from the name of the status
		DefenseStatus final_status = DefenseStatus.valueOf(target.toString().split("Building")[1]);
		updateDefense(d, final_status);
	}


}

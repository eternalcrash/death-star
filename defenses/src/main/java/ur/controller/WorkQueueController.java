package ur.controller;

import redis.clients.jedis.Jedis;
import ur.model.Defense;
import ur.model.DefenseStatus;

/**
 * Class to manage work to be done in queues
 * 
 * 
 * this class calls DefenseController to update defenses when work is done
 */
public class WorkQueueController {

	public static final String CANNON_QUEUE = "CannonQueue";
	public static final String DECK_QUEUE = "DeckQueue";

	private static Jedis redis_client;
	static {
		String redis_host = System.getenv("REDIS_HOST");
		redis_client = redis_host == null ? new Jedis("0.0.0.0") : new Jedis(redis_host);
		// TODO clear both queues at startup for easier development
		redis_client.del(CANNON_QUEUE, DECK_QUEUE);
	}

	/**
	 * Method called when 1 time unit has passed see TimeController Rules: - attend
	 * Cannon queue before than Deck queue.
	 * 
	 */
	public static void tick() {
		String[] queue_order = { CANNON_QUEUE, DECK_QUEUE };

		for (String queue : queue_order) {
			String job = redis_client.rpop(queue);
			if (job != null) { // check not empty
				String[] job_split = job.split("#");
				int time = Integer.parseInt(job_split[2]);
				time--;
				if (time <= 0) { // if time reaches 0 , the job is complete
					DefenseController.job_complete(Integer.parseInt(job_split[0]), DefenseStatus.valueOf(job_split[1]));
				} else { // job still goes -> return to the queue
					redis_client.rpush(queue, job_split[0] + "#" + job_split[1] + "#" + time);
				}
				break; // only work in one queue per tick
			}
		}
	}

	/**
	 * Queues the job in the corresponding queue
	 * 
	 * @param queueName
	 * @param d
	 * @param target
	 */
	public static void queueBuildJob(String queueName, Defense d, DefenseStatus target) {
		// queue system are implemented with redis
		// each build queue is managed with redis lists
		// each queued item contains a hash separated value of:
		// id # targetName # time_remaining
		int time_cost = d.getStatus().get_time_cost(target);
		String job = d.getId() + "#" + target.name() + "#" + time_cost;
		redis_client.lpush(queueName, job);
	}

}

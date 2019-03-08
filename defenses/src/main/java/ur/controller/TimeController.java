package ur.controller;

import redis.clients.jedis.Jedis;

/**
 * Class to manage time simulation using redis
 *
 */
public class TimeController {
	private static Jedis redis_client;
	static {
		String redis_host = System.getenv("REDIS_HOST");
		redis_client = redis_host == null ? new Jedis() : new Jedis(redis_host);
		redis_client.set("time", "0"); //set to zero at startup
	}
	
	
	/**
	 * Get the current time (in units passed since start)
	 * @return
	 */
	public static int get_time() {
		return Integer.parseInt(redis_client.get("time"));
		
	}
	
	/**
	 * Make the time advance one unit, also calls the tick() method on the worker
	 */
	public static void tick() {
		redis_client.set("time", Integer.toString(get_time()+1));
		WorkQueueController.tick();
	}

}

package ur;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import ur.controller.TimeController;

/**
 * This class provides a simple endpoint to make the time advance in 1 unit
 * this class also simulates the work in the redis work queues
 *
 */
@Api(tags = { "Time Resource" })
@SwaggerDefinition(tags = { @Tag(name = "Time Resource", description = "Death Star simulation time"), })

@Path("time")
public class TimeSimulationResource {
	

	@ApiOperation("Obtain the current time (in time units)")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public int get() {
		return TimeController.get_time();
	}


	@ApiOperation("Make the time advance one unit")
	@POST
	public void tick() {
		TimeController.tick();
	}


}

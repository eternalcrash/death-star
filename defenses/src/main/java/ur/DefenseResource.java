package ur;

import java.util.List;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import ur.controller.DefenseController;
import ur.model.Defense;
import ur.model.DefenseStatus;

@Api(tags = { "Defense Resource" })
@SwaggerDefinition(tags = { @Tag(name = "Defense Resource", description = "Death Star defense building system"), })

@Path("defense")
public class DefenseResource {

	@ApiOperation("Obtain the list of Defense quadrants with their building status")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Defense> list() {
		return DefenseController.list_defenses();
	}

	@ApiOperation("get (by its id) a Defense quadrants with their building status")
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Defense getDefense(@PathParam("id") int id) {
		return DefenseController.getDefense(id);
	}

	@ApiOperation("create a new empty defense quadrant")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Defense newDefense() {
		return DefenseController.newDefense();
	}

	@ApiOperation("build a new (valid) building in the given defense quadrant id")
	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Defense updateDefense(@PathParam("id") int id, @QueryParam("status") DefenseStatus status) {
		Defense d = DefenseController.getDefense(id);
		if(d == null) {
			throw new NotFoundException();
		}
		if (DefenseController.canBuild(id, status)) {
			return DefenseController.buildDefense(d, status);
		}
		throw new ForbiddenException("That build target is not valid");

	}

}

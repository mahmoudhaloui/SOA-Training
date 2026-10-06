package webservices;

import entities.Module;
import metiers.ModuleBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/modules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModuleRestAPI {

    // static: the business class rebuilds its list in the constructor,
    // and JAX-RS creates a new resource object for every request.
    private static ModuleBusiness business = new ModuleBusiness();

    // GET /modules            -> all modules
    // GET /modules?type=TRANSVERSAL -> filtered by type
    @GET
    public Response getModules(@QueryParam("type") Module.TypeModule type) {
        List<Module> result = (type == null)
                ? business.getAllModules()
                : business.getModulesByType(type);
        return Response.status(Response.Status.OK).entity(result).build();
    }

    // GET /modules/{matricule}
    @GET
    @Path("/{matricule}")
    public Response getModule(@PathParam("matricule") String matricule) {
        Module module = business.getModuleByMatricule(matricule);
        if (module == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(module).build();
    }

    // POST /modules
    @POST
    public Response addModule(Module module) {
        // addModule() reads module.getUniteEnseignement().getCode(), so it must exist
        if (module == null || module.getUniteEnseignement() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("uniteEnseignement manquante").build();
        }
        if (business.getModuleByMatricule(module.getMatricule()) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("matricule deja utilise").build();
        }
        if (business.addModule(module)) {
            return Response.status(Response.Status.CREATED).entity(module).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("UE introuvable").build();
    }

    // PUT /modules/{matricule}
    @PUT
    @Path("/{matricule}")
    public Response updateModule(@PathParam("matricule") String matricule, Module module) {
        if (business.updateModule(matricule, module)) {
            return Response.status(Response.Status.OK).entity(module).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // DELETE /modules/{matricule}
    @DELETE
    @Path("/{matricule}")
    public Response deleteModule(@PathParam("matricule") String matricule) {
        if (business.deleteModule(matricule)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

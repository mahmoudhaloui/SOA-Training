package webservices;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/module")
public class ModuleRestAPI{

    static ModuleBusiness helper = new ModuleBusiness();

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListModules() {
        return Response.status(200).entity(helper.getAllModules()).build();
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addModule(Module module) {
        if (module.getUniteEnseignement() != null && helper.addModule(module)) {
            return Response.status(201).entity("succes").build();
        } else {
            return Response.status(400).entity("erreur").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{matricule}")
    public Response getModuleByMatricule(@PathParam("matricule") String matricule) {
        return Response.status(200).entity(helper.getModuleByMatricule(matricule)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getModulesByType(@QueryParam("type") Module.TypeModule type) {
        return Response.status(200).entity(helper.getModulesByType(type)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ue/{code}")
    public Response getModulesByUE(@PathParam("code") int code) {
        UniteEnseignement ue = UniteEnsRestAPI.helper.getUEByCode(code);

        return Response.status(200).entity(helper.getModulesByUE(ue)).build();
    }

    @DELETE
    @Path("/{matricule}")
    public Response deleteModule(@PathParam("matricule") String matricule) {
        return Response.status(200).entity(helper.deleteModule(matricule)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{matricule}")
    public Response updateModule(@PathParam("matricule") String matricule, Module updatedModule) {
        return Response.status(200).entity(helper.updateModule(matricule, updatedModule)).build();
    }
}
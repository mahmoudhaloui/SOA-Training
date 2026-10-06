package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UniteEnsRestAPI {

    // static: the business constructor resets the list, so keep a single instance
    private static UniteEnseignementBusiness business = new UniteEnseignementBusiness();

    // GET /ues                    -> all UEs
    // GET /ues?semestre=1         -> filtered by semester
    // GET /ues?domaine=Informatique -> filtered by domain
    @GET
    public Response getUEs(@QueryParam("semestre") Integer semestre,
                           @QueryParam("domaine") String domaine) {
        List<UniteEnseignement> result;
        if (semestre != null) {
            result = business.getUEBySemestre(semestre);
        } else if (domaine != null) {
            result = business.getUEByDomaine(domaine);
        } else {
            result = business.getListeUE();
        }
        return Response.status(Response.Status.OK).entity(result).build();
    }

    // GET /ues/{code}
    @GET
    @Path("/{code}")
    public Response getUE(@PathParam("code") int code) {
        UniteEnseignement ue = business.getUEByCode(code);
        if (ue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(ue).build();
    }

    // POST /ues
    @POST
    public Response addUE(UniteEnseignement ue) {
        if (ue == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (business.getUEByCode(ue.getCode()) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("code deja utilise").build();
        }
        business.addUniteEnseignement(ue);
        return Response.status(Response.Status.CREATED).entity(ue).build();
    }

    // PUT /ues/{code}
    @PUT
    @Path("/{code}")
    public Response updateUE(@PathParam("code") int code, UniteEnseignement ue) {
        if (business.updateUniteEnseignement(code, ue)) {
            return Response.status(Response.Status.OK).entity(ue).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // DELETE /ues/{code}
    @DELETE
    @Path("/{code}")
    public Response deleteUE(@PathParam("code") int code) {
        if (business.deleteUniteEnseignement(code)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

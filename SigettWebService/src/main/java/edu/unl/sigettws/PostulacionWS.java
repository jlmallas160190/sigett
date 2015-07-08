/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigettws;

import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.service.ProyectoCarreraOfertaService;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jorge-luis
 */
@Path("/postulacionws/")
@DeclareRoles({"USER", "ADMIN"})
@Stateless
public class PostulacionWS {

    @EJB(lookup = "java:global/SigettService/ProyectoCarreraOfertaServiceImplement!edu.unl.sigett.service.ProyectoCarreraOfertaService")
    private ProyectoCarreraOfertaService proyectoCarreraOfertaService;

    @GET
    @Path("/proyectosCarreraOfertaWS/{carreraId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ProyectoCarreraOferta> sigettWSProyectosCarreraOferta(@PathParam("carreraId") String carreraId) {
        ProyectoCarreraOferta proyectoCarreraOfertaBuscar = new ProyectoCarreraOferta();
        proyectoCarreraOfertaBuscar.setCarreraId(Integer.parseInt(carreraId));
        List<ProyectoCarreraOferta> proyectoCarreraOfertas = proyectoCarreraOfertaService.buscar(proyectoCarreraOfertaBuscar);
        return proyectoCarreraOfertas;
    }
}

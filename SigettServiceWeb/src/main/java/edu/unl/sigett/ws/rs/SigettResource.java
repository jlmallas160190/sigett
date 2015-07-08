/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.ws.rs;

import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.service.ProyectoCarreraOfertaService;
import java.util.ArrayList;
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
@Path("/sigettws/")
@DeclareRoles({"USER", "ADMIN"})
@Stateless
public class SigettResource {

    @EJB(lookup = "java:global/SigettService/ProyectoCarreraOfertaServiceImplement!edu.unl.sigett.service.ProyectoCarreraOfertaService")
    private ProyectoCarreraOfertaService proyectoCarreraOfertaService;

    @GET
    @Path("/proyectos_carrera_oferta/{carreraId}/{ofertaId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ProyectoCarreraOferta> sigettWSProyectosCarreraOferta(@PathParam("carreraId") String carreraId, @PathParam("ofertaId") String ofertaId) {
        ProyectoCarreraOferta proyectoCarreraOfertaBuscar = new ProyectoCarreraOferta();
        proyectoCarreraOfertaBuscar.setCarreraId(Integer.parseInt(carreraId));
        proyectoCarreraOfertaBuscar.setOfertaAcademicaId(Long.parseLong(ofertaId));
        List<ProyectoCarreraOferta> proyectoCarreraOfertas = proyectoCarreraOfertaService.buscar(proyectoCarreraOfertaBuscar);
        List<ProyectoCarreraOferta> lista=new ArrayList<>();
        for(ProyectoCarreraOferta pc:proyectoCarreraOfertas){
           pc.getProyectoId().setProyectoCarreraOfertaList(null);
           pc.getProyectoId().setConfiguracionProyectoList(null);
           pc.getProyectoId().getCronograma().setProyecto(null);
           lista.add(pc);
        }
        return lista;
    }
}

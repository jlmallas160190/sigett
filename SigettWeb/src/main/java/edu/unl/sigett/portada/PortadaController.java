/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.portada;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.service.ProyectoService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.webSemantica.dto.ProyectoOntDTO;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "portadaController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "consultarProyecto",
            pattern = "/consultaProyecto/",
            viewId = "/faces/pages/sigett/consulta/consultaProyecto.xhtml"
    ),
    @URLMapping(
            id = "portada",
            pattern = "/portada/",
            viewId = "/faces/portada.xhtml"
    )
})
public class PortadaController implements Serializable {

    @Inject
    private SessionPortada sessionPortada;
    @Inject
    private CabeceraController cabeceraController;
    @EJB
    private ProyectoService proyectoService;

    public PortadaController() {
    }

    /**
     * BUSCAR SEMANTICO PARA LOS PROYECTOS.
     */
    public void buscarProyectos() {
        cabeceraController.getOntologyService().getProyectoOntService().read(cabeceraController.getCabeceraWebSemantica());
        ProyectoOntDTO proyectoOntDTOBuscar = new ProyectoOntDTO();
        proyectoOntDTOBuscar.setAutor(sessionPortada.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setCarrera(sessionPortada.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setDocente(sessionPortada.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setEstado(sessionPortada.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setLineaInvestigacion(sessionPortada.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setNivelAcademico(sessionPortada.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setTema(sessionPortada.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setTipo(sessionPortada.getFiltroBuscadorSemantico());
        List<ProyectoOntDTO> proyectoOntDTOs = cabeceraController.getOntologyService().getProyectoOntService().buscar(proyectoOntDTOBuscar);
        for (ProyectoOntDTO proyectoOntDTO : proyectoOntDTOs) {
            if (!contieneProyecto(proyectoOntDTO)) {
                sessionPortada.getProyectosOntology().add(proyectoOntDTO);
            }
        }
    }

    private Boolean contieneProyecto(ProyectoOntDTO proyectoOntDTO) {
        for (ProyectoOntDTO p : sessionPortada.getProyectosOntology()) {
            if (p.getId().equals(proyectoOntDTO.getId())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public String editarProyecto(ProyectoOntDTO proyectoOntDTO) {
        Proyecto proyectoBuscar = new Proyecto();
        proyectoBuscar.setId(proyectoOntDTO.getId());
        sessionPortada.setProyectoSeleccionado(proyectoService.buscarPorId(new Proyecto(proyectoBuscar.getId())));
        return "pretty:consultarProyecto";
    }

    public List<ProyectoOntDTO> completarProyectosOnt(final String query) {
        List<ProyectoOntDTO> results = new ArrayList<>();
        if (!"".equals(query.trim())) {
            sessionPortada.setFiltroBuscadorSemantico(query);
            buscarProyectos();
            results.addAll(sessionPortada.getProyectosOntology());
            ProyectoOntDTOConverter.setProyectoOntDTOs(results);
        }
        return results;
    }

}

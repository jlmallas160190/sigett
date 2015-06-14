/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.autorProyecto;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.unl.sigett.entity.Aspirante;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.estudianteUsuario.SessionEstudianteUsuario;
import edu.unl.sigett.service.AspiranteService;
import edu.unl.sigett.service.AutorProyectoService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "autorProyectoController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "proyectos",
            pattern = "/proyectos/",
            viewId = "/faces/pages/sigett/autorProyecto/index.xhtml"
    ),
    @URLMapping(
            id = "editarAutorProyecto",
            pattern = "/autorProyecto/#{sessionAutorProyecto.autorProyectoDTO.autorProyecto.id}",
            viewId = "/faces/pages/sigett/autorProyecto/editarProyectoAutor.xhtml"
    )
})
public class AutorProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionAutorProyecto sessionAutorProyecto;
    @Inject
    private SessionEstudianteUsuario sessionEstudianteUsuario;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private AutorProyectoService autorProyectoService;
    @EJB
    private PersonaService personaService;
    @EJB
    private EstudianteCarreraService estudianteCarreraService;
    @EJB
    private AspiranteService aspiranteService;

    //</editor-fold>
    public AutorProyectoController() {
    }
    public void preRenderView() {
        listadoProyectos();
    }
    public String editar(AutorProyectoDTO autorProyectoDTO) {
        sessionAutorProyecto.setAutorProyectoDTO(autorProyectoDTO);
        return "pretty:editarAutorProyecto";
    }
    
    private void listadoProyectos() {
        sessionAutorProyecto.getAutorProyectos().clear();
        sessionAutorProyecto.getFilterAutorProyectos().clear();
        List<EstudianteCarrera> estudianteCarreras = estudianteCarreraService.buscar(
                new EstudianteCarrera(null, sessionEstudianteUsuario.getEstudianteUsuarioDTO().getEstudiante(), Boolean.TRUE, null));
        if (estudianteCarreras == null) {
            return;
        }
        for (EstudianteCarrera estudianteCarrera : estudianteCarreras) {
            Aspirante aspirante = aspiranteService.buscarPorId(estudianteCarrera.getId());
            List<AutorProyecto> autorProyectos = autorProyectoService.buscar(new AutorProyecto(null, aspirante != null ? aspirante : null,
                    null, null, null));
            if (autorProyectos == null) {
                continue;
            }
            for (AutorProyecto autorProyecto : autorProyectos) {
                AutorProyectoDTO autorProyectoDTO = new AutorProyectoDTO(autorProyecto, aspirante, estudianteCarrera,
                        personaService.buscarPorId(new Persona(estudianteCarrera.getEstudianteId().getId())));
                sessionAutorProyecto.getAutorProyectos().add(autorProyectoDTO);
            }
        }
        sessionAutorProyecto.setFilterAutorProyectos(sessionAutorProyecto.getAutorProyectos());
    }
}

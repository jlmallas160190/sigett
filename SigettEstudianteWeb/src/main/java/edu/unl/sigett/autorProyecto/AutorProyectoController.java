/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.autorProyecto;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.unl.sigett.director.DirectorDTO;
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import edu.unl.sigett.entity.Aspirante;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.enumeration.EstadoDirectorEnum;
import edu.unl.sigett.estudianteUsuario.SessionEstudianteUsuario;
import edu.unl.sigett.service.AspiranteService;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.DirectorProyectoService;
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
            pattern = "/autorProyecto/",
            viewId = "/faces/pages/sigett/autorProyecto/editarAutorProyecto.xhtml"
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
    @EJB(lookup = "java:global/SigettService/AutorProyectoServiceImplement!edu.unl.sigett.service.AutorProyectoService")
    private AutorProyectoService autorProyectoService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/AcademicoService/EstudianteCarreraServiceImplement!edu.jlmallas.academico.service.EstudianteCarreraService")
    private EstudianteCarreraService estudianteCarreraService;
    @EJB(lookup = "java:global/SigettService/AspiranteServiceImplement!edu.unl.sigett.service.AspiranteService")
    private AspiranteService aspiranteService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/SigettService/DirectorProyectoServiceImplement!edu.unl.sigett.service.DirectorProyectoService")
    private DirectorProyectoService directorProyectoService;
    @EJB(lookup = "java:global/AcademicoService/DocenteCarreraServiceImplement!edu.jlmallas.academico.service.DocenteCarreraService")
    private DocenteCarreraService docenteCarreraService;

    //</editor-fold>
    public AutorProyectoController() {
    }

    public void preRenderView() {
        listadoProyectos();
        listadoCategorias();
        listadoEstados();
        listadoTipos();
    }

    public String editar(AutorProyectoDTO autorProyectoDTO) {
        sessionAutorProyecto.setAutorProyectoDTO(autorProyectoDTO);
        this.listadoDirectores();
        return "pretty:editarAutorProyecto";
    }

    private void listadoTipos() {
        sessionAutorProyecto.getTipos().clear();
        sessionAutorProyecto.setTipos(itemService.buscarPorCatalogo(CatalogoEnum.TIPOPROYECTO.getTipo()));
    }

    private void listadoCategorias() {
        sessionAutorProyecto.getCategorias().clear();
        sessionAutorProyecto.setCategorias(itemService.buscarPorCatalogo(CatalogoEnum.CATALOGOPROYECTO.getTipo()));
    }

    private void listadoEstados() {
        sessionAutorProyecto.getEstados().clear();
        sessionAutorProyecto.setEstados(itemService.buscarPorCatalogo(CatalogoEnum.ESTADOPROYECTO.getTipo()));
    }

    private void listadoProyectos() {
        sessionAutorProyecto.getAutorProyectos().clear();
        sessionAutorProyecto.getFilterAutorProyectos().clear();
        List<EstudianteCarrera> estudianteCarreras = estudianteCarreraService.buscar(
                new EstudianteCarrera(null, sessionEstudianteUsuario.getEstudianteUsuarioDTO().getEstudiante(), null, Boolean.TRUE, null));
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
                autorProyecto.getProyectoId().setEstado(itemService.buscarPorId(autorProyecto.getProyectoId().getEstadoProyectoId()).getNombre());
                autorProyecto.getProyectoId().setTipo(itemService.buscarPorId(autorProyecto.getProyectoId().getTipoProyectoId()).getNombre());
                autorProyecto.getProyectoId().setCatalogo(itemService.buscarPorId(autorProyecto.getProyectoId().getCatalogoProyectoId()).getNombre());
                AutorProyectoDTO autorProyectoDTO = new AutorProyectoDTO(autorProyecto, aspirante, estudianteCarrera,
                        personaService.buscarPorId(new Persona(estudianteCarrera.getEstudianteId().getId())));
                sessionAutorProyecto.getAutorProyectos().add(autorProyectoDTO);
            }
        }
        sessionAutorProyecto.setFilterAutorProyectos(sessionAutorProyecto.getAutorProyectos());
    }

    private void listadoDirectores() {
        sessionAutorProyecto.getDirectoresProyectoDTO().clear();
        List<DirectorProyecto> directorProyectos = directorProyectoService.buscar(
                new DirectorProyecto(null, null, null, sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId(), null, null));
        Item estadoDirector = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADODIRECTOR.getTipo(), EstadoDirectorEnum.RENUNCIADO.getTipo());
        for (DirectorProyecto directorProyecto : directorProyectos) {
            if (directorProyecto.getEstadoDirectorId().equals(estadoDirector.getId())) {
                continue;
            }
            DirectorProyectoDTO directorProyectoDTO = new DirectorProyectoDTO(directorProyecto, new DirectorDTO(
                    directorProyecto.getDirectorId(), docenteCarreraService.buscarPorId(new DocenteCarrera(
                                    directorProyecto.getDirectorId().getId())), null));
            directorProyectoDTO.getDirectorDTO().setPersona(personaService.buscarPorId(
                    new Persona(directorProyectoDTO.getDirectorDTO().getDocenteCarrera().getDocenteId().getId())));
            sessionAutorProyecto.getDirectoresProyectoDTO().add(directorProyectoDTO);
        }
    }
}

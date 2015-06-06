/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.DocenteProyectoService;
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
@Named(value = "docenteProyectoController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "docenteProyectos",
            pattern = "/docenteProyectos",
            viewId = "/faces/pages/docenteProyecto/index.xhtml"
    ),
    @URLMapping(
            id = "editarDocenteProyecto",
            pattern = "/editarDocenteProyecto",
            viewId = "/faces/pages/docenteProyecto/editarDocenteProyecto.xhtml"
    )
})
public class DocenteProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    @Inject
    private DocenteProyectoDM docenteProyectoDM;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private DocenteProyectoService docenteProyectoService;
    @EJB
    private PersonaDao personaDao;
    @EJB
    private ItemService itemService;
    @EJB
    private AutorProyectoService autorProyectoService;
    @EJB
    private EstudianteCarreraDao estudianteCarreraDao;
//</editor-fold>

    public DocenteProyectoController() {
    }
    
    public void preRenderView() {
        this.listadoPertinenciaProyecto();
    }
    
    public String editar(final DocenteProyectoDTO docenteProyectoDTO) {
        docenteProyectoDM.setDocenteProyectoDTOSeleccionado(docenteProyectoDTO);
        return "pretty:editarDocenteProyecto";
    }

    /**
     * LISTADO DE PROYECTO ASIGNADOS A DOCENTE PARA QUE POSTERIORMENTE DE
     * PERTINENCIA.
     */
    private void listadoPertinenciaProyecto() {
        docenteProyectoDM.getDocentesProyectoDTO().clear();
        docenteProyectoDM.getFilterDocentesProyectoDTO().clear();
        Item estadoProyecto=itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.INICIO.getTipo());
        List<DocenteProyecto> docenteProyectos = this.docenteProyectoService.buscar(new DocenteProyecto(
                null, null, docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId(), Boolean.TRUE,estadoProyecto.getId()));
        if (docenteProyectos == null) {
            return;
        }
        for (DocenteProyecto docenteProyecto : docenteProyectos) {
            Item estado = itemService.buscarPorId(docenteProyecto.getProyectoId().getEstadoProyectoId());
            Item tipo = itemService.buscarPorId(docenteProyecto.getProyectoId().getTipoProyectoId());
            Item categoria = itemService.buscarPorId(docenteProyecto.getProyectoId().getCatalogoProyectoId());
            docenteProyecto.getProyectoId().setEstado(estado.getNombre());
            docenteProyecto.getProyectoId().setTipo(tipo.getNombre());
            docenteProyecto.getProyectoId().setCatalogo(categoria.getNombre());
            docenteProyecto.getProyectoId().setAutores(autores(docenteProyecto.getProyectoId()));
            DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, personaDao.find(docenteProyecto.getDocenteId()));
            docenteProyectoDM.getDocentesProyectoDTO().add(docenteProyectoDTO);
        }
        docenteProyectoDM.setFilterDocentesProyectoDTO(docenteProyectoDM.getDocentesProyectoDTO());
    }
    private void historialPertinenciaProyecto() {
        docenteProyectoDM.getHistorialDocenteProyectosDTO().clear();
        docenteProyectoDM.getFilterHistorialDocenteProyectosDTO().clear();
        Item estadoProyecto=itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.INICIO.getTipo());
        List<DocenteProyecto> docenteProyectos = this.docenteProyectoService.buscar(new DocenteProyecto(
                null, null, docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId(), Boolean.TRUE,estadoProyecto.getId()));
        if (docenteProyectos == null) {
            return;
        }
        for (DocenteProyecto docenteProyecto : docenteProyectos) {
            Item estado = itemService.buscarPorId(docenteProyecto.getProyectoId().getEstadoProyectoId());
            Item tipo = itemService.buscarPorId(docenteProyecto.getProyectoId().getTipoProyectoId());
            Item categoria = itemService.buscarPorId(docenteProyecto.getProyectoId().getCatalogoProyectoId());
            docenteProyecto.getProyectoId().setEstado(estado.getNombre());
            docenteProyecto.getProyectoId().setTipo(tipo.getNombre());
            docenteProyecto.getProyectoId().setCatalogo(categoria.getNombre());
            docenteProyecto.getProyectoId().setAutores(autores(docenteProyecto.getProyectoId()));
            DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, personaDao.find(docenteProyecto.getDocenteId()));
            docenteProyectoDM.getDocentesProyectoDTO().add(docenteProyectoDTO);
        }
        docenteProyectoDM.setFilterDocentesProyectoDTO(docenteProyectoDM.getDocentesProyectoDTO());
    }

    /**
     *
     * @param proyecto
     * @return
     */
    private String autores(Proyecto proyecto) {
        String resultado = "";
        List<AutorProyecto> autorProyectos = autorProyectoService.buscar(new AutorProyecto(proyecto, null,
                itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo()).getId(), null, null));
        if (autorProyectos == null) {
            return "";
        }
        int contador = 0;
        for (AutorProyecto autorProyecto : autorProyectos) {
            EstudianteCarrera estudianteCarrera = estudianteCarreraDao.find(autorProyecto.getAspiranteId().getId());
            Persona persona = personaDao.find(estudianteCarrera.getEstudianteId().getId());
            if (contador == 0) {
                if (persona == null) {
                    continue;
                }
                resultado = (persona.getApellidos() + " " + persona.getNombres());
            } else {
                resultado = (resultado + ", " + persona.getApellidos() + " " + persona.getNombres());
            }
            contador++;
        }
        return resultado;
    }
}

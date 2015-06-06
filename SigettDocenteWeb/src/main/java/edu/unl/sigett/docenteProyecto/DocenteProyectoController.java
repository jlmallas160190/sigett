/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.service.ItemService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.entity.DocenteProyecto;
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
        List<DocenteProyecto> docenteProyectos = this.docenteProyectoService.buscar(new DocenteProyecto(
                null, null, docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId(), Boolean.TRUE));
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
            DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, personaDao.find(docenteProyecto.getDocenteId()));
            docenteProyectoDM.getDocentesProyectoDTO().add(docenteProyectoDTO);
        }
        docenteProyectoDM.setFilterDocentesProyectoDTO(docenteProyectoDM.getDocentesProyectoDTO());
    }
}

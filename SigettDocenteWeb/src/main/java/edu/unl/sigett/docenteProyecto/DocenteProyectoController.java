/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.CoordinadorPeriodoService;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.DocenteService;
import edu.unl.sigett.academico.coordinadorPeriodo.CoordinadorPeriodoDTO;
import edu.unl.sigett.autorProyecto.AutorProyectoDTO;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.documentoProyecto.DocumentoProyectoDTO;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.enumeration.CatalogoDocumentoProyectoEnum;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.DocenteProyectoService;
import edu.unl.sigett.service.DocumentoProyectoService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
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
    @EJB
    private DocenteCarreraService docenteCarreraService;
    @EJB
    private CoordinadorPeriodoService coordinadorPeriodoService;
    @EJB
    private DocenteService docenteService;
    @EJB
    private DocumentoProyectoService documentoProyectoService;
    @EJB
    private DocumentoService documentoService;
//</editor-fold>
    private static final Logger LOG = Logger.getLogger(DocenteProyectoController.class.getName());

    public DocenteProyectoController() {
    }

    public void preRenderView() {
        this.listadoPertinenciaProyecto();
    }

    public void preRenderViewEditar() {
        this.recuperaEstadoActualProyecto();
    }

    private void iniciar() {
        this.docenteProyectoDM.getAutorProyectos().clear();
        this.docenteProyectoDM.getFilterAutorProyectos().clear();
        this.docenteProyectoDM.getDocumentoProyectos().clear();
        this.docenteProyectoDM.getFilterDocumentoProyectos().clear();
        this.docenteProyectoDM.getLineasInvestigacionProyecto().clear();
        this.docenteProyectoDM.getFilterLineasInvestigacionProyecto().clear();
    }

    public String editar(final DocenteProyectoDTO docenteProyectoDTO) {
        iniciar();
        docenteProyectoDM.setDocenteProyectoDTOSeleccionado(docenteProyectoDTO);
        List<CoordinadorPeriodo> coordinadores = coordinadorPeriodoService.buscar(new CoordinadorPeriodo(Boolean.TRUE, null, null,
                docenteProyectoDTO.getDocenteCarrera().getCarreraId()));
        if (coordinadores == null) {
            return "";
        }
        CoordinadorPeriodo coordinadorPeriodo = !coordinadores.isEmpty() ? coordinadores.get(0) : null;
        CoordinadorPeriodoDTO coordinadorPeriodoDTO = new CoordinadorPeriodoDTO(
                coordinadorPeriodo, personaDao.find(coordinadorPeriodo.getCoordinadorId().getId()), null);
        coordinadorPeriodoDTO.setDocente(docenteService.buscarPorId(new Docente(coordinadorPeriodoDTO.getPersona().getId())));
        docenteProyectoDM.setCoordinadorPeriodoDTO(coordinadorPeriodoDTO);
        listadoDocumentos();
        listadoAutores();
        return "pretty:editarDocenteProyecto";
    }

    /**
     * LISTAR ANTEPROYECTO
     */
    private void listadoDocumentos() {
        try {
            docenteProyectoDM.getDocumentoProyectos().clear();
            docenteProyectoDM.getFilterDocumentoProyectos().clear();
            Item catalogoDocumento = itemService.buscarPorCatalogoCodigo(
                    CatalogoEnum.CATALOGODOCUMENTOPROYECTO.getTipo(), CatalogoDocumentoProyectoEnum.ANTEPROYECTO.getTipo());

            List<DocumentoProyecto> documentoProyectos = this.documentoProyectoService.buscar(
                    new DocumentoProyecto(Boolean.TRUE, null, docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId()));
            for (DocumentoProyecto documentoProyecto : documentoProyectos) {
                DocumentoProyectoDTO documentoProyectoDTO = new DocumentoProyectoDTO(
                        documentoProyecto, documentoService.buscarPorId(new Documento(
                                        documentoProyecto.getDocumentoId(), null, catalogoDocumento.getId(), null, null, null, null, null)));
                documentoProyectoDTO.getDocumento().setCatalogo(itemService.buscarPorId(documentoProyectoDTO.getDocumento().getCatalogoId()).getNombre());
                docenteProyectoDM.getDocumentoProyectos().add(documentoProyectoDTO);
            }
            docenteProyectoDM.setFilterDocumentoProyectos(docenteProyectoDM.getDocumentoProyectos());
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    private void listadoAutores() {
        docenteProyectoDM.getAutorProyectos().clear();
        docenteProyectoDM.getFilterAutorProyectos().clear();
        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo());
        for (AutorProyecto autorProyecto : docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getAutorProyectoList()) {
            if (autorProyecto.getEstadoAutorId().equals(estadoRenunciado.getId())) {
                continue;
            }
            AutorProyectoDTO autorProyectoDTO = new AutorProyectoDTO(autorProyecto, autorProyecto.getAspiranteId(),
                    estudianteCarreraDao.find(autorProyecto.getAspiranteId().getId()), null);
            autorProyectoDTO.setPersona(personaDao.find(autorProyectoDTO.getEstudianteCarrera().getEstudianteId().getId()));
            if (!docenteProyectoDM.getAutorProyectos().contains(autorProyectoDTO)) {
                docenteProyectoDM.getAutorProyectos().add(autorProyectoDTO);
            }
        }
        docenteProyectoDM.setFilterAutorProyectos(docenteProyectoDM.getAutorProyectos());
    }

    private void recuperaEstadoActualProyecto() {
        this.docenteProyectoDM.setEstadoActualProyecto(itemService.buscarPorId(
                docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getEstadoProyectoId()));
    }

    /**
     * LISTADO DE PROYECTO ASIGNADOS A DOCENTE PARA QUE POSTERIORMENTE DE
     * PERTINENCIA.
     */
    private void listadoPertinenciaProyecto() {
        docenteProyectoDM.getDocentesProyectoDTO().clear();
        docenteProyectoDM.getFilterDocentesProyectoDTO().clear();
        Item estadoProyecto = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.INICIO.getTipo());
        List<DocenteProyecto> docenteProyectos = this.docenteProyectoService.buscar(new DocenteProyecto(
                null, null, docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId(), Boolean.TRUE, estadoProyecto.getId()));
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
            DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, personaDao.find(docenteProyecto.getDocenteCarreraId()),
                    docenteCarreraService.buscarPorId(new DocenteCarrera(docenteProyecto.getDocenteCarreraId(), null, null, null)));
            docenteProyectoDM.getDocentesProyectoDTO().add(docenteProyectoDTO);
        }
        docenteProyectoDM.setFilterDocentesProyectoDTO(docenteProyectoDM.getDocentesProyectoDTO());
    }

    /**
     * HISTORIAL DE PROYECTOS ASIGNADOS A DOCENTE PARA SU POSTERIOR PERTINENCIA
     */
    private void historialPertinenciaProyecto() {
        docenteProyectoDM.getHistorialDocenteProyectosDTO().clear();
        docenteProyectoDM.getFilterHistorialDocenteProyectosDTO().clear();
        List<DocenteProyecto> docenteProyectos = this.docenteProyectoService.buscar(new DocenteProyecto(
                null, null, docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId(), Boolean.TRUE, null));
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
            DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, personaDao.find(docenteProyecto.getDocenteCarreraId()),
                    docenteCarreraService.buscarPorId(new DocenteCarrera(docenteProyecto.getDocenteCarreraId(), null, null, null)));
            docenteProyectoDM.getHistorialDocenteProyectosDTO().add(docenteProyectoDTO);
        }
        docenteProyectoDM.setFilterHistorialDocenteProyectosDTO(docenteProyectoDM.getHistorialDocenteProyectosDTO());
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

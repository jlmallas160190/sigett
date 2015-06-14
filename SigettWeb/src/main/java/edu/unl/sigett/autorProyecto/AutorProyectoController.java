/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.autorProyecto;

import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.academico.dto.EstudianteCarreraDTO;
import edu.unl.sigett.autorProyecto.AspiranteDTO;
import edu.unl.sigett.entity.Aspirante;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.enumeration.TipoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.AspiranteService;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "autorProyectoController")
@SessionScoped
public class AutorProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionAutorProyecto sessionAutorProyecto;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private CabeceraController cabeceraController;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private AutorProyectoService autorProyectoService;
    @EJB
    private EstudianteCarreraDao estudianteCarreraDao;
    @EJB
    private PersonaDao personaDao;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private ItemService itemService;
    @EJB
    private AspiranteService aspiranteService;
    //</editor-fold>

    public AutorProyectoController() {
    }

    public void preRenderView() {
        this.renderedBuscarAspirantes();
        this.renderedSeleccionar();
        this.buscarAspirantes();
    }
    //<editor-fold defaultstate="collapsed" desc="CRUD">

    /**
     * QUITAR AUTOR DE UN PROYECTO
     *
     * @param autorProyectoDTO
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public void remover(AutorProyectoDTO autorProyectoDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            if (autorProyectoDTO.getAutorProyecto().getId() != null) {
                Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo());
                autorProyectoDTO.getAutorProyecto().setEstadoAutorId(estadoRenunciado.getId());
                sessionProyecto.getAutoresProyectoDTO().remove(autorProyectoDTO);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar") + ". ", "");
                return;
            }
            sessionProyecto.getAutoresProyectoDTO().remove(autorProyectoDTO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * BUSCAR ASPIRANTES APTOS PARA ASIGNARLO COMO AUTOR DE TRABAJO DE
     * TITULACION
     */
    public void buscarAspirantes() {
        sessionAutorProyecto.getAspirantesDTO().clear();
        sessionAutorProyecto.getFilterAspirantesDTO().clear();
        try {
            for (EstudianteCarreraDTO estudianteCarreraDTO : sessionUsuarioCarrera.getEstudiantesCarreraDTO()) {
                Item item = itemService.buscarPorId(estudianteCarreraDTO.getEstudianteCarrera().getEstadoId());
                estudianteCarreraDTO.getEstudianteCarrera().setEstado(item.getNombre());
                List<Aspirante> aspirantes = aspiranteService.buscar(new Aspirante(estudianteCarreraDTO.getEstudianteCarrera().getId(), null));
                if (aspirantes == null) {
                    continue;
                }
                if (aspirantes.isEmpty()) {
                    continue;
                }
                AspiranteDTO aspiranteDTO = new AspiranteDTO(aspirantes.get(0),
                        estudianteCarreraDTO.getEstudianteCarrera(), personaDao.find(
                                estudianteCarreraDTO.getEstudianteCarrera().getEstudianteId().getId()));
                sessionAutorProyecto.getAspirantesDTO().add(aspiranteDTO);
            }
            sessionAutorProyecto.setFilterAspirantesDTO(sessionAutorProyecto.getAspirantesDTO());
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * PERMITE AUTOCOMPLETAR ASPIRANTES APTOS PARA DESARROLLO DE TRABAJOS DE
     * TITULACION
     *
     * @param query
     * @return
     */
    public List<AspiranteDTO> completarAspirantes(final String query) {
        List<AspiranteDTO> results = new ArrayList<>();
        if (!"".equals(query.trim())) {
            for (AspiranteDTO aspiranteDTO : sessionAutorProyecto.getAspirantesDTO()) {
                if (query.trim().toLowerCase().contains(aspiranteDTO.getPersona().getApellidos().toLowerCase())
                        || query.trim().toLowerCase().contains(aspiranteDTO.getPersona().getNombres().toLowerCase())
                        || query.trim().toLowerCase().contains(aspiranteDTO.getPersona().getNumeroIdentificacion().toLowerCase())) {
                    results.add(aspiranteDTO);
                }
            }
        }
        AspiranteDTOConverter.setAspirantesDTO(results);
        return results;
    }

    /**
     * AGREGAR ASPIRANTE COMO AUTOR DE PROYECTO SELECCIONADO
     *
     * @param aspiranteDTO
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public void agregar(AspiranteDTO aspiranteDTO) {
        Calendar fecha = Calendar.getInstance();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "select_autor_proyecto");
            if (tienePermiso != 1) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                return;
            }
            if (aspiranteDTO.getAspirante().getId() != null) {
                aspiranteDTO.setAspirante(aspiranteService.buscarPorId(aspiranteDTO.getAspirante().getId()));
            }
            if (sessionProyecto.getTipoSeleccionado() == null) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " "
                        + bundle.getString("lbl.tipoProyecto"), "");
                return;
            }
            if (!aspiranteDTO.getAspirante().getEsApto()
                    && sessionProyecto.getTipoSeleccionado().getCodigo().equals(TipoProyectoEnum.TRABAJOTITULACION.getTipo())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.aspirante") + " "
                        + bundle.getString("lbl.msm_no_es_apto_tt"), "");
                return;
            }
            if (tieneAsignadoTrabajoTitulacion(aspiranteDTO.getAspirante())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.aspirante") + " "
                        + bundle.getString("lbl.msm_tiene_autor_proyecto"), "");
                return;
            }
            AutorProyecto autorProyecto = new AutorProyecto(sessionProyecto.getProyectoSeleccionado(), aspiranteDTO.getAspirante(),
                    itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.DESARROLLO.getTipo()).getId(),
                    fecha.getTime(), null);
            if (sessionProyecto.getCronograma().getFechaProrroga() != null) {
                autorProyecto.setFechaCulminacion(sessionProyecto.getCronograma().getFechaProrroga());
            } else {
                autorProyecto.setFechaCulminacion(sessionProyecto.getCronograma().getFechaProrroga());
            }
            AutorProyectoDTO autorProyectoDTO = new AutorProyectoDTO(
                    autorProyecto, aspiranteDTO.getAspirante(), estudianteCarreraDao.find(
                            aspiranteDTO.getAspirante().getId()), null);
            autorProyectoDTO.setPersona(personaDao.find(autorProyectoDTO.getEstudianteCarrera().getEstudianteId().getId()));
            AutorProyectoDTO ap = contieneAutorProyecto(autorProyectoDTO);
            if (ap == null) {
                sessionProyecto.getAutoresProyectoDTO().add(autorProyectoDTO);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.autor") + " "
                        + bundle.getString("lbl.msm_agregar"), "");
                RequestContext.getCurrentInstance().execute("PF('dlgBuscarAspirantes').hide()");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PERMITE DETERMINAR SI EL ASPIRANTE SELECCIONADO YA PERTENEC COMO AUTOR
     * DEL PROYECTO SELECCIONADO
     *
     * @param autorProyecto
     * @return
     */
    private AutorProyectoDTO contieneAutorProyecto(AutorProyectoDTO autorProyecto) {
        AutorProyectoDTO ap = null;
        try {
            for (AutorProyectoDTO autor : sessionProyecto.getAutoresProyectoDTO()) {
                if (autorProyecto.getAspirante().equals(autor.getAspirante())) {
                    ap = autor;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return ap;
    }

    /**
     * DETERMINAR SI ASPIRANTE ESTÁ ASIGNADO EN OTRO TRABAJO DE TITULACIÓN
     *
     * @param aspirante
     * @return
     */
    private boolean tieneAsignadoTrabajoTitulacion(Aspirante aspirante) {
        boolean var = false;
        for (AutorProyecto autorProyecto : aspirante.getAutorProyectoList()) {
            Item tipo = itemService.buscarPorId(autorProyecto.getProyectoId().getTipoProyectoId());
            Item estadoAutor = itemService.buscarPorId(autorProyecto.getEstadoAutorId());
            Item estadoProyecto = itemService.buscarPorId(autorProyecto.getProyectoId().getEstadoProyectoId());
            if (tipo.getCodigo().equals(TipoProyectoEnum.TRABAJOTITULACION.getTipo())
                    && !estadoAutor.getCodigo().equalsIgnoreCase(EstadoAutorEnum.RENUNCIADO.getTipo())
                    && !estadoProyecto.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.ABANDONADO.getTipo())
                    || (!estadoProyecto.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.REPROBADO.getTipo()))) {
                var = true;
                break;
            }
        }
        return var;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RENDERED">
    public void renderedBuscarAspirantes() {
        try {
            sessionAutorProyecto.setRenderedBuscarAspirantes(Boolean.FALSE);
            Item item = itemService.buscarPorId(sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId());
            if (item.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "buscar_aspirante");
                if (tienePermiso == 1) {
                    sessionAutorProyecto.setRenderedBuscarAspirantes(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
        }
    }

    public void renderedSeleccionar() {
        sessionAutorProyecto.setRenderedSeleccionar(Boolean.FALSE);
        Item item = itemService.buscarPorId(sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId());
        if (item.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "select_autor_proyecto");
            if (tienePermiso == 1) {
                sessionAutorProyecto.setRenderedSeleccionar(Boolean.TRUE);
            }
        }
    }
    //</editor-fold>
}

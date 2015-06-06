/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.entity.Item;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionDocenteCarrera;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.Director;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstadoLaboral;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.dao.ItemDao;
import edu.jlmallas.academico.entity.TituloDocente;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import com.jlmallas.comun.dao.NacionalidadFacadeLocal;
import edu.unl.sigett.dao.DirectorDao;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.dao.LineaInvestigacionDao;
import org.jlmallas.seguridad.dao.LogDao;
import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Nacionalidad;
import com.jlmallas.comun.enumeration.GeneroEnum;
import com.jlmallas.comun.enumeration.TipoDocIdentEnum;
import com.jlmallas.comun.service.CatalogoService;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.entity.Titulo;
import edu.jlmallas.academico.dao.TituloDocenteDao;
import edu.jlmallas.academico.dao.TituloDao;
import edu.jlmallas.academico.service.EstadoLaboralService;
import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.DocenteUsuario;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.DocenteUsuarioService;
import edu.unl.sigett.service.LineaInvestigacionService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.MessageView;
import java.util.Calendar;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.httpClient.ConexionDTO;
import org.jlmallas.httpClient.NetClientService;
import org.jlmallas.secure.SecureDTO;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.dao.RolDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.RolUsuario;
import org.jlmallas.seguridad.entity.Usuario;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarDocenteCarrera",
            pattern = "/editarDocenteCarrera/#{sessionDocenteCarrera.docenteCarreraDTO.docenteCarrera.id}",
            viewId = "/faces/pages/academico/docentesCarrera/editarDocenteCarrera.xhtml"
    ),
    @URLMapping(
            id = "crearDocenteCarrera",
            pattern = "/crearDocenteCarrera/",
            viewId = "/faces/pages/academico/docentesCarrera/editarDocenteCarrera.xhtml"
    ),
    @URLMapping(
            id = "docentesCarrera",
            pattern = "/docentesCarrera/",
            viewId = "/faces/pages/academico/docentesCarrera/index.xhtml"
    )
})
public class AdministrarDocentesCarrera implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE MANAGED BEANS">
    @Inject
    private SessionDocenteCarrera sessionDocenteCarrera;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private CabeceraController cabeceraController;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE SERVICIOS">
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralDao;
    @EJB
    private TituloDao tituloDao;
    @EJB
    private NacionalidadFacadeLocal nacionalidadFacadeLocal;
    @EJB
    private RolDao rolDao;
    @EJB
    private RolUsuarioDao rolUsuarioDao;
    @EJB
    private DocenteCarreraDao docenteCarreraDao;
    @EJB
    private DocenteDao docenteDao;
    @EJB
    private PersonaDao personaDao;
    @EJB
    private ItemDao itemDao;
    @EJB
    private EstadoLaboralService estadoLaboralService;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private DocenteUsuarioService docenteUsuarioService;
    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteFacadeLocal;
    @EJB
    private LineaInvestigacionDao lineaInvestigacionFacadeLocal;
    @EJB
    private TituloDocenteDao tituloDocenteFacadeLocal;
    @EJB
    private DirectorDao directorFacadeLocal;
    @EJB
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB
    private ItemService itemService;
    @EJB
    private CatalogoService catalogoService;
    @EJB
    private LineaInvestigacionService lineaInvestigacionService;
//</editor-fold>

    public void init() {
        this.buscar();
        this.renderedEditar();
        this.renderedCrear();
        sessionDocenteCarrera.setLineasInvestigacionDualList(new DualListModel<LineaInvestigacion>());
    }

    public void initEditar() {
        listadoEstadosLaborales();
        listadoTiposDocumentos();
        listadoTitulos();
        listadoGeneros();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void listadoTiposDocumentos() {
        try {
            sessionDocenteCarrera.setTiposDocumento(itemService.buscarPorCatalogo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * LISTADO DE ESTADOS LABORALES
     */
    private void listadoEstadosLaborales() {
        try {
            sessionDocenteCarrera.getEstadoLaborales().clear();
            List<EstadoLaboral> estadosLaborales = estadoLaboralService.buscar(new EstadoLaboral());
            if (estadosLaborales == null) {
                return;
            }
            for (EstadoLaboral e : estadosLaborales) {
                Item item = itemService.buscarPorId(e.getTipoContratoId());
                e.setTipoContrato(item.getCodigo());
                sessionDocenteCarrera.getEstadoLaborales().add(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listadoTitulos() {
        try {
            sessionDocenteCarrera.setTitulos(tituloDocenteFacadeLocal.buscar(new TituloDocente(new Titulo())));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listadoGeneros() {
        try {
            sessionDocenteCarrera.setGeneros(itemService.buscarPorCatalogo(CatalogoEnum.GENERO.getTipo()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * CREAR
     *
     * @return
     */
    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_docente_carrera");
            if (tienePermiso == 1) {
                sessionDocenteCarrera.setDocenteCarreraDTO(new DocenteCarreraDTO(new DocenteCarrera(null, new Docente(), new Carrera(), Boolean.TRUE),
                        new Persona(), new Director(null, Boolean.TRUE)));
                sessionDocenteCarrera.setEstadoLaboral("");
                sessionDocenteCarrera.setTipoDocumento("");
                sessionDocenteCarrera.setTitulo("");
                sessionDocenteCarrera.setLineaInvestigacionDocentesRemovidos(new ArrayList<LineaInvestigacionDocente>());
                listadoLineasInvestigacion(new Docente());
                navegacion = "pretty:crearDocenteCarrera";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". "
                            + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navegacion;
    }

    /**
     * EDITAR
     *
     * @param docenteCarreraAux
     * @return
     */
    public String editar(DocenteCarreraDTO docenteCarreraAux) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_docente_carrera");
            if (tienePermiso == 1) {
                sessionDocenteCarrera.setLineaInvestigacionDocentesRemovidos(new ArrayList<LineaInvestigacionDocente>());
                sessionDocenteCarrera.setDocenteCarreraDTO(new DocenteCarreraDTO(docenteCarreraDao.find(
                        docenteCarreraAux.getDocenteCarrera().getId()), personaDao.find(docenteCarreraAux.
                                getDocenteCarrera().getDocenteId().getId()),
                        directorFacadeLocal.find(docenteCarreraAux.getDocenteCarrera().getId())));
                listadoLineasInvestigacion(docenteCarreraAux.getDocenteCarrera().getDocenteId());
                docenteCarreraAux.getDocenteCarrera().getDocenteId().getEstadoLaboralId().setTipoContrato(
                        itemService.buscarPorId(docenteCarreraAux.getDocenteCarrera().getDocenteId().
                                getEstadoLaboralId().getTipoContratoId()).getCodigo());
                sessionDocenteCarrera.setEstadoLaboral(docenteCarreraAux.
                        getDocenteCarrera().getDocenteId().getEstadoLaboralId().toString());
                sessionDocenteCarrera.setTitulo(docenteCarreraAux.
                        getDocenteCarrera().getDocenteId().getTituloDocenteId().toString());
                sessionDocenteCarrera.setTipoDocumento(itemDao.find(docenteCarreraAux.getPersona().getTipoDocumentoIdentificacionId()).
                        toString());
                sessionDocenteCarrera.setGenero(itemDao.find(docenteCarreraAux.getPersona().
                        getGeneroId()).
                        toString());
                navegacion = "pretty:editarDocenteCarrera";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void buscar() {
        this.sessionDocenteCarrera.getDocenteCarreraDTOs().clear();
        this.sessionDocenteCarrera.getFilterDocenteCarrerasDTO().clear();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "buscar_docente_carrera");
            if (tienePermiso == 1) {
                List<DocenteCarrera> docenteCarreras = docenteCarreraDao.buscar(new DocenteCarrera(null, null, sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(),
                        Boolean.FALSE));
                if (docenteCarreras == null) {
                    return;
                }
                for (DocenteCarrera docenteCarrera : docenteCarreras) {
                    DocenteCarreraDTO dca = new DocenteCarreraDTO(docenteCarrera, personaDao.find(docenteCarrera.getDocenteId().getId()),
                            directorFacadeLocal.find(docenteCarrera.getId()));
                    this.sessionDocenteCarrera.getDocenteCarreraDTOs().add(dca);
                }
                sessionDocenteCarrera.getFilterDocenteCarrerasDTO().addAll(sessionDocenteCarrera.getDocenteCarreraDTOs());
            } else {
                if (tienePermiso == 2) {
                    MessageView messageView = new MessageView();
                    messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". "
                            + bundle.getString("lbl.msm_consulte"), "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String grabar() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            /**
             * GENERO
             */
            Item g = itemDao.buscarPorCodigo(CatalogoEnum.GENERO.getTipo(), sessionDocenteCarrera.getGenero());
            if (g != null) {
                sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().
                        setGeneroId(g.getId());
            }
            /**
             * Tipo de documento de identificación
             */
            Item tdi = itemDao.buscarPorCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(), sessionDocenteCarrera.getTipoDocumento());
            if (tdi != null) {
                sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().
                        setTipoDocumentoIdentificacionId(tdi.getId());
            }
            /**
             * Estado Laboral
             */
            EstadoLaboral el = estadoLaboralService.buscarPorTipoContrato(new EstadoLaboral(null, itemService.
                    buscarPorCatalogoCodigo(CatalogoEnum.TIPOCONTRATO.getTipo(), sessionDocenteCarrera.getEstadoLaboral().toUpperCase()).getId()));
            if (el != null) {
                sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().
                        setEstadoLaboralId(el);
            }
            /**
             * Titulo de docente
             */
            int posTitulo = sessionDocenteCarrera.getTitulo().indexOf(":");
            TituloDocente td = tituloDocenteFacadeLocal.find(Long.parseLong(sessionDocenteCarrera.getTitulo().substring(0, posTitulo)));
            if (td != null) {
                sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().setTituloDocenteId(td);
            }

            if (sessionDocenteCarrera.getDocenteCarreraDTO().
                    getDocenteCarrera().getId() == null) {
                int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_docente_carrera");
                if (tienePermiso == 1) {
                    if (personaDao.esUnico(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNumeroIdentificacion(),
                            sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getId())) {
                        /**
                         * GRABAR PERSONA
                         */
                        personaDao.create(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona());
                        sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().
                                setId(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getId());
                        /**
                         * GRABAR DOCENTE
                         */
                        docenteDao.create(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
                        /**
                         * GRABAR LOG
                         */
                        logFacadeLocal.create(logFacadeLocal.crearLog("Docente", sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().getId() + "",
                                "CREAR", "|Nombres= " + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNombres() + "|Apellidos= "
                                + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getApellidos()
                                + "|Cédula= " + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNumeroIdentificacion() + "|Email= "
                                + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getEmail(), sessionUsuario.getUsuario()));

                        /**
                         * GRABAR DOCENTE CARRERA
                         */
                        sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera());
                        docenteCarreraDao.create(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera());
                        /**
                         * GRABAR DIRECTOR
                         */
                        sessionDocenteCarrera.getDocenteCarreraDTO().getDirector().setId(sessionDocenteCarrera.getDocenteCarreraDTO()
                                .getDocenteCarrera().getId());
                        directorFacadeLocal.create(sessionDocenteCarrera.getDocenteCarreraDTO().getDirector());
                        /**
                         * GRABAR USSUARIO
                         */
                        this.grabarUsuarioDocente(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
                        /**
                         * GRABAR LINEAS DE INVESTIGACION
                         */
                        grabarLineasInvestigacionDocentes(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
                        if (param.equalsIgnoreCase("grabar")) {
                            navegacion = "pretty:docentesCarrera";
                            sessionDocenteCarrera.setDocenteCarreraDTO(new DocenteCarreraDTO(new DocenteCarrera(), new Persona(), new Director()));
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_existe"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    this.buscar();
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_docente_carrera");
                if (tienePermiso == 1) {
                    if (personaDao.esUnico(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNumeroIdentificacion(),
                            sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getId())) {
                        personaDao.edit(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona());
                        sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().setId(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getId());
                        docenteDao.edit(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("Docente", sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().getId() + "",
                                "CREAR", "|Nombres= " + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNombres() + "|Apellidos= "
                                + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getApellidos()
                                + "|Cédula= " + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNumeroIdentificacion() + "|Email= "
                                + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getEmail(), sessionUsuario.getUsuario()));

                        sessionDocenteCarrera.getDocenteCarreraDTO().getDirector().setEsActivo(true);
                        docenteCarreraDao.edit(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera());
                        grabarLineasInvestigacionDocentes(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
                        removerDocenteLineasInvestigacion(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
                        if (param.equalsIgnoreCase("grabar")) {
                            navegacion = "pretty:docentesCarrera";
                            sessionDocenteCarrera.setDocenteCarreraDTO(new DocenteCarreraDTO(new DocenteCarrera(), new Persona(), new Director()));
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_existe"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    this.buscar();
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    /**
     * LISTA LINEAS DE INVESTIGACION EL DOCENTE SELECCIONADO
     *
     * @param docente
     */
    public void listadoLineasInvestigacion(Docente docente) {
        List<LineaInvestigacion> lineaInvestigacionDocentes = new ArrayList<>();
        try {
            if (docente == null) {
                return;
            }
            if (docente.getId() != null) {
                lineaInvestigacionDocentes = lineaInvestigacionService.buscarPorDocente(new LineaInvestigacionDocente(docente.getId(), null));
            }
            sessionDocenteCarrera.setLineasInvestigacionDualList(new DualListModel<>(lineaInvestigacionService.
                    buscarDiferenciaDocenteCarrera(new LineaInvestigacionCarrera(null, sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId()),
                            new LineaInvestigacionDocente(docente.getId(), null)), lineaInvestigacionDocentes));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Long devuelveLineaInvestigacionEliminar(List<LineaInvestigacionDocente> docenteLineasInv, LineaInvestigacionDocente ld) {
        Long var = (long) 0;
        for (LineaInvestigacionDocente lineaInvestigacionDocente : docenteLineasInv) {
            if (lineaInvestigacionDocente.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
                var = lineaInvestigacionDocente.getId();
            }
        }
        return var;
    }

    public void transferDocenteLineaInvestigacion(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                LineaInvestigacion li = lineaInvestigacionFacadeLocal.find(id);
                LineaInvestigacionDocente lid = new LineaInvestigacionDocente();
                lid.setLineaInvestigacionId(li);
                if (event.isRemove()) {
                    sessionDocenteCarrera.getLineaInvestigacionDocentesRemovidos().add(lid);
                } else {
                    sessionDocenteCarrera.getLineaInvestigacionDocentesRemovidos().remove(lid);
                }
            }
        } catch (Exception e) {
        }
    }

    public void removerDocenteLineasInvestigacion(Docente docente) {
        try {
            if (docente.getId() != null) {
                LineaInvestigacionDocente lineaInvestigacionDocenteBuscar = new LineaInvestigacionDocente();
                lineaInvestigacionDocenteBuscar.setDocenteId(docente.getId());
                for (LineaInvestigacionDocente ld : sessionDocenteCarrera.getLineaInvestigacionDocentesRemovidos()) {
                    Long id = devuelveLineaInvestigacionEliminar(lineaInvestigacionDocenteFacadeLocal.buscar(lineaInvestigacionDocenteBuscar), ld);
                    LineaInvestigacionDocente lid = null;
                    lid = lineaInvestigacionDocenteFacadeLocal.find(id);
                    if (lid != null) {
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionDocente", ld.getId() + "",
                                "DELETE", "LineaInvestigacion=" + ld.getLineaInvestigacionId() + "|Docente=" + ld.getDocenteId(),
                                sessionUsuario.getUsuario()));
                        lineaInvestigacionDocenteFacadeLocal.remove(lid);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * GRABAR LÍNEAS DE INVESTIGACIÓN
     *
     * @param docente
     */
    public void grabarLineasInvestigacionDocentes(Docente docente) {
        List<LineaInvestigacionDocente> lids = new ArrayList<>();
        for (Object o : sessionDocenteCarrera.getLineasInvestigacionDualList().getTarget()) {
            int v = o.toString().indexOf(":");
            Long id = Long.parseLong(o.toString().substring(0, v));
            LineaInvestigacion li = lineaInvestigacionFacadeLocal.find(id);
            LineaInvestigacionDocente ld = new LineaInvestigacionDocente();
            ld.setDocenteId(docente.getId());
            ld.setLineaInvestigacionId(li);
            lids.add(ld);
        }
        for (LineaInvestigacionDocente lineaInvestigacionDocente : lids) {
            LineaInvestigacionDocente lineaInvestigacionDocenteBuscar = new LineaInvestigacionDocente();
            lineaInvestigacionDocenteBuscar.setDocenteId(docente.getId());
            if (contieneLineaInvestigacion(lineaInvestigacionDocenteFacadeLocal.buscar(lineaInvestigacionDocente), lineaInvestigacionDocente) == false) {
                lineaInvestigacionDocenteFacadeLocal.create(lineaInvestigacionDocente);
                logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionDocente", lineaInvestigacionDocente.getId() + "",
                        "CREAR", "|LineaInvestigacion= " + lineaInvestigacionDocente.getLineaInvestigacionId() + "|Docente="
                        + lineaInvestigacionDocente.getDocenteId(), sessionUsuario.getUsuario()));
            }
        }
    }

    /**
     * DETERMINAR SI CONTIENE UNA LÍNEA DE INVESTIGACIÓN EL DOCENTE
     *
     * @param docenteLineasInv
     * @param ld
     * @return
     */
    public boolean contieneLineaInvestigacion(List<LineaInvestigacionDocente> docenteLineasInv, LineaInvestigacionDocente ld) {
        boolean var = false;
        for (LineaInvestigacionDocente lineaInvestigacionDocente : docenteLineasInv) {
            if (lineaInvestigacionDocente.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
                var = true;
                break;
            }
        }
        return var;
    }

    /**
     * GRABAR UN USUARIO PARA LOS DOCENTES
     *
     * @param docente
     */
    private void grabarUsuarioDocente(Docente docente) {
        try {
            Usuario usuario = null;
            DocenteUsuario du = docenteUsuarioService.buscarPorDocente(new DocenteUsuario(null, docente.getId()));
            Persona personaDocente = personaDao.find(docente.getId());
            if (du != null) {
                usuario = usuarioDao.find(du.getId());
            }
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setApellidos(personaDocente.getApellidos());
                usuario.setNombres(personaDocente.getNombres());
                usuario.setEmail(personaDocente.getEmail());
                usuario.setEsSuperuser(false);
                usuario.setEsActivo(true);
                usuario.setPassword(cabeceraController.getSecureService().encrypt(
                                    new SecureDTO(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(),personaDocente.getNumeroIdentificacion())));
                usuario.setUsername(personaDocente.getNumeroIdentificacion());
                if (usuarioService.unicoUsername(usuario.getUsername()) == false) {
                    usuarioDao.create(usuario);
                    DocenteUsuario docenteUsuario = new DocenteUsuario();
                    docenteUsuario.setDocenteId(docente.getId());
                    docenteUsuario.setId(usuario.getId());
                    docenteUsuario.setId(usuario.getId());
                    docenteUsuarioService.guardar(docenteUsuario);
                    Rol rol = rolDao.find((long) 1);
                    if (rol != null) {
                        RolUsuario rolUsuario = new RolUsuario();
                        rolUsuario.setRolId(rol);
                        rolUsuario.setUsuarioId(usuario);
                        rolUsuarioDao.create(rolUsuario);
                    }
                }
            } else {
                usuario.setPassword(cabeceraController.getSecureService().encrypt(
                                    new SecureDTO(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(),personaDocente.getNumeroIdentificacion())));
                usuarioDao.edit(usuario);
            }
        } catch (Exception e) {
            e.initCause(e);
        }
    }

    public void crearPDF(Carrera carrera) throws IOException, DocumentException {
        Document pdf = new Document();
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources/img" + File.separator + "selloInstitucion.png";
        Image image = Image.getInstance(logo);
        image.scaleToFit(50, 50);
        image.setAbsolutePosition(50f, 775f);
        Image carreraLogo = Image.getInstance(carrera.getLogo());
        carreraLogo.scaleToFit(50, 50);
        carreraLogo.setAbsolutePosition(450f, 775f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(pdf, baos);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment; filename=Docentes.pdf");
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.setMargins(20f, 20f, 20f, 20f);
        pdf.setPageSize(PageSize.A4);
        Font fontHeader = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
        Font fontTitle = FontFactory.getFont("Times-Roman", 18, Font.BOLD);
        Paragraph title = new Paragraph(bundle.getString("lbl.listado") + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.docentes"), fontTitle);
        Paragraph titleCarrera = new Paragraph(bundle.getString("lbl.carrera") + " " + bundle.getString("lbl.de") + " " + carrera.getNombre(), fontTitle);
        Paragraph titleArea = new Paragraph(carrera.getAreaId().getNombre(), fontTitle);
        title.setSpacingAfter(20);
        title.setAlignment(1);
        titleArea.setSpacingAfter(20);
        titleArea.setAlignment(1);
        titleCarrera.setSpacingAfter(20);
        titleCarrera.setAlignment(1);
        pdf.open();
        pdf.add(image);
        pdf.add(carreraLogo);
        pdf.add(title);
        pdf.add(titleArea);
        pdf.add(titleCarrera);
        PdfPTable pdfTable = new PdfPTable(5);
        pdfTable.setWidthPercentage(100f);
        pdfTable.setHorizontalAlignment(0);
        PdfPCell cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.nombres"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.apellidos"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.ci"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.titulo"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.email"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        pdfTable.setHeaderRows(1);
//        for (DocenteCarrera docenteCarrera : docenteCarreras) {
//            Persona personaDocente = personaDao.find(docenteCarrera.getDocenteId().getId());
//            pdfTable.addCell(personaDocente.getNombres());
//            pdfTable.addCell(personaDocente.getApellidos());
//            pdfTable.addCell(personaDocente.getNumeroIdentificacion());
//            pdfTable.addCell(docenteCarrera.getDocenteId().getTituloDocenteId().getTituloId().getNombre());
//            pdfTable.addCell(personaDocente.getEmail());
//        }
        pdf.add(pdfTable);
        pdf.close();
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    private void renderedEditar() {
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_docente_carrera");
        if (tienePermiso == 1) {
            sessionDocenteCarrera.setRenderedEditar(true);
            sessionDocenteCarrera.setRenderedNoEditar(false);
        } else {
            sessionDocenteCarrera.setRenderedEditar(false);
            sessionDocenteCarrera.setRenderedNoEditar(true);
        }
    }
//
//    public boolean renderedEliminar() {
//        boolean var = false;
//        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "eliminar_docente_carrera");
//        if (tienePermiso == 1) {
//            var = true;
//        }
//        return var;
//    }

    private void renderedCrear() {

        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_docente_carrera");
        if (tienePermiso == 1) {
            sessionDocenteCarrera.setRenderedCrear(true);
            return;
        }
        sessionDocenteCarrera.setRenderedCrear(false);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES">

    /**
     * Unidades de docente por paralelo
     *
     * @param paraleloId
     * @param carrera
     */
    public void sgaWebServicesUnidadesDocenteParalelo(String paraleloId, Carrera carrera) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        if (usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                String serviceUrl = configuracionGeneralDao.find((int) 43).getValor();
                String s = serviceUrl + "?id_paralelo=" + paraleloId;
                ConexionDTO seguridad = new ConexionDTO(configuracionGeneralDao.find((int) 5).getValor(),
                        s, configuracionGeneralDao.find((int) 6).getValor());
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    parserDocentesUnidadesParaleloJson(datos, carrera);
                }
            } catch (Exception e) {
                messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
            }
        } else {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
        }
    }

    /**
     * PARSER DOCENTES POR UNIDADES PARALELO
     *
     * @param elemento
     * @param carrera
     * @throws Exception
     */
    private void parserDocentesUnidadesParaleloJson(JsonElement elemento, Carrera carrera) throws Exception {
        try {

            if (elemento.isJsonObject()) {
                sessionDocenteCarrera.setI(0);
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parserDocentesUnidadesParaleloJson(jsonElement, carrera);
                    } catch (Exception e) {
                        parserDocentesUnidadesParaleloJson(entrada.getValue(), carrera);
                    }
                }
                return;
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionDocenteCarrera.setKeyEnteroWSUnidadesDocenteParalelo(0);
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserDocentesUnidadesParaleloJson(entrada, carrera);
                }
                return;
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (sessionDocenteCarrera.getI() > 5) {
                    if (sessionDocenteCarrera.getKeyEnteroWSUnidadesDocenteParalelo() == 7) {
                        Persona persona = personaDao.buscarPorNumeroIdentificacion(valor.getAsString());
                        DocenteCarreraDTO docenteCarreraAux = null;
                        Docente docente = null;
                        if (persona != null) {
                            docente = docenteDao.find(persona.getId());
                        } else {
                            persona = new Persona();
                            persona.setNumeroIdentificacion(valor.getAsString());
                        }
                        if (docente == null) {
                            docente = new Docente();
                        }
                        DocenteCarrera dc = new DocenteCarrera(null, docente, carrera, Boolean.TRUE);
                        docenteCarreraAux = new DocenteCarreraDTO(dc,
                                persona, new Director(null, Boolean.TRUE));
                        sessionDocenteCarrera.setDocenteCarrerDTOWS(docenteCarreraAux);
                        sessionDocenteCarrera.setKeyEnteroWSUnidadesDocenteParalelo(sessionDocenteCarrera.getKeyEnteroWSUnidadesDocenteParalelo() + 1);
                        grabarDesdeWebServices();
                        return;
                    }
                    sessionDocenteCarrera.setKeyEnteroWSUnidadesDocenteParalelo(sessionDocenteCarrera.getKeyEnteroWSUnidadesDocenteParalelo() + 1);
                    return;
                }
                sessionDocenteCarrera.setI(sessionDocenteCarrera.getI() + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GRABAR DOCENTE CARRERA DESDE SINCRONIZACIÓN DE SERVICIOS WEB
     *
     * @param docenteCarreraAux
     */
    private void grabarDesdeWebServices() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona().setEmail("S/N");
                String serviceUrl = configuracionGeneralDao.find((int) 26).getValor();
                String s = serviceUrl + "?cedula=" + sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona().getNumeroIdentificacion();
                ConexionDTO seguridad = new ConexionDTO(configuracionGeneralDao.find((int) 5).getValor(),
                        s, configuracionGeneralDao.find((int) 6).getValor());
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    parserDocenteJson(datos, sessionDocenteCarrera.getDocenteCarrerDTOWS());
                    /**
                     * GRABAR DOCENTE, DOCENTE CARRERA, PERSONA, DIRECTOR
                     */
                    if (sessionDocenteCarrera.getDocenteCarrerDTOWS().getDocenteCarrera().getDocenteId().getId() == null) {
                        Calendar fechaActual = Calendar.getInstance();
                        Item itemT = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(),
                                TipoDocIdentEnum.CEDULA.getTipo());
                        Item itemG = itemService.buscarPorCatalogoCodigo(CatalogoEnum.GENERO.getTipo(), GeneroEnum.MASCULINO.getTipo());
                        Nacionalidad nacionalidad = nacionalidadFacadeLocal.find(1);
                        sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona().setTipoDocumentoIdentificacionId(itemT.getId());
                        sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona().setGeneroId(itemG.getId());
                        sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona()
                                .setFechaNacimiento(fechaActual.getTime());
                        sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona().setNacionalidadId(nacionalidad);
                        if (sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona().getId() == null) {
                            personaDao.create(sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona());
                        } else {
                            personaDao.edit(sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona());
                        }
                        sessionDocenteCarrera.getDocenteCarrerDTOWS().getDocenteCarrera().getDocenteId()
                                .setId(sessionDocenteCarrera.getDocenteCarrerDTOWS().getPersona().getId());
                        docenteDao.create(sessionDocenteCarrera.getDocenteCarrerDTOWS().getDocenteCarrera().getDocenteId());
                        this.grabarUsuarioDocente(sessionDocenteCarrera.getDocenteCarrerDTOWS().getDocenteCarrera().getDocenteId());
                        docenteCarreraDao.create(sessionDocenteCarrera.getDocenteCarrerDTOWS().getDocenteCarrera());
                        sessionDocenteCarrera.getDocenteCarrerDTOWS().getDirector().
                                setId(sessionDocenteCarrera.getDocenteCarrerDTOWS().getDocenteCarrera().getId());
                        directorFacadeLocal.create(sessionDocenteCarrera.getDocenteCarrerDTOWS().getDirector());
                    } else {
                        Persona datosDocente = personaDao.find(
                                sessionDocenteCarrera.getDocenteCarrerDTOWS().getDocenteCarrera().getDocenteId().getId());
                        personaDao.edit(datosDocente);
                        docenteDao.edit(sessionDocenteCarrera.getDocenteCarrerDTOWS().getDocenteCarrera().getDocenteId());
                    }
                }
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * Sincronizar docente por carrera
     *
     * @param c
     */
    public void sgaWebServicesPorCarrera(Carrera c) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        if (usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                /**
                 * Buscar Oferta académica actual de la carrera
                 */
                ConfiguracionCarrera configuracionCarrera = configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(c.getId(), "OA"));
                if (configuracionCarrera == null) {
                    return;
                }
                String ofertaIdActual = configuracionCarrera.getValor();
                String serviceUrl = configuracionGeneralDao.find((int) 41).getValor();
                String s = serviceUrl + "?id_oferta=" + ofertaIdActual + ";id_carrera=" + c.getIdSga();
                ConexionDTO seguridad = new ConexionDTO(configuracionGeneralDao.find((int) 5).getValor(),
                        s, configuracionGeneralDao.find((int) 6).getValor());
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    parserDocentesPorCarreraJson(datos, c);
                }
                this.buscar();
            } catch (Exception e) {
                messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
            }
        } else {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
        }
    }

    /**
     * Parser docente por carrera
     *
     * @param elemento
     * @param carrera
     * @throws Exception
     */
    private void parserDocentesPorCarreraJson(JsonElement elemento, Carrera carrera) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    sessionDocenteCarrera.setKey(entrada.getKey());
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parserDocentesPorCarreraJson(jsonElement, carrera);

                    } catch (Exception e) {
                        parserDocentesPorCarreraJson(elemento, carrera);
                    }
                }
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionDocenteCarrera.setKeyEntero(0);
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserDocentesPorCarreraJson(entrada, carrera);
                }
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (sessionDocenteCarrera.getKeyEntero() == 0) {
                    if (valor.isNumber()) {
                        String paraleloId = valor.getAsInt() + "";
                        this.sgaWebServicesUnidadesDocenteParalelo(paraleloId, carrera);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sgaWebServicesDatosDocente() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        if (usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                String serviceUrl = configuracionGeneralDao.find((int) 26).getValor();
                String s = serviceUrl + "?cedula=" + sessionDocenteCarrera.getDocenteCarreraDTO().
                        getPersona().getNumeroIdentificacion();
                ConexionDTO seguridad = new ConexionDTO(configuracionGeneralDao.find((int) 5).getValor(),
                        s, configuracionGeneralDao.find((int) 6).getValor());
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    parserDocenteJson(datos, sessionDocenteCarrera.getDocenteCarreraDTO());
                    messageView.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
                }
            } catch (Exception e) {
                messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
            }
        } else {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
        }
    }

    /**
     * PARSER DOCENTE
     *
     * @param elemento
     * @throws Exception
     */
    private void parserDocenteJson(JsonElement elemento, DocenteCarreraDTO docenteCarreraAux) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    sessionDocenteCarrera.setKey(entrada.getKey());
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parserDocenteJson(jsonElement, docenteCarreraAux);

                    } catch (Exception e) {
                        parserDocenteJson(entrada.getValue(), docenteCarreraAux);
                    }
                }
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionDocenteCarrera.setKeyEntero(0);
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserDocenteJson(entrada, docenteCarreraAux);
                }
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (sessionDocenteCarrera.getKeyEntero() == 0) {
                    docenteCarreraAux.getPersona().setNombres(valor.getAsString());
                    sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionDocenteCarrera.getKeyEntero() == 1) {
                    docenteCarreraAux.getPersona().setApellidos(valor.getAsString());
                    sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionDocenteCarrera.getKeyEntero() == 3) {
                    TituloDocente tituloDocenteBuscar = new TituloDocente(new Titulo());
                    tituloDocenteBuscar.getTituloId().setNombre(valor.getAsString());
                    List<TituloDocente> tituloDocentes = tituloDocenteFacadeLocal.buscar(tituloDocenteBuscar);
                    TituloDocente td = null;
                    if (tituloDocentes != null) {
                        td = !tituloDocentes.isEmpty() ? tituloDocentes.get(0) : null;
                    }
                    if (td != null) {
                        docenteCarreraAux.getDocenteCarrera().getDocenteId().setTituloDocenteId(td);
                        sessionDocenteCarrera.setTitulo(td.toString());
                        sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                        return;
                    }
                    td = new TituloDocente();
                    Titulo titulo = new Titulo();
                    titulo.setEsActivo(true);
                    titulo.setNombre(valor.getAsString());
                    int espacio = titulo.getNombre().indexOf(" ");
                    titulo.setAbreviacion(titulo.getNombre().substring(0, espacio));
                    tituloDao.create(titulo);
                    td.setTituloId(titulo);
                    docenteCarreraAux.getDocenteCarrera().getDocenteId().setTituloDocenteId(td);
                    tituloDocenteFacadeLocal.create(td);
                    sessionDocenteCarrera.setTitulo(td.toString());
                    sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionDocenteCarrera.getKeyEntero() == 4) {
                    Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOCONTRATO.getTipo(), valor.getAsString().toUpperCase().trim());
                    List<EstadoLaboral> estados = new ArrayList<>();
                    EstadoLaboral estadoLaboralEncontrado = null;
                    if (item != null) {
                        EstadoLaboral estadoLaboralBuscar = new EstadoLaboral();
                        estadoLaboralBuscar.setTipoContratoId(item.getId());
                        estados = estadoLaboralService.buscar(estadoLaboralBuscar);
                        if (estados != null) {
                            estadoLaboralEncontrado = !estados.isEmpty() ? estados.get(0) : null;
                        }
                        if (estadoLaboralEncontrado == null) {
                            estadoLaboralEncontrado = new EstadoLaboral();
                            estadoLaboralEncontrado.setTipoContratoId(item.getId());
                            estadoLaboralService.guardar(estadoLaboralEncontrado);
                        }

                        docenteCarreraAux.getDocenteCarrera().getDocenteId().
                                setEstadoLaboralId(estadoLaboralEncontrado);
                        sessionDocenteCarrera.setEstadoLaboral(item.toString());
                        sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                        return;
                    }
                    item = new Item(null, valor.getAsString().toUpperCase(), valor.getAsString().toUpperCase(), true, null,
                            catalogoService.buscarPorCodigo(CatalogoEnum.TIPOCONTRATO.getTipo()));
                    itemDao.create(item);
                    item.setIdPadre(item.getId());
                    itemDao.edit(item);
                    estadoLaboralEncontrado = new EstadoLaboral();
                    estadoLaboralEncontrado.setTipoContratoId(item.getId());
                    estadoLaboralService.guardar(estadoLaboralEncontrado);
                    docenteCarreraAux.getDocenteCarrera().getDocenteId().
                            setEstadoLaboralId(estadoLaboralEncontrado);
                    sessionDocenteCarrera.setEstadoLaboral(item.toString());
                    sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionDocenteCarrera.getKeyEntero() == 5) {
                    if (valor == null) {
                        docenteCarreraAux.getPersona().setEmail("S/N");
                        return;
                    }
                    if (valor.getAsString().equals("")) {
                        docenteCarreraAux.getPersona().setEmail("S/N");
                        return;
                    }
                    docenteCarreraAux.getPersona().setEmail(valor.getAsString());
                    sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                    return;
                }
                sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionDocenteCarrera getSessionDocenteCarrera() {
        return sessionDocenteCarrera;
    }

    public void setSessionDocenteCarrera(SessionDocenteCarrera sessionDocenteCarrera) {
        this.sessionDocenteCarrera = sessionDocenteCarrera;
    }

    public SessionUsuarioCarrera getSessionUsuarioCarrera() {
        return sessionUsuarioCarrera;
    }

    public void setSessionUsuarioCarrera(SessionUsuarioCarrera sessionUsuarioCarrera) {
        this.sessionUsuarioCarrera = sessionUsuarioCarrera;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }
//</editor-fold>
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.docenteCarrera;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.entity.Item;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
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
import edu.jlmallas.academico.entity.TituloDocente;
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
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.jlmallas.seguridad.dao.LogDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Nacionalidad;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.GeneroEnum;
import com.jlmallas.comun.enumeration.TipoDocIdentEnum;
import com.jlmallas.comun.enumeration.URLWSEnum;
import com.jlmallas.comun.service.CatalogoService;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.NacionalidadService;
import com.jlmallas.comun.service.PersonaService;
import edu.jlmallas.academico.entity.Titulo;
import edu.jlmallas.academico.dao.TituloDocenteDao;
import edu.jlmallas.academico.dao.TituloDao;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.DocenteService;
import edu.jlmallas.academico.service.EstadoLaboralService;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.DocenteUsuario;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.DirectorService;
import edu.unl.sigett.service.DocenteUsuarioService;
import edu.unl.sigett.service.LineaInvestigacionDocenteService;
import edu.unl.sigett.service.LineaInvestigacionService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.MessageView;
import java.util.Calendar;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.httpClient.ConexionDTO;
import org.jlmallas.httpClient.NetClientService;
import org.jlmallas.secure.Secure;
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
@Named(value = "docenteCarreraController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarDocenteCarrera",
            pattern = "/editarDocenteCarrera/",
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
@SuppressWarnings("CallToThreadDumpStack")
public class DocenteCarreraController implements Serializable {

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
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logFacadeLocal;
    @EJB(lookup = "java:global/AcademicoService/TituloDaoImplement!edu.jlmallas.academico.dao.TituloDao")
    private TituloDao tituloDao;
    @EJB(lookup = "java:global/ComunService/NacionalidadServiceImplement!com.jlmallas.comun.service.NacionalidadService")
    private NacionalidadService nacionalidadService;
    @EJB(lookup = "java:global/SeguridadService/RolDaoImplement!org.jlmallas.seguridad.dao.RolDao")
    private RolDao rolDao;
    @EJB(lookup = "java:global/SeguridadService/RolUsuarioDaoImplement!org.jlmallas.seguridad.dao.RolUsuarioDao")
    private RolUsuarioDao rolUsuarioDao;
    @EJB(lookup = "java:global/AcademicoService/DocenteCarreraServiceImplement!edu.jlmallas.academico.service.DocenteCarreraService")
    private DocenteCarreraService docenteCarreraService;
    @EJB(lookup = "java:global/AcademicoService/DocenteServiceImplement!edu.jlmallas.academico.service.DocenteService")
    private DocenteService docenteService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/AcademicoService/EstadoLaboralServiceImplement!edu.jlmallas.academico.service.EstadoLaboralService")
    private EstadoLaboralService estadoLaboralService;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/SigettService/DocenteUsuarioServiceImplement!edu.unl.sigett.service.DocenteUsuarioService")
    private DocenteUsuarioService docenteUsuarioService;
    @EJB(lookup = "java:global/SigettService/LineaInvestigacionDocenteServiceImplement!edu.unl.sigett.service.LineaInvestigacionDocenteService")
    private LineaInvestigacionDocenteService lineaInvestigacionDocenteService;
    @EJB(lookup = "java:global/AcademicoService/TituloDocenteDaoImplement!edu.jlmallas.academico.dao.TituloDocenteDao")
    private TituloDocenteDao tituloDocenteFacadeLocal;
    @EJB(lookup = "java:global/SigettService/DirectorServiceImplement!edu.unl.sigett.service.DirectorService")
    private DirectorService directorService;
    @EJB(lookup = "java:global/SigettService/ConfiguracionCarreraServiceImplement!edu.unl.sigett.service.ConfiguracionCarreraService")
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/ComunService/CatalogoServiceImplement!com.jlmallas.comun.service.CatalogoService")
    private CatalogoService catalogoService;
    @EJB(lookup = "java:global/SigettService/LineaInvestigacionServiceImplement!edu.unl.sigett.service.LineaInvestigacionService")
    private LineaInvestigacionService lineaInvestigacionService;
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;
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
            sessionDocenteCarrera.setDocenteCarreraDTO(new DocenteCarreraDTO(new DocenteCarrera(null, new Docente(), new Carrera(), null, Boolean.TRUE),
                    new Persona(), new Director(null, Boolean.TRUE)));
            sessionDocenteCarrera.setEstadoLaboral("");
            sessionDocenteCarrera.setTipoDocumento("");
            sessionDocenteCarrera.setTitulo("");
            sessionDocenteCarrera.setLineaInvestigacionDocentesRemovidos(new ArrayList<LineaInvestigacionDocente>());
            listadoLineasInvestigacion(new Docente());
            navegacion = "pretty:crearDocenteCarrera";
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
            sessionDocenteCarrera.setLineaInvestigacionDocentesRemovidos(new ArrayList<LineaInvestigacionDocente>());
            sessionDocenteCarrera.setDocenteCarreraDTO(new DocenteCarreraDTO(docenteCarreraService.buscarPorId(
                    new DocenteCarrera(docenteCarreraAux.getDocenteCarrera().getId())), personaService.buscarPorId(new Persona(docenteCarreraAux.
                                    getDocenteCarrera().getDocenteId().getId())),
                    directorService.buscarPorId(new Director(docenteCarreraAux.getDocenteCarrera().getId()))));
            listadoLineasInvestigacion(docenteCarreraAux.getDocenteCarrera().getDocenteId());
            docenteCarreraAux.getDocenteCarrera().getDocenteId().getEstadoLaboralId().setTipoContrato(
                    itemService.buscarPorId(docenteCarreraAux.getDocenteCarrera().getDocenteId().
                            getEstadoLaboralId().getTipoContratoId()).getCodigo());
            sessionDocenteCarrera.setEstadoLaboral(docenteCarreraAux.
                    getDocenteCarrera().getDocenteId().getEstadoLaboralId().toString());
            sessionDocenteCarrera.setTitulo(docenteCarreraAux.
                    getDocenteCarrera().getDocenteId().getTituloDocenteId().toString());
            sessionDocenteCarrera.setTipoDocumento(itemService.buscarPorId(docenteCarreraAux.getPersona().getTipoDocumentoIdentificacionId()).
                    toString());
            sessionDocenteCarrera.setGenero(itemService.buscarPorId(docenteCarreraAux.getPersona().
                    getGeneroId()).
                    toString());
            navegacion = "pretty:editarDocenteCarrera";
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void buscar() {
        this.sessionDocenteCarrera.getDocenteCarreraDTOs().clear();
        this.sessionDocenteCarrera.getFilterDocenteCarrerasDTO().clear();
        try {
            List<DocenteCarrera> docenteCarreras = docenteCarreraService.buscar(new DocenteCarrera(null, null, sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(),
                    null, Boolean.FALSE));
            if (docenteCarreras == null) {
                return;
            }
            for (DocenteCarrera docenteCarrera : docenteCarreras) {
                DocenteCarreraDTO dca = new DocenteCarreraDTO(docenteCarrera, personaService.buscarPorId(new Persona(docenteCarrera.getDocenteId().getId())),
                        directorService.buscarPorId(new Director(docenteCarrera.getId())));
                this.sessionDocenteCarrera.getDocenteCarreraDTOs().add(dca);
            }
            sessionDocenteCarrera.getFilterDocenteCarrerasDTO().addAll(sessionDocenteCarrera.getDocenteCarreraDTOs());
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
            Item g = itemService.buscarPorCatalogoCodigo(CatalogoEnum.GENERO.getTipo(), sessionDocenteCarrera.getGenero());
            if (g != null) {
                sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().
                        setGeneroId(g.getId());
            }
            Item tdi = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(), sessionDocenteCarrera.getTipoDocumento());
            if (tdi != null) {
                sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().
                        setTipoDocumentoIdentificacionId(tdi.getId());
            }
            EstadoLaboral el = estadoLaboralService.buscarPorTipoContrato(new EstadoLaboral(null, itemService.
                    buscarPorCatalogoCodigo(CatalogoEnum.TIPOCONTRATO.getTipo(), sessionDocenteCarrera.getEstadoLaboral().toUpperCase()).getId()));
            if (el != null) {
                sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().
                        setEstadoLaboralId(el);
            }
            int posTitulo = sessionDocenteCarrera.getTitulo().indexOf(":");
            TituloDocente td = tituloDocenteFacadeLocal.find(Long.parseLong(sessionDocenteCarrera.getTitulo().substring(0, posTitulo)));
            if (td != null) {
                sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().setTituloDocenteId(td);
            }

            if (sessionDocenteCarrera.getDocenteCarreraDTO().
                    getDocenteCarrera().getId() == null) {
                if (personaService.esUnico(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNumeroIdentificacion(),
                        sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getId())) {
                    personaService.guardar(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona());
                    sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().
                            setId(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getId());
                    docenteService.guardar(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
                    logFacadeLocal.create(logFacadeLocal.crearLog("Docente", sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().getId() + "",
                            "CREAR", "|Nombres= " + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNombres() + "|Apellidos= "
                            + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getApellidos()
                            + "|Cédula= " + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNumeroIdentificacion() + "|Email= "
                            + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getEmail(), sessionUsuario.getUsuario()));

                    sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera());
                    docenteCarreraService.guardar(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera());
                    sessionDocenteCarrera.getDocenteCarreraDTO().getDirector().setId(sessionDocenteCarrera.getDocenteCarreraDTO()
                            .getDocenteCarrera().getId());
                    directorService.guardar(sessionDocenteCarrera.getDocenteCarreraDTO().getDirector());
                    this.grabarUsuarioDocente(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
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
                if (personaService.esUnico(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNumeroIdentificacion(),
                        sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getId())) {
                    personaService.actualizar(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona());
                    sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().setId(sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getId());
                    docenteService.actualizar(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId());
                    logFacadeLocal.create(logFacadeLocal.crearLog("Docente", sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera().getDocenteId().getId() + "",
                            "CREAR", "|Nombres= " + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNombres() + "|Apellidos= "
                            + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getApellidos()
                            + "|Cédula= " + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getNumeroIdentificacion() + "|Email= "
                            + sessionDocenteCarrera.getDocenteCarreraDTO().getPersona().getEmail(), sessionUsuario.getUsuario()));

                    sessionDocenteCarrera.getDocenteCarreraDTO().getDirector().setEsActivo(true);
                    docenteCarreraService.actualizar(sessionDocenteCarrera.getDocenteCarreraDTO().getDocenteCarrera());
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
            }
        } catch (NumberFormatException e) {
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
                LineaInvestigacion li = lineaInvestigacionService.buscarPorId(new LineaInvestigacion(id));
                LineaInvestigacionDocente lid = new LineaInvestigacionDocente();
                lid.setLineaInvestigacionId(li);
                if (event.isRemove()) {
                    sessionDocenteCarrera.getLineaInvestigacionDocentesRemovidos().add(lid);
                } else {
                    sessionDocenteCarrera.getLineaInvestigacionDocentesRemovidos().remove(lid);
                }
            }
        } catch (NumberFormatException e) {
        }
    }

    public void removerDocenteLineasInvestigacion(Docente docente) {
        try {
            if (docente.getId() != null) {
                LineaInvestigacionDocente lineaInvestigacionDocenteBuscar = new LineaInvestigacionDocente();
                lineaInvestigacionDocenteBuscar.setDocenteId(docente.getId());
                for (LineaInvestigacionDocente ld : sessionDocenteCarrera.getLineaInvestigacionDocentesRemovidos()) {
                    Long id = devuelveLineaInvestigacionEliminar(lineaInvestigacionDocenteService.buscar(lineaInvestigacionDocenteBuscar), ld);
                    LineaInvestigacionDocente lid = null;
                    lid = lineaInvestigacionDocenteService.buscarPorId(new LineaInvestigacionDocente(id));
                    if (lid != null) {
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionDocente", ld.getId() + "",
                                "DELETE", "LineaInvestigacion=" + ld.getLineaInvestigacionId() + "|Docente=" + ld.getDocenteId(),
                                sessionUsuario.getUsuario()));
                        lineaInvestigacionDocenteService.eliminar(lid);
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
            LineaInvestigacion li = lineaInvestigacionService.buscarPorId(new LineaInvestigacion(id));
            LineaInvestigacionDocente ld = new LineaInvestigacionDocente();
            ld.setDocenteId(docente.getId());
            ld.setLineaInvestigacionId(li);
            lids.add(ld);
        }
        for (LineaInvestigacionDocente lineaInvestigacionDocente : lids) {
            LineaInvestigacionDocente lineaInvestigacionDocenteBuscar = new LineaInvestigacionDocente();
            lineaInvestigacionDocenteBuscar.setDocenteId(docente.getId());
            if (contieneLineaInvestigacion(lineaInvestigacionDocenteService.buscar(lineaInvestigacionDocente), lineaInvestigacionDocente) == false) {
                lineaInvestigacionDocenteService.guardar(lineaInvestigacionDocente);
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
            Persona personaDocente = personaService.buscarPorId(new Persona(docente.getId()));
            if (du != null) {
                usuario = usuarioService.buscarPorId(new Usuario(du.getId()));
            }
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setApellidos(personaDocente.getApellidos());
                usuario.setNombres(personaDocente.getNombres());
                usuario.setEmail(personaDocente.getEmail());
                usuario.setEsSuperuser(false);
                usuario.setEsActivo(true);
                usuario.setPassword(cabeceraController.getSecureService().encrypt(
                        new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), personaDocente.getNumeroIdentificacion())));
                usuario.setUsername(personaDocente.getNumeroIdentificacion());
                if (usuarioService.unicoUsername(usuario.getUsername()) == false) {
                    usuarioService.guardar(usuario);
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
                        new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), personaDocente.getNumeroIdentificacion())));
                usuarioService.actualizar(usuario);
            }
        } catch (Exception e) {
            e.initCause(e);
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    private void renderedEditar() {
        int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "editar_docente_carrera");
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

        int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "crear_docente_carrera");
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
        if (usuarioService.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.UNIDADDOCENTEPARALELO.getTipo())).get(0).getValor();
                String s = serviceUrl + "?id_paralelo=" + paraleloId;
                ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
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
                        Persona persona = personaService.buscarPorNumeroIdentificacion(valor.getAsString());
                        DocenteCarreraDTO docenteCarreraAux = null;
                        Docente docente = null;
                        if (persona != null) {
                            docente = docenteService.buscarPorId(new Docente(persona.getId()));
                        } else {
                            persona = new Persona();
                            persona.setNumeroIdentificacion(valor.getAsString());
                        }
                        if (docente == null) {
                            docente = new Docente();
                        }
                        DocenteCarrera dc = new DocenteCarrera(null, docente, carrera, null, Boolean.TRUE);
                        docenteCarreraAux = new DocenteCarreraDTO(dc,
                                persona, new Director(null, Boolean.TRUE));
                        sessionDocenteCarrera.setDocenteCarreraDTOWS(docenteCarreraAux);
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
        if (usuarioService.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().setEmail("S/N");
                String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.DATOSDOCENTE.getTipo())).get(0).getValor();
                String s = serviceUrl + "?cedula=" + sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().getNumeroIdentificacion();
                ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    parserDocenteJson(datos, sessionDocenteCarrera.getDocenteCarreraDTOWS());
                    /**
                     * GRABAR DOCENTE, DOCENTE CARRERA, PERSONA, DIRECTOR
                     */

                    if (sessionDocenteCarrera.getDocenteCarreraDTOWS().getDocenteCarrera().getDocenteId().getId() == null) {
                        if (!personaService.esUnico(sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().getNumeroIdentificacion(),
                                sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().getId())) {
                            return;
                        }
                        Calendar fechaActual = Calendar.getInstance();
                        Item itemT = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(),
                                TipoDocIdentEnum.CEDULA.getTipo());
                        Item itemG = itemService.buscarPorCatalogoCodigo(CatalogoEnum.GENERO.getTipo(), GeneroEnum.MASCULINO.getTipo());
                        Nacionalidad nacionalidad = nacionalidadService.buscarPorId(new Nacionalidad(1));
                        sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().setTipoDocumentoIdentificacionId(itemT.getId());
                        sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().setGeneroId(itemG.getId());
                        sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona()
                                .setFechaNacimiento(fechaActual.getTime());
                        sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().setNacionalidadId(nacionalidad);
                        if (sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().getId() == null) {
                            personaService.guardar(sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona());
                        } else {
                            personaService.actualizar(sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona());
                        }
                        sessionDocenteCarrera.getDocenteCarreraDTOWS().getDocenteCarrera().getDocenteId()
                                .setId(sessionDocenteCarrera.getDocenteCarreraDTOWS().getPersona().getId());
                        docenteService.guardar(sessionDocenteCarrera.getDocenteCarreraDTOWS().getDocenteCarrera().getDocenteId());
                        this.grabarUsuarioDocente(sessionDocenteCarrera.getDocenteCarreraDTOWS().getDocenteCarrera().getDocenteId());
                        docenteCarreraService.guardar(sessionDocenteCarrera.getDocenteCarreraDTOWS().getDocenteCarrera());
                        sessionDocenteCarrera.getDocenteCarreraDTOWS().getDirector().
                                setId(sessionDocenteCarrera.getDocenteCarreraDTOWS().getDocenteCarrera().getId());
                        directorService.guardar(sessionDocenteCarrera.getDocenteCarreraDTOWS().getDirector());
                    } else {
                        Persona datosDocente = personaService.buscarPorId(
                                new Persona(sessionDocenteCarrera.getDocenteCarreraDTOWS().getDocenteCarrera().getDocenteId().getId()));
                        personaService.actualizar(datosDocente);
                        docenteService.actualizar(sessionDocenteCarrera.getDocenteCarreraDTOWS().getDocenteCarrera().getDocenteId());
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
        if (usuarioService.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                /**
                 * Buscar Oferta académica actual de la carrera
                 */
                ConfiguracionCarrera configuracionCarrera = configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(c.getId(), "OA"));
                if (configuracionCarrera == null) {
                    return;
                }
                String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.PARALELOCARRERA.getTipo())).get(0).getValor();
                String ofertaIdActual = configuracionCarrera.getValor();
                String s = serviceUrl + "?id_oferta=" + ofertaIdActual + ";id_carrera=" + c.getIdSga();
                ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
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
        if (usuarioService.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.DATOSDOCENTE.getTipo())).get(0).getValor();
                String s = serviceUrl + "?cedula=" + sessionDocenteCarrera.getDocenteCarreraDTO().
                        getPersona().getNumeroIdentificacion();
                ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
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
                    if (valor == null) {
                        sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                        return;
                    }
                    if (valor.getAsString() == null) {
                        sessionDocenteCarrera.setKeyEntero(sessionDocenteCarrera.getKeyEntero() + 1);
                        return;
                    }
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
                    if (espacio > 0) {
                        titulo.setAbreviacion(titulo.getNombre().substring(0, espacio));
                    } else {
                        titulo.setAbreviacion(titulo.getNombre());
                    }
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
                    itemService.guardar(item);
                    item.setIdPadre(item.getId());
                    itemService.actualizar(item);
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

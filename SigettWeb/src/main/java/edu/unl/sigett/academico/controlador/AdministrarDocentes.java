/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.jlmallas.comun.entity.Item;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionDocente;
import edu.unl.sigett.seguridad.controlador.AdministrarUsuarios;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.Director;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstadoLaboral;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import com.jlmallas.comun.entity.Nacionalidad;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemFacadeLocal;
import edu.jlmallas.academico.entity.TituloDocente;
import org.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import edu.unl.sigett.session.DirectorFacadeLocal;
import edu.jlmallas.academico.service.DocenteCarreraFacadeLocal;
import edu.jlmallas.academico.service.DocenteFacadeLocal;
import edu.jlmallas.academico.service.EstadoLaboralFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionCarreraFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionDocenteFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import com.jlmallas.comun.service.NacionalidadFacadeLocal;
import com.jlmallas.comun.service.PersonaFacadeLocal;
import edu.jlmallas.academico.service.TituloDocenteFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.comun.controlador.AdministrarLineasInvestigacionDocente;
import edu.unl.sigett.session.UsuarioCarreraFacadeLocal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarDocente",
            pattern = "/editarDocente/#{sessionDocente.docente.id}",
            viewId = "/faces/pages/academico/editarDocente.xhtml"
    ),
    @URLMapping(
            id = "crearDocente",
            pattern = "/crearDocente/",
            viewId = "/faces/pages/academico/editarDocente.xhtml"
    ),
    @URLMapping(
            id = "docentes",
            pattern = "/docentes/",
            viewId = "/faces/pages/academico/buscarDocentes.xhtml"
    )
})
public class AdministrarDocentes implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private AdministrarLineasInvestigacionDocente administrarLineasInvestigacionDocente;
    @Inject
    private AdministrarDocentesCarrera administrarDocentesCarrera;
    @Inject
    private SessionDocente sessionDocente;
    @Inject
    private AdministrarUsuarios administrarUsuarios;

    @EJB
    private PersonaFacadeLocal personaFacadeLocal;
    @EJB
    private DocenteFacadeLocal docenteFacadeLocal;
    @EJB
    private ItemFacadeLocal itemFacadeLocal;
    @EJB
    private TituloDocenteFacadeLocal tituloDocenteFacadeLocal;
    @EJB
    private EstadoLaboralFacadeLocal estadoLaboralFacadeLocal;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    @EJB
    private LineaInvestigacionDocenteFacadeLocal lineaInvestigacionDocenteFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private DocenteCarreraFacadeLocal docenteCarreraFacadeLocal;
    @EJB
    private LineaInvestigacionCarreraFacadeLocal lineaInvestigacionCarreraFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private LineaInvestigacionFacadeLocal lineaInvestigacionFacadeLocal;
    @EJB
    private DirectorFacadeLocal directorFacadeLocal;
    @EJB
    private NacionalidadFacadeLocal nacionalidadFacadeLocal;
    @EJB
    private UsuarioCarreraFacadeLocal usuarioCarreraFacadeLocal;

    private List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos;
    private List<LineaInvestigacionDocente> lineaInvestigacionDocentesAgregados;
    private List<Docente> docentes;
    private DualListModel<Carrera> carrerasDualList;
    private List<DocenteCarrera> docenteCarrerasRemovidos;
    private DualListModel<LineaInvestigacion> lineasInvestigacionDualList;

    private String estadoLaboral;
    private String tituloDocente;
    private String criterio;
    private String tipoDocumento;
    private String genero;

    private boolean renderedNoEditar;
    private boolean renderedAdministrar;

    @PostConstruct
    public void init() {
        renderedAdministrar = false;
        this.carrerasDualList = new DualListModel<>();
        this.lineasInvestigacionDualList = new DualListModel<>();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public boolean renderedFotos(Docente docente) {
        boolean var = false;
        if (docente.getId() != null) {
            var = true;
        }
        return var;
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_docente");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_docente");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            sessionDocente.setRenderedBuscar(true);
        } else {
            sessionDocente.setRenderedBuscar(false);
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_docente");
            if (tienePermiso == 1) {
                Nacionalidad nacionalidad = nacionalidadFacadeLocal.find(1);
                sessionDocente.setDocente(new Docente());
                sessionDocente.setPersona(new Persona());
                sessionDocente.getPersona().setNacionalidadId(nacionalidad);
                listadoCarreras(new Docente());
                listadoLineasInvestigacion(new Docente());
                lineaInvestigacionDocentesRemovidos = new ArrayList<>();
                docenteCarrerasRemovidos = new ArrayList<>();
                lineaInvestigacionDocentesAgregados = new ArrayList<>();
                tipoDocumento = "";
                tituloDocente = "";
                estadoLaboral = "";
                genero = "";
                navegacion = "pretty:crearDocente";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(Docente docente) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_docente");
            if (tienePermiso == 1) {
                sessionDocente.setDocente(docente);
                sessionDocente.setPersona(personaFacadeLocal.find(docente.getId()));
                listadoCarreras(docente);
                listadoLineasInvestigacion(docente);
                tipoDocumento = itemFacadeLocal.find(sessionDocente.getPersona().getTipoDocumentoIdentificacionId()).toString();
                genero = itemFacadeLocal.find(sessionDocente.getPersona().getGeneroId()).toString();
                estadoLaboral = docente.getEstadoLaboralId().toString();
                tituloDocente = docente.getTituloDocenteId().toString();
                docenteCarrerasRemovidos = new ArrayList<>();
                lineaInvestigacionDocentesRemovidos = new ArrayList<>();
                lineaInvestigacionDocentesAgregados = new ArrayList<>();
                navegacion = "pretty:editarDocente";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public Docente getDocente(String docente) {
        int pos = docente.indexOf(":");
        Docente d = docenteFacadeLocal.find(Long.parseLong(docente.substring(0, pos)));
        return d;
    }

    public String grabar() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int posEstadoLaboral = estadoLaboral.indexOf(":");
            int posTitulo = tituloDocente.indexOf(":");
            EstadoLaboral el = estadoLaboralFacadeLocal.find(Long.parseLong(estadoLaboral.substring(0, posEstadoLaboral)));
            TituloDocente td = tituloDocenteFacadeLocal.find(Long.parseLong(tituloDocente.substring(0, posTitulo)));
            Item g = itemFacadeLocal.buscarPorCodigo(CatalogoEnum.GENERO.getTipo(), genero);
            Item tdi = itemFacadeLocal.buscarPorCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(), tipoDocumento);
            if (g != null) {
                sessionDocente.getPersona().setGeneroId(g.getId());
            }
            if (tdi != null) {
                sessionDocente.getPersona().setTipoDocumentoIdentificacionId(tdi.getId());
            }
            if (el != null) {
                sessionDocente.getDocente().setEstadoLaboralId(el);
            }
            if (td != null) {
                sessionDocente.getDocente().setTituloDocenteId(td);
            }
            if (sessionDocente.getDocente().getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_docente");
                if (tienePermiso == 1) {
                    if (personaFacadeLocal.esUnico(sessionDocente.getPersona().getNumeroIdentificacion(), sessionDocente.getPersona().getId())) {
                        personaFacadeLocal.create(sessionDocente.getPersona());
                        sessionDocente.getDocente().setId(sessionDocente.getPersona().getId());
                        docenteFacadeLocal.create(sessionDocente.getDocente());
                        administrarUsuarios.grabarUsuarioDocente(sessionDocente.getDocente());
                        logFacadeLocal.create(logFacadeLocal.crearLog("Docente", sessionDocente.getDocente().getId() + "", "CREAR", "|Nombres= "
                                + sessionDocente.getPersona().getNombres() + "|Apellidos= " + sessionDocente.getPersona().getApellidos()
                                + "|Cédula= " + sessionDocente.getPersona().getNumeroIdentificacion() + "|Email= " + sessionDocente.getPersona().getEmail(), sessionUsuario.getUsuario()));
                        grabarDocenteCarreras(sessionDocente.getDocente());
                        removerDocenteCarrera(sessionDocente.getDocente());
                        administrarLineasInvestigacionDocente.grabar(sessionDocente.getDocente(), sessionUsuario.getUsuario(), lineaInvestigacionDocentesAgregados);
                        administrarLineasInvestigacionDocente.eliminar(sessionDocente.getDocente(), sessionUsuario.getUsuario(), lineaInvestigacionDocentesRemovidos);
                        if (param.equalsIgnoreCase("grabar")) {
                            sessionDocente.setDocente(new Docente());
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            navegacion = "pretty:docentes";
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
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
                listadoDocentes("");
                return navegacion;
            }

            if (sessionDocente.getDocente().getId() != null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_docente");
                if (tienePermiso == 1) {
                    if (personaFacadeLocal.esUnico(sessionDocente.getPersona().getNumeroIdentificacion(), sessionDocente.getPersona().getId())) {
                        personaFacadeLocal.edit(sessionDocente.getPersona());
                        logFacadeLocal.create(logFacadeLocal.crearLog("Docente", sessionDocente.getDocente().getId() + "", "EDITAR", "NUEVO: |Nombres= "
                                + sessionDocente.getPersona().getNombres() + "|Apellidos= " + sessionDocente.getPersona().getApellidos()
                                + "|Cédula= " + sessionDocente.getPersona().getNumeroIdentificacion() + "|Email= " + sessionDocente.getPersona().getEmail(), sessionUsuario.getUsuario()));
                        grabarDocenteCarreras(sessionDocente.getDocente());
                        removerDocenteCarrera(sessionDocente.getDocente());
                        administrarLineasInvestigacionDocente.grabar(sessionDocente.getDocente(), sessionUsuario.getUsuario(), lineaInvestigacionDocentesAgregados);
                        administrarLineasInvestigacionDocente.eliminar(sessionDocente.getDocente(), sessionUsuario.getUsuario(), lineaInvestigacionDocentesRemovidos);
                        if (param.equalsIgnoreCase("grabar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            navegacion = "pretty:docentes";
                            sessionDocente.setDocente(new Docente());
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
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
                listadoDocentes("");
                return navegacion;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public List<Item> listadoTiposDocumentos() {
        try {
            return itemFacadeLocal.buscarPorCatalogo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<EstadoLaboral> listadoEstadosLaborales() {
        try {
            return estadoLaboralFacadeLocal.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<TituloDocente> listadoTitulos() {
        try {
            return tituloDocenteFacadeLocal.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Item> listadoGeneros() {
        try {
            return itemFacadeLocal.buscarPorCatalogo(CatalogoEnum.GENERO.getTipo());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void listadoDocentes(String criterio) {
        this.docentes = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_docente");
            if (tienePermiso == 1) {
                docentes = docenteFacadeLocal.buscarPorCriterio(criterio);

            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void grabarDocenteCarreras(Docente docente) {
        List<DocenteCarrera> docenteCarreras = new ArrayList<>();
        for (Object o : carrerasDualList.getTarget()) {
            int v = o.toString().indexOf(":");
            Integer id = Integer.parseInt(o.toString().substring(0, v));
            Carrera c = carreraFacadeLocal.find(id);
            DocenteCarrera dc = new DocenteCarrera();
            dc.setCarreraId(c);
            Long dcId = devuelveDocenteCarrera(docente.getDocenteCarreraList(), dc);
            dc = docenteCarreraFacadeLocal.find(dcId);
            if (dc == null) {
                dc = new DocenteCarrera();
                dc.setCarreraId(c);
                dc.setEsActivo(true);
                dc.setDocenteId(docente);
            }
            docenteCarreras.add(dc);
        }
        for (DocenteCarrera docenteCarrera : docenteCarreras) {
            if (contieneCarrera(docenteCarreraFacadeLocal.buscarPorDocente(docente.getId()), docenteCarrera) == false) {
                if (docenteCarrera.getId() == null) {
                    Director director = new Director();
//                    docenteCarrera.getDirector().setDocenteCarrera(docenteCarrera);
                    director.setEsActivo(true);
//                    Director director = docenteCarrera.getDirector();
                    docenteCarreraFacadeLocal.create(docenteCarrera);
                    logFacadeLocal.create(logFacadeLocal.crearLog("DocenteCarrera", docenteCarrera.getId() + "", "CREAR", "|Carrera=" + docenteCarrera.getCarreraId() + "|Docente=" + docenteCarrera.getDocenteId() + "|EsActivo= " + docenteCarrera.isEsActivo(), sessionUsuario.getUsuario()));
//                    director.setDocenteCarrera(docenteCarrera);
                    director.setId(docenteCarrera.getId());
                    directorFacadeLocal.create(director);
                } else {
                    Director director = directorFacadeLocal.find(docenteCarrera.getId());
                    director.setEsActivo(true);
                    docenteCarrera.setEsActivo(true);
                    docenteCarreraFacadeLocal.edit(docenteCarrera);
                    logFacadeLocal.create(logFacadeLocal.crearLog("DocenteCarrera", docenteCarrera.getId() + "", "EDITAR", "NUEVO: |Carrera=" + docenteCarrera.getCarreraId() + "|Docente=" + docenteCarrera.getDocenteId() + "|EsActivo= " + docenteCarrera.isEsActivo(), sessionUsuario.getUsuario()));

                }
            }
        }
    }

    public void listadoLineasInvestigacion(Docente docente) {
        List<LineaInvestigacion> lineaInvestigacionDocentes = new ArrayList<>();
        List<LineaInvestigacion> lineaInvestigaciones = new ArrayList<>();
        try {
            if (docente.getId() != null) {
                for (LineaInvestigacionDocente lid : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId())) {
                    lineaInvestigacionDocentes.add(lid.getLineaInvestigacionId());
                }
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    List<LineaInvestigacionCarrera> lics = new ArrayList<>();
                    lics = lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId());
                    for (LineaInvestigacionCarrera lic : lics) {
                        if (!lineaInvestigacionDocentes.contains(lic.getLineaInvestigacionId())) {
                            lineaInvestigaciones.add(lic.getLineaInvestigacionId());
                        }
                    }
                }

            } else {
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    List<LineaInvestigacionCarrera> lics = new ArrayList<>();
                    lics = lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId());
                    for (LineaInvestigacionCarrera lic : lics) {
                        lineaInvestigaciones.add(lic.getLineaInvestigacionId());
                    }
                }
            }
            lineasInvestigacionDualList = new DualListModel<>(lineaInvestigaciones, lineaInvestigacionDocentes);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listadoCarreras(Docente docente) {
        List<Carrera> docenteCarreras = new ArrayList<>();
        List<Carrera> carreras = new ArrayList<>();
        try {
            if (docente.getId() != null) {
                for (DocenteCarrera dc : docenteCarreraFacadeLocal.buscarPorDocente(docente.getId())) {
                    if (dc.isEsActivo()) {
                        docenteCarreras.add(dc.getCarreraId());
                    }
                }
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
                    if (!docenteCarreras.contains(carrera)) {
                        carreras.add(carrera);

                    }
                }
            } else {
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
                    carreras.add(carrera);
                }
            }
            carrerasDualList = new DualListModel<>(carreras, docenteCarreras);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public boolean contieneCarrera(List<DocenteCarrera> docenteCarreras, DocenteCarrera dc) {
        boolean var = false;
        for (DocenteCarrera docenteCarrera : docenteCarreras) {
            if (docenteCarrera.getCarreraId().equals(dc.getCarreraId()) && docenteCarrera.isEsActivo()) {
                var = true;
                break;
            }
        }
        return var;
    }

    public Long devuelveDocenteCarrera(List<DocenteCarrera> docenteCarreras, DocenteCarrera dc) {
        Long var = (long) 0;
        for (DocenteCarrera docenteCarrera : docenteCarreras) {
            if (docenteCarrera.getCarreraId().equals(dc.getCarreraId())) {
                var = docenteCarrera.getId();
                break;
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
                    lineaInvestigacionDocentesAgregados.remove(lid);
                    lineaInvestigacionDocentesRemovidos.add(lid);
                } else {
                    lineaInvestigacionDocentesAgregados.add(lid);
                    if (administrarLineasInvestigacionDocente.contieneLineaInvestigacion(lineaInvestigacionDocentesRemovidos, lid)) {
                        lineaInvestigacionDocentesRemovidos.remove(lid);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void transferDocenteCarrera(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Integer id = Integer.parseInt(item.toString().substring(0, v));
                Carrera c = carreraFacadeLocal.find(id);
                DocenteCarrera docenteCarrera = new DocenteCarrera();
                docenteCarrera.setCarreraId(c);
                if (event.isRemove()) {
                    docenteCarrerasRemovidos.add(docenteCarrera);
                } else {
                    docenteCarrerasRemovidos.remove(docenteCarrera);
                }
            }
        } catch (Exception e) {
        }
    }

    public void removerDocenteCarrera(Docente docente) {
        try {
            if (docente.getId() != null) {
                for (DocenteCarrera dc : docenteCarrerasRemovidos) {
                    Long id = devuelveDocenteCarrera(docente.getDocenteCarreraList(), dc);
                    DocenteCarrera docenteCarrera = null;
                    docenteCarrera = docenteCarreraFacadeLocal.find(id);
                    if (docenteCarrera != null) {
                        Director director = directorFacadeLocal.find(docenteCarrera.getId());
                        director.setEsActivo(false);
                        docenteCarrera.setEsActivo(false);
                        docenteCarreraFacadeLocal.edit(docenteCarrera);
                        directorFacadeLocal.edit(director);
                        logFacadeLocal.create(logFacadeLocal.crearLog("DocenteCarrera", docenteCarrera.getId() + "", "DESACTIVAR", "DESACTIVAR: |Carrera=" + docenteCarrera.getCarreraId() + "|Docente=" + docenteCarrera.getDocenteId() + "|Estado= " + docenteCarrera.isEsActivo(), sessionUsuario.getUsuario()));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES">

    public void sgaWebServicesDatosDocente() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                Map parametros = new HashMap();
                parametros.put("persona", sessionDocente.getPersona());
                parametros.put("docente", sessionDocente.getDocente());
                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 26).getValor();
                String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
                String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
                String s = serviceUrl + "?cedula=" + sessionDocente.getPersona().getNumeroIdentificacion();
                parametros.put("url", s);
                parametros.put("clave", passwordService);
                parametros.put("usuario", userService);
                parametros.put("msm_sincronizado", bundle.getString("lbl.sincronizado"));
                parametros.put("msm_no_sincronizado", bundle.getString("lbl.no_sincronizar_web_services"));
//                Map resultado = docenteConsumeService.getDatosDocente(parametros);
//                sessionDocente.setDocente((Docente) resultado.get("docente"));
//                sessionDocente.setPersona((Persona) resultado.get("persona"));
                tituloDocente = sessionDocente.getDocente().getTituloDocenteId().getTituloId().toString();
//                administrarDocentesCarrera.setTitulo(sessionDocente.getDocente().getTituloDocenteId().getTituloId().toString());
                estadoLaboral = sessionDocente.getDocente().getEstadoLaboralId().toString();
//                administrarDocentesCarrera.setEstadoLaboral(sessionDocente.getDocente().getEstadoLaboralId().toString());
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionDocente getSessionDocente() {
        return sessionDocente;
    }

    public void setSessionDocente(SessionDocente sessionDocente) {
        this.sessionDocente = sessionDocente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public String getEstadoLaboral() {
        return estadoLaboral;
    }

    public void setEstadoLaboral(String estadoLaboral) {
        this.estadoLaboral = estadoLaboral;
    }

    public DualListModel<Carrera> getCarrerasDualList() {
        return carrerasDualList;
    }

    public void setCarrerasDualList(DualListModel<Carrera> carrerasDualList) {
        this.carrerasDualList = carrerasDualList;
    }

    public List<DocenteCarrera> getDocenteCarrerasRemovidos() {
        return docenteCarrerasRemovidos;
    }

    public void setDocenteCarrerasRemovidos(List<DocenteCarrera> docenteCarrerasRemovidos) {
        this.docenteCarrerasRemovidos = docenteCarrerasRemovidos;
    }

    public DualListModel<LineaInvestigacion> getLineasInvestigacionDualList() {
        return lineasInvestigacionDualList;
    }

    public void setLineasInvestigacionDualList(DualListModel<LineaInvestigacion> lineasInvestigacionDualList) {
        this.lineasInvestigacionDualList = lineasInvestigacionDualList;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }

    public void setDocentes(List<Docente> docentes) {
        this.docentes = docentes;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedAdministrar() {
        return renderedAdministrar;
    }

    public void setRenderedAdministrar(boolean renderedAdministrar) {
        this.renderedAdministrar = renderedAdministrar;
    }

    public AdministrarLineasInvestigacionDocente getAdministrarLineasInvestigacionDocente() {
        return administrarLineasInvestigacionDocente;
    }

    public void setAdministrarLineasInvestigacionDocente(AdministrarLineasInvestigacionDocente administrarLineasInvestigacionDocente) {
        this.administrarLineasInvestigacionDocente = administrarLineasInvestigacionDocente;
    }

    public List<LineaInvestigacionDocente> getLineaInvestigacionDocentesRemovidos() {
        return lineaInvestigacionDocentesRemovidos;
    }

    public void setLineaInvestigacionDocentesRemovidos(List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos) {
        this.lineaInvestigacionDocentesRemovidos = lineaInvestigacionDocentesRemovidos;
    }

    public List<LineaInvestigacionDocente> getLineaInvestigacionDocentesAgregados() {
        return lineaInvestigacionDocentesAgregados;
    }

    public void setLineaInvestigacionDocentesAgregados(List<LineaInvestigacionDocente> lineaInvestigacionDocentesAgregados) {
        this.lineaInvestigacionDocentesAgregados = lineaInvestigacionDocentesAgregados;
    }

    public String getTituloDocente() {
        return tituloDocente;
    }

    public void setTituloDocente(String tituloDocente) {
        this.tituloDocente = tituloDocente;
    }

    public AdministrarDocentesCarrera getAdministrarDocentesCarrera() {
        return administrarDocentesCarrera;
    }

    public void setAdministrarDocentesCarrera(AdministrarDocentesCarrera administrarDocentesCarrera) {
        this.administrarDocentesCarrera = administrarDocentesCarrera;
    }

//</editor-fold>
}

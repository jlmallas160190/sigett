/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.configuracionCarrera;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import java.util.List;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.httpClient.ConexionDTO;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarConfiguracionCarrera",
            pattern = "/editarConfiguracionCarrera/#{sessionConfiguracionCarrera.configuracionCarrera.id}",
            viewId = "/faces/pages/sigett/editarConfiguracionCarrera.xhtml"
    ),
    @URLMapping(
            id = "configuracionesCarrera",
            pattern = "/configuracionesCarrera/",
            viewId = "/faces/pages/sigett/configuracionesCarrera/index.xhtml"
    )})
public class AdministrarConfiguracionCarrera implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE MANAGED BEANS">
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionConfiguracionCarrera sessionConfiguracionCarrera;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE SERVICIOS">
    @EJB
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private OfertaAcademicaService ofertaAcademicaFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    //</editor-fold>

    public AdministrarConfiguracionCarrera() {
    }

    public void init() {
        renderedEditar();
        this.buscar();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_configuracion_carrera");
            if (tienePermiso == 1) {
                sessionConfiguracionCarrera.setConfiguracionCarrera(new ConfiguracionCarrera());
                navegacion = "pretty:crearConfiguracionCarrera";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void buscar() {
        sessionConfiguracionCarrera.getConfiguracionCarreras().clear();
        ConfiguracionCarrera configuracionCarreraBuscar = new ConfiguracionCarrera();
        configuracionCarreraBuscar.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId());
        List<ConfiguracionCarrera> configuracionCarreras = configuracionCarreraService.buscar(configuracionCarreraBuscar);
        if (configuracionCarreras == null) {
            return;
        }
        sessionConfiguracionCarrera.setConfiguracionCarreras(configuracionCarreras);
    }

    public String editar(ConfiguracionCarrera configuracionCarrera) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_configuracion_carrera");
            if (tienePermiso == 1) {
                sessionConfiguracionCarrera.setConfiguracionCarrera(configuracionCarrera);
                navegacion = "pretty:editarConfiguracionCarrera";
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

    public String grabar(ConfiguracionCarrera configuracionCarrera) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//            configuracionCarrera.setCarreraId(sessionUsuarioCarrera.getUsuarioCarrera().getCarreraId());
            if (configuracionCarrera.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_configuracion_carrera");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        configuracionCarreraService.guardar(configuracionCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "CREAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                        sessionConfiguracionCarrera.setConfiguracionCarrera(new ConfiguracionCarrera());
                        navegacion = "pretty:editarUsuarioCarrera";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            configuracionCarreraService.guardar(configuracionCarrera);
                            logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "CREAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.configuracion") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                configuracionCarreraService.guardar(configuracionCarrera);
                                logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "CREAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                                sessionConfiguracionCarrera.setConfiguracionCarrera(new ConfiguracionCarrera());
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_configuracion_carrera");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        configuracionCarreraService.actualizar(configuracionCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "EDITAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                        sessionConfiguracionCarrera.setConfiguracionCarrera(new ConfiguracionCarrera());
                        navegacion = "pretty:editarUsuarioCarrera";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            configuracionCarreraService.actualizar(configuracionCarrera);
                            logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "EDITAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.configuracion") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                configuracionCarreraService.actualizar(configuracionCarrera);
                                logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "EDITAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                                sessionConfiguracionCarrera.setConfiguracionCarrera(new ConfiguracionCarrera());
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void seleccionaOfertaAcademicaActual(ConfiguracionCarrera configuracionCarrera) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (configuracionCarrera.getId() != null) {
                for (OfertaAcademica ofertaAcademica : ofertaAcademicaFacadeLocal.buscarPorPeriodoActual()) {
                    sgaWebServicesModulosCarrera(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), ofertaAcademica);
                    if (sessionConfiguracionCarrera.isTieneModulos()) {
                        configuracionCarrera.setValor(ofertaAcademica.getIdSga() + "");
                        configuracionCarrera.setObservacion(ofertaAcademica.getNombre());
                        configuracionCarreraService.actualizar(configuracionCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "EDITAR", "|Nombre= "
                                + configuracionCarrera.getNombre() + "|Codigo= " + configuracionCarrera.getCodigo() + "|Valor= "
                                + configuracionCarrera.getValor() + "Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                        break;
                    }
                }
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.oferta_academica") + " " + bundle.getString("lbl.seleccionada"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedEditar() {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_configuracion_carrera");
        if (tienePermiso == 1) {
            sessionConfiguracionCarrera.setRenderedEditar(true);
        } else {
            sessionConfiguracionCarrera.setRenderedEditar(false);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES CONSUMES">
    public void sgaWebServicesModulosCarrera(Carrera carrera, OfertaAcademica ofertaAcademica) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_modulos_carrera") == 1) {
            try {
                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 27).getValor();
                String s = serviceUrl + "?id_oferta=" + ofertaAcademica.getIdSga() + ";id_carrera=" + carrera.getIdSga();
                ConexionDTO seguridad = new ConexionDTO(configuracionGeneralFacadeLocal.find((int) 5).getValor(),
                        s, configuracionGeneralFacadeLocal.find((int) 6).getValor());
                NetClientServiceImplement conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    sessionConfiguracionCarrera.setTieneModulos(false);
                    parserModulosCarrera(datos);
                }
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, bundle.getString("lbl.no_sincronizar_web_services"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private void parserModulosCarrera(JsonElement elemento) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    try {
                        if (!sessionConfiguracionCarrera.isTieneModulos()) {
                            String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                            JsonParser jp = new JsonParser();
                            JsonElement jsonElement = jp.parse(e);
                            parserModulosCarrera(jsonElement);
                        }
                    } catch (Exception e) {
                        parserModulosCarrera(entrada.getValue());
                    }
                }
            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                if (array.size() > 2) {
                    sessionConfiguracionCarrera.setTieneModulos(true);
                } else {
                    sessionConfiguracionCarrera.setTieneModulos(false);
                }
            } else if (elemento.isJsonPrimitive()) {

            }
        } catch (Exception e) {
            System.out.println(e);
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

    public SessionConfiguracionCarrera getSessionConfiguracionCarrera() {
        return sessionConfiguracionCarrera;
    }

    public void setSessionConfiguracionCarrera(SessionConfiguracionCarrera sessionConfiguracionCarrera) {
        this.sessionConfiguracionCarrera = sessionConfiguracionCarrera;
    }

    public SessionUsuarioCarrera getSessionUsuarioCarrera() {
        return sessionUsuarioCarrera;
    }

    public void setSessionUsuarioCarrera(SessionUsuarioCarrera sessionUsuarioCarrera) {
        this.sessionUsuarioCarrera = sessionUsuarioCarrera;
    }

//</editor-fold>
}

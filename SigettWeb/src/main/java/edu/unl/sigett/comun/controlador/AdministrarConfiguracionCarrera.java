/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.managed.session.SessionConfiguracionCarrera;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuarioCarrera;
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
import edu.unl.sigett.session.ConfiguracionCarreraFacadeLocal;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import edu.jlmallas.academico.service.OfertaAcademicaFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

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
            id = "crearConfiguracionCarrera",
            pattern = "/crearConfiguracionCarrera/",
            viewId = "/faces/pages/sigett/editarConfiguracionCarrera.xhtml"
    ),
    @URLMapping(
            id = "configuracionesCarrera",
            pattern = "/configuracionesCarrera/",
            viewId = "/faces/pages/sigett/buscarConfiguracionesCarrera.xhtml"
    )})
public class AdministrarConfiguracionCarrera implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionConfiguracionCarrera sessionConfiguracionCarrera;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @EJB
    private ConfiguracionCarreraFacadeLocal configuracionCarreraFacadeLocal;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    @EJB
    private OfertaAcademicaFacadeLocal ofertaAcademicaFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;

    private boolean tieneModulos = false;
    private boolean renderedNoEditar;

    public AdministrarConfiguracionCarrera() {
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
            configuracionCarrera.setCarreraId(sessionUsuarioCarrera.getUsuarioCarrera().getCarreraId());
            if (configuracionCarrera.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_configuracion_carrera");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        configuracionCarreraFacadeLocal.create(configuracionCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "CREAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                        sessionConfiguracionCarrera.setConfiguracionCarrera(new ConfiguracionCarrera());
                        navegacion = "pretty:editarUsuarioCarrera";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            configuracionCarreraFacadeLocal.create(configuracionCarrera);
                            logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "CREAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.configuracion") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                configuracionCarreraFacadeLocal.create(configuracionCarrera);
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
                        configuracionCarreraFacadeLocal.edit(configuracionCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "EDITAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                        sessionConfiguracionCarrera.setConfiguracionCarrera(new ConfiguracionCarrera());
                        navegacion = "pretty:editarUsuarioCarrera";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            configuracionCarreraFacadeLocal.edit(configuracionCarrera);
                            logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "EDITAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Valor= " + configuracionCarrera.getValor() + "|Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.configuracion") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                configuracionCarreraFacadeLocal.edit(configuracionCarrera);
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
                    if (sgaWebServicesModulosCarrera(sessionUsuarioCarrera.getCarrera(), ofertaAcademica)) {
                        configuracionCarrera.setValor(ofertaAcademica.getIdSga() + "");
                        configuracionCarrera.setObservacion(ofertaAcademica.getNombre());
                        configuracionCarreraFacadeLocal.edit(configuracionCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionCarrera", configuracionCarrera.getId() + "", "EDITAR", "|Nombre= " + configuracionCarrera.getNombre() + "|Codigo= " + configuracionCarrera.getCodigo() + "|Valor= " + configuracionCarrera.getValor() + "Carrera= " + configuracionCarrera.getCarreraId(), sessionUsuario.getUsuario()));
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

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_configuracion_carrera");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_configuracion_carrera");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES CONSUMES">
    public boolean sgaWebServicesModulosCarrera(Carrera carrera, OfertaAcademica ofertaAcademica) {
        boolean actual = false;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_modulos_carrera") == 1) {
            try {
//                ConexionServicio conexionServicio = new ConexionServicio();
//                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 27).getValor();
//                String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//                String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//                String s = serviceUrl + "?id_oferta=" + ofertaAcademica.getIdSga() + ";id_carrera=" + carrera.getIdSga();
//                String datosJson = conexionServicio.conectar(s, userService, passwordService);
//                if (!datosJson.equalsIgnoreCase("")) {
//                    JsonParser parser = new JsonParser();
//                    JsonElement datos = parser.parse(datosJson);
//                    tieneModulos = false;
//                    actual = recorrerElementosJson(datos);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, bundle.getString("lbl.no_sincronizar_web_services"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return actual;
    }

    private boolean recorrerElementosJson(JsonElement elemento) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    try {
                        if (tieneModulos == false) {
                            String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                            JsonParser jp = new JsonParser();
                            JsonElement jsonElement = jp.parse(e);
                            recorrerElementosJson(jsonElement);
                        }
                    } catch (Exception e) {
                        recorrerElementosJson(entrada.getValue());
                    }
                }
            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                if (array.size() > 2) {
                    tieneModulos = true;
                } else {
                    tieneModulos = false;
                }
            } else if (elemento.isJsonPrimitive()) {

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return tieneModulos;
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

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

//</editor-fold>
}

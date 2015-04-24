/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionConfiguracion;
import edu.unl.sigett.entity.ConfiguracionGeneral;
import com.jlmallas.soporte.entity.Objeto;
import com.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import com.jlmallas.soporte.session.ExcepcionFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import com.jlmallas.soporte.session.ProyectoSoftwareFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarConfiguracion",
            pattern = "/editarConfiguracion/#{sessionConfiguracion.configuracionGeneral.id}",
            viewId = "/faces/pages/comun/editarConfiguracion.xhtml"
    ),
    @URLMapping(
            id = "crearConfiguracion",
            pattern = "/crearConfiguracion/",
            viewId = "/faces/pages/comun/editarConfiguracion.xhtml"
    ),
    @URLMapping(
            id = "configuraciones",
            pattern = "/configuraciones/",
            viewId = "/faces/pages/comun/buscarConfiguraciones.xhtml"
    )})
public class AdministrarConfiguraciones implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionConfiguracion sessionConfiguracion;

    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    @EJB
    private ExcepcionFacadeLocal excepcionFacadeLocal;
    @EJB
    private ObjetoFacadeLocal objetoFacadeLocal;
    @EJB
    private ProyectoSoftwareFacadeLocal proyectoSoftwareFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    private List<ConfiguracionGeneral> configuracionGenerals;

    private boolean renderedNoEditar;
    private String criterio;

    public AdministrarConfiguraciones() {
    }
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear(Usuario usuario) {
        String navagacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_configuracion_general");
            if (tienePermiso == 1) {
                sessionConfiguracion.setConfiguracionGeneral(new ConfiguracionGeneral());
                navagacion = "pretty:crearConfiguracion";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al crear Configuracion";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Configuracion");
                if (obj == null) {
                    obj = new Objeto(null, "Configuracion", "Configuracion");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return navagacion;
    }

    public String timestamp(Date fecha) {
        try {
            return configuracionGeneralFacadeLocal.timestampFormat(fecha);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String date(Date fecha) {
        try {
            return configuracionGeneralFacadeLocal.dateFormat(fecha);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String time(Date fecha) {
        try {
            return configuracionGeneralFacadeLocal.timeFormat(fecha);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public int intervaloActualizaciones() {
        int intervalo = 100;
        try {
            intervalo = Integer.parseInt(configuracionGeneralFacadeLocal.find(32).getValor());
        } catch (Exception e) {
        }
        return intervalo;
    }

    public String editar(ConfiguracionGeneral configuracionGeneral, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_configuracion_general");
            if (tienePermiso == 1) {
                sessionConfiguracion.setConfiguracionGeneral(configuracionGeneral);
                navegacion = "pretty:editarConfiguracion";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al editar Configuracion";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Configuracion");
                if (obj == null) {
                    obj = new Objeto(null, "Configuracion", "Configuracion");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return navegacion;
    }

    public String grabar(ConfiguracionGeneral configuracionGeneral) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (configuracionGeneral.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_configuracion_general");
                if (tienePermiso == 1) {
                    if (configuracionGeneral.getTipo().equalsIgnoreCase("radio")) {
                        if (configuracionGeneral.isRadio()) {
                            configuracionGeneral.setValor("SI");
                        } else {
                            configuracionGeneral.setValor("NO");
                        }
                    }
                    configuracionGeneralFacadeLocal.create(configuracionGeneral);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionGeneral", configuracionGeneral.getId() + "", "CREAR", "|Nombre= " + configuracionGeneral.getNombre() + "|Valor= " + configuracionGeneral.getValor(), sessionUsuario.getUsuario()));
                    if (param.equalsIgnoreCase("guardar")) {
                        navegacion = "pretty:configuraciones";
                        sessionConfiguracion.setConfiguracionGeneral(new ConfiguracionGeneral());
                    } else {
                        if (param.equalsIgnoreCase("guardar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            sessionConfiguracion.setConfiguracionGeneral(new ConfiguracionGeneral());
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_configuracion_general");
                if (tienePermiso == 1) {
                    if (configuracionGeneral.getTipo().equalsIgnoreCase("radio")) {
                        if (configuracionGeneral.isRadio()) {
                            configuracionGeneral.setValor("SI");
                        } else {
                            configuracionGeneral.setValor("No");
                        }
                    }
                    configuracionGeneralFacadeLocal.edit(configuracionGeneral);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionGeneral", configuracionGeneral.getId() + "", "EDITAR", "|Nombre= " + configuracionGeneral.getNombre() + "|Valor= " + configuracionGeneral.getValor(), sessionUsuario.getUsuario()));
                    if (param.equalsIgnoreCase("guardar")) {
                        navegacion = "pretty:configuraciones";
                        sessionConfiguracion.setConfiguracionGeneral(new ConfiguracionGeneral());
                    } else {
                        if (param.equalsIgnoreCase("guardar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            sessionConfiguracion.setConfiguracionGeneral(new ConfiguracionGeneral());
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        } catch (Exception e) {
            String mensaje = "Error en grabar Configuracion";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Configuracion");
                if (obj == null) {
                    obj = new Objeto(null, "Configuracion", "Configuracion");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return navegacion;
    }

    public void buscar(String criterio) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_configuracion_general");
            if (tienePermiso == 1) {
                this.configuracionGenerals = new ArrayList<>();
                for (ConfiguracionGeneral configuracionGeneral : configuracionGeneralFacadeLocal.buscarPorNombre(criterio)) {
                    if (configuracionGeneral.getTipo().equalsIgnoreCase("radio")) {
                        if (configuracionGeneral.getValor().equalsIgnoreCase("SI")) {
                            configuracionGeneral.setRadio(true);
                        } else {
                            configuracionGeneral.setRadio(false);
                        }
                    }
                    configuracionGenerals.add(configuracionGeneral);
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al buscar Configuraciones";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Configuracion");
                if (obj == null) {
                    obj = new Objeto(null, "Configuracion", "Configuracion");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
    }

    public long getTamanioPermitidoArchivos() {
        long tamanioMax = 0;
        try {
            tamanioMax = Long.parseLong(configuracionGeneralFacadeLocal.find((int) 8).getValor());
        } catch (Exception e) {
        }
        return tamanioMax;
    }

    public long getTamanioPermitidoFotos() {
        long tamanioMax = 0;
        try {
            tamanioMax = Long.parseLong(configuracionGeneralFacadeLocal.find((int) 11).getValor());
        } catch (Exception e) {
        }
        return tamanioMax;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_configuracion_general");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_configuracion_general");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionConfiguracion getSessionConfiguracion() {
        return sessionConfiguracion;
    }

    public void setSessionConfiguracion(SessionConfiguracion sessionConfiguracion) {
        this.sessionConfiguracion = sessionConfiguracion;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<ConfiguracionGeneral> getConfiguracionGenerals() {
        return configuracionGenerals;
    }

    public void setConfiguracionGenerals(List<ConfiguracionGeneral> configuracionGenerals) {
        this.configuracionGenerals = configuracionGenerals;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }
//</editor-fold>
}

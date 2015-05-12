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
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import com.jlmallas.soporte.session.ExcepcionFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import com.jlmallas.soporte.session.ProyectoSoftwareFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;

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
            viewId = "/faces/pages/comun/configuraciones/index.xhtml"
    )})
public class AdministrarConfiguraciones implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionConfiguracion sessionConfiguracion;

    @EJB
    private ConfiguracionGeneralDao configuracionGeneralDao;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private ExcepcionFacadeLocal excepcionFacadeLocal;
    @EJB
    private ObjetoFacadeLocal objetoFacadeLocal;
    @EJB
    private ProyectoSoftwareFacadeLocal proyectoSoftwareFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;

    public void init() {
        this.renderedEditar();
        this.buscar();
    }

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
            return configuracionGeneralDao.timestampFormat(fecha);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String date(Date fecha) {
        try {
            return configuracionGeneralDao.dateFormat(fecha);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String time(Date fecha) {
        try {
            return configuracionGeneralDao.timeFormat(fecha);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void intervaloActualizaciones() {
        try {
            sessionConfiguracion.setTamanioPermitidoImagenes(Long.parseLong(configuracionGeneralDao.find((int) 32).getValor()));
        } catch (Exception e) {
        }
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
                    configuracionGeneralDao.create(configuracionGeneral);
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
                    configuracionGeneralDao.edit(configuracionGeneral);
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

    public void buscar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_configuracion_general");
            if (tienePermiso == 1) {
                sessionConfiguracion.getConfiguracionesGenerales().clear();
                for (ConfiguracionGeneral configuracionGeneral : configuracionGeneralDao.buscar(new ConfiguracionGeneral())) {
                    if (configuracionGeneral.getTipo().equalsIgnoreCase("radio")) {
                        if (configuracionGeneral.getValor().equalsIgnoreCase("SI")) {
                            configuracionGeneral.setRadio(true);
                        } else {
                            configuracionGeneral.setRadio(false);
                        }
                    }
                    sessionConfiguracion.getConfiguracionesGenerales().add(configuracionGeneral);
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

    public void obtenerTamanioPermitidoArchivos() {
        try {
            sessionConfiguracion.setTamanioPermitidoImagenes(Long.parseLong(configuracionGeneralDao.find((int) 8).getValor()));
        } catch (Exception e) {
        }
    }

    public void obtenerTamanioPermitidoFotos() {
        try {
            sessionConfiguracion.setTamanioPermitidoImagenes(Long.parseLong(configuracionGeneralDao.find((int) 11).getValor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    private void renderedEditar() {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_configuracion_general");
        if (tienePermiso == 1) {
            sessionConfiguracion.setRenderedEditar(true);
        } else {
            sessionConfiguracion.setRenderedEditar(false);
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

    public SessionConfiguracion getSessionConfiguracion() {
        return sessionConfiguracion;
    }

    public void setSessionConfiguracion(SessionConfiguracion sessionConfiguracion) {
        this.sessionConfiguracion = sessionConfiguracion;
    }

//</editor-fold>
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import edu.unl.sigett.autor.manged.session.SessionAutorProyecto;
import edu.unl.sigett.comun.managed.session.SessionExpediente;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.Expediente;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarExpedientes implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionAutorProyecto sessionAutorProyecto;
    @Inject
    private SessionExpediente sessionExpediente;
    private boolean esEditado;
    private boolean renderedNoEditar;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    private List<Expediente> expedientes;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedEliminar;
    private boolean renderedBuscar;

    public AdministrarExpedientes() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_expediente");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_expediente");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
            renderedEditar = false;
        }
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_expediente");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedEliminar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_expediente");
        if (tienePermiso == 1) {
            renderedEliminar = true;
        } else {
            renderedEliminar = false;
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_expediente");
            if (tienePermiso == 1) {
                if (param.equalsIgnoreCase("crear-dlg")) {
                    esEditado = false;
                    sessionExpediente.setExpediente(new Expediente());
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarExpediente').show()");
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(Expediente expediente) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_expediente");
            if (tienePermiso == 1) {
                if (param.equalsIgnoreCase("editar-dlg")) {
                    esEditado = true;
                    sessionExpediente.setExpediente(expediente);
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarExpediente').show()");
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void buscarExpedientes(AutorProyecto autorProyecto) {
        expedientes = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_expediente");
            if (tienePermiso == 1) {
                for (Expediente expediente : autorProyecto.getExpedienteList()) {
                    if (expediente.getEsActivo()) {
                        if (!expedientes.contains(expediente)) {
                            expedientes.add(expediente);
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String agregar(Expediente expediente) {
        String navegacion = "";
        Calendar fecha = Calendar.getInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (esEditado == false) {
                sessionAutorProyecto.getAutorProyecto().getExpedienteList().add(expediente);
                expediente.setAutorProyectoId(sessionAutorProyecto.getAutorProyecto());
                expediente.setFecha(fecha.getTime());
                expediente.setEsActivo(true);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                if (param.equalsIgnoreCase("agregar")) {
                    navegacion = "pretty:editarExpediente";
                } else {
                    if (param.equalsIgnoreCase("agregar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarExpediente').hide()");
                    }
                }
                buscarExpedientes(sessionAutorProyecto.getAutorProyecto());
            } else {
                if (param.equalsIgnoreCase("agregar")) {
                    navegacion = "editarExpediente?faces-redirect=true";
                } else {
                    if (param.equalsIgnoreCase("agregar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarExpediente').hide()");
                    }
                }
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public void remover(Expediente expediente) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_expediente");
            if (tienePermiso == 1) {
                expediente.setEsActivo(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                buscarExpedientes(sessionAutorProyecto.getAutorProyecto());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
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

    public SessionAutorProyecto getSessionAutorProyecto() {
        return sessionAutorProyecto;
    }

    public void setSessionAutorProyecto(SessionAutorProyecto sessionAutorProyecto) {
        this.sessionAutorProyecto = sessionAutorProyecto;
    }

    public SessionExpediente getSessionExpediente() {
        return sessionExpediente;
    }

    public void setSessionExpediente(SessionExpediente sessionExpediente) {
        this.sessionExpediente = sessionExpediente;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<Expediente> getExpedientes() {
        return expedientes;
    }

    public void setExpedientes(List<Expediente> expedientes) {
        this.expedientes = expedientes;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }
}
//</editor-fold>


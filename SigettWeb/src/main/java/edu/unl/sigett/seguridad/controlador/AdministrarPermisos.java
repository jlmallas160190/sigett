/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import edu.unl.sigett.seguridad.managed.session.SessionPermiso;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import com.jlmallas.soporte.entity.Objeto;
import com.jlmallas.seguridad.entity.Permiso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import com.jlmallas.soporte.session.ExcepcionFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import com.jlmallas.seguridad.session.PermisoFacadeLocal;
import com.jlmallas.soporte.session.ProyectoSoftwareFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarPermisos implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @EJB
    private PermisoFacadeLocal permisoFacadeLocal;
    @Inject
    private SessionPermiso sessionPermiso;
    private String criterio;
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

    public AdministrarPermisos() {
    }

    public String crear() {
        String navegacion = "";
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "");
        if (tienePermiso == 1) {
            sessionPermiso.setPermiso(new Permiso());
            navegacion = "editarPermiso?faces-redirect=true";
        } else {
            if (tienePermiso == 2) {
                navegacion = "";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para crear Permiso...", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                navegacion = "loginAdmin?faces-redirect=true";
            }
        }
        return navegacion;
    }

    public String editar(Permiso permiso) {
        String navegacion = "";
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "");
        if (tienePermiso == 1) {
            sessionPermiso.setPermiso(permiso);
            logFacadeLocal.create(logFacadeLocal.crearLog("Permiso", permiso.getId() + "", "SELECT", "SELECT: |Nombre= " + permiso.getNombre(), sessionUsuario.getUsuario()));
            navegacion = "editarPermiso?faces-redirect=true";
        } else {
            if (tienePermiso == 2) {
                navegacion = "";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para editar Permiso...", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                navegacion = "loginAdmin?faces-redirect=true";
            }
        }
        return navegacion;
    }

    public List<Permiso> listadoPermisos() {
        List<Permiso> permisos = new ArrayList<>();
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "");
            if (tienePermiso == 1) {
                permisos = permisoFacadeLocal.buscarPorNombre(criterio);
            } else {
                if (tienePermiso == 2) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("admin.xhtml");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para listar Permisos...", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("loginAdmin.xhtml");
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al buscar Permisos";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Permiso");
                if (obj == null) {
                    obj = new Objeto(null, "Permiso", "Permiso");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return permisos;
    }

    public String grabar(Permiso permiso) {

        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (permiso.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "");
                if (tienePermiso == 1) {
                    permisoFacadeLocal.create(permiso);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Permiso", permiso.getId() + "", "CREAR", "Nuevo: |Nombre= " + permiso.getNombre(), sessionUsuario.getUsuario()));
                    sessionPermiso.setPermiso(new Permiso());
                    if (param.equalsIgnoreCase("guardar")) {
                        navegacion = "buscarPermisos?faces-redirect=true";
                    } else {
                        crear();
                    }
                } else {
                    if (tienePermiso == 2) {
                        navegacion = "";
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para guardar permiso..", "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "loginAdmin?faces-redirect=true";
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "");
                if (tienePermiso == 1) {
                    permisoFacadeLocal.edit(permiso);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Permiso", permiso.getId() + "", "ACTUALIZAR", "Nuevo: |Nombre= " + permiso.getNombre(), sessionUsuario.getUsuario()));
                    sessionPermiso.setPermiso(new Permiso());
                    if (param.equalsIgnoreCase("guardar")) {
                        navegacion = "buscarPermisos?faces-redirect=true";
                    } else {
                        crear();
                    }
                } else {
                    if (tienePermiso == 2) {
                        navegacion = "";
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para editar permiso..", "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "loginAdmin?faces-redirect=true";
                    }
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al grabar Permiso";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Permiso");
                if (obj == null) {
                    obj = new Objeto(null, "Permiso", "Permiso");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return navegacion;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionPermiso getSessionPermiso() {
        return sessionPermiso;
    }

    public void setSessionPermiso(SessionPermiso sessionPermiso) {
        this.sessionPermiso = sessionPermiso;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lineaInvestigacionCarrera;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import org.jlmallas.seguridad.entity.Usuario;
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
import edu.unl.sigett.dao.LineaInvestigacionCarreraDao;
import edu.unl.sigett.dao.LineaInvestigacionDao;
import edu.unl.sigett.service.LineaInvestigacionService;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarLineaInvestigacionCarrera",
            pattern = "/editarLineaInvestigacionCarrera/#{sessionLineaInvestigacionCarrera.lineaInvestigacionCarrera.id}",
            viewId = "/faces/pages/sigett/lineasInvestigacionCarrera/editarLineaInvestigacionCarrera.xhtml"
    ),
    @URLMapping(
            id = "crearLineaInvestigacionCarrera",
            pattern = "/crearLineaInvestigacionCarera/",
            viewId = "/faces/pages/sigett/lineasInvestigacionCarrera/editarLineaInvestigacionCarrera.xhtml"
    ),
    @URLMapping(
            id = "lineasInvestigacionCarrera",
            pattern = "/lineasInvestigacionCarrera/",
            viewId = "/faces/pages/sigett/lineasInvestigacionCarrera/index.xhtml"
    )})
public class AdministrarLineasInvestigacionCarrera implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE MANAGED BEANS">
    @Inject
    private SessionLineaInvestigacionCarrera sessionLineaInvestigacionCarrera;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE SERVICIOS">
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logFacadeLocal;
    @EJB(lookup = "java:global/SigettService/LineaInvestigacionCarreraDaoImplement!edu.unl.sigett.dao.LineaInvestigacionCarreraDao")
    private LineaInvestigacionCarreraDao lineaInvestigacionCarreraDao;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/SigettService/LineaInvestigacionServiceImplement!edu.unl.sigett.service.LineaInvestigacionService")
    private LineaInvestigacionService lineaInvestigacionService;
//</editor-fold>

    public AdministrarLineasInvestigacionCarrera() {
    }

    public void init() {
        this.buscar();
        this.renderedCrear();
        this.renderedEditar();
        this.renderedEliminar();
    }
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    /**
     * CREAR LÍNEA DE INVESTIGACIÓN CARRERA
     *
     * @return
     */
    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "crear_linea_investigacion_carrera");
            if (tienePermiso == 1) {
                sessionLineaInvestigacionCarrera.setLineaInvestigacionCarrera(new LineaInvestigacionCarrera(new LineaInvestigacion(),
                        sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId()));
                navegacion = "pretty:crearLineaInvestigacionCarrera";
            } else {
                if (tienePermiso == 2) {
                    navegacion = "";
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

    public String editar(LineaInvestigacionCarrera lineaInvestigacionCarrera) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion_carrera");
            if (tienePermiso == 1) {
                sessionLineaInvestigacionCarrera.setLineaInvestigacionCarrera(lineaInvestigacionCarrera);
                navegacion = "pretty:editarLineaInvestigacionCarrera";
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

    public void eliminar(LineaInvestigacion lineaInvestigacion) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "eliminar_linea_investigacion_carrera");
            if (tienePermiso == 1) {
                if (lineaInvestigacion.getEsActivo()) {
                    lineaInvestigacion.setEsActivo(false);
                    logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "DESACTIVAR",
                            "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                } else {
                    lineaInvestigacion.setEsActivo(true);
                    logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "ACTIVAR", "|Nombre= "
                            + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                }
                lineaInvestigacionService.actualizar(lineaInvestigacion);
                return;
            }
            if (tienePermiso == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }
        } catch (Exception e) {
        }
    }

    public void buscar() {
        this.sessionLineaInvestigacionCarrera.getLineaInvestigacionCarreras().clear();
        this.sessionLineaInvestigacionCarrera.getFilterLineasInvestigacionCarreras().clear();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");

            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "buscar_linea_investigacion_carrera");
            if (tienePermiso == 1) {
                this.sessionLineaInvestigacionCarrera.setLineaInvestigacionCarreras(lineaInvestigacionCarreraDao.buscar(
                        new LineaInvestigacionCarrera(null, sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId())));
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);

                }
            }
            sessionLineaInvestigacionCarrera.setFilterLineasInvestigacionCarreras(sessionLineaInvestigacionCarrera.getLineaInvestigacionCarreras());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LineaInvestigacionCarrera> buscarByCarrera(Carrera carrera) {
        try {
//            return lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(carrera.getId());
        } catch (Exception e) {
        }
        return null;
    }

    public String grabar(LineaInvestigacionCarrera lineaInvestigacionCarrera) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (lineaInvestigacionCarrera.getId() == null) {
                int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "crear_linea_investigacion_carrera");
                if (tienePermiso == 1) {
                    lineaInvestigacionService.guardar(lineaInvestigacionCarrera.getLineaInvestigacionId());
                    logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacionCarrera.getLineaInvestigacionId().getId()
                            + "", "CREAR", "|Nombre= " + lineaInvestigacionCarrera.getLineaInvestigacionId().getDescripcion(),
                            sessionUsuario.getUsuario()));
                    lineaInvestigacionCarreraDao.create(lineaInvestigacionCarrera);
                    logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionCarrera", lineaInvestigacionCarrera.getId() + "", "CREAR",
                            "|Carrera=" + lineaInvestigacionCarrera.getCarreraId() + "|LineaInvestigacion="
                            + lineaInvestigacionCarrera.getLineaInvestigacionId(), sessionUsuario.getUsuario()));
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionLineaInvestigacionCarrera.setLineaInvestigacionCarrera(new LineaInvestigacionCarrera());
                        return "pretty:lineasInvestigacionCarrera";
                    }
                    if (param.equalsIgnoreCase("grabar-editar")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        return "";
                    }
                    return "";
                }
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                buscar();
                return "";
            }
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion_carrera");
            if (tienePermiso == 1) {
                lineaInvestigacionService.actualizar(lineaInvestigacionCarrera.getLineaInvestigacionId());
                logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacionCarrera.getLineaInvestigacionId().getId()
                        + "", "EDITAR", "|Nombre= " + lineaInvestigacionCarrera.getLineaInvestigacionId().getDescripcion(),
                        sessionUsuario.getUsuario()));
                lineaInvestigacionCarreraDao.edit(lineaInvestigacionCarrera);
                logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionCarrera", lineaInvestigacionCarrera.getId() + "", "EDITAR",
                        "|Carrera=" + lineaInvestigacionCarrera.getCarreraId() + "|LineaInvestigacion="
                        + lineaInvestigacionCarrera.getLineaInvestigacionId(), sessionUsuario.getUsuario()));
                if (param.equalsIgnoreCase("grabar")) {
                    sessionLineaInvestigacionCarrera.setLineaInvestigacionCarrera(new LineaInvestigacionCarrera());
                    return "pretty:lineasInvestigacionCarrera";
                }
                if (param.equalsIgnoreCase("grabar-editar")) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.lineaInvestigacion") + " " + bundle.getString("lbl.msm_editar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
                return "";
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". "
                    + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            buscar();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            sessionLineaInvestigacionCarrera.setRenderedBuscar(true);
        } else {
            sessionLineaInvestigacionCarrera.setRenderedBuscar(false);
        }
    }

    public void renderedEditar() {
        int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion_carrera");
        if (tienePermiso == 1) {
            sessionLineaInvestigacionCarrera.setRenderedEditar(true);
        } else {
            sessionLineaInvestigacionCarrera.setRenderedEditar(false);
        }
    }

    public void renderedEliminar() {
        int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "eliminar_linea_investigacion_carrera");
        if (tienePermiso == 1) {
            sessionLineaInvestigacionCarrera.setRenderedEliminar(true);
        } else {
            sessionLineaInvestigacionCarrera.setRenderedEliminar(false);
        }
    }

    public void renderedCrear() {
        int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "crear_linea_investigacion_carrera");
        if (tienePermiso == 1) {
            sessionLineaInvestigacionCarrera.setRenderedCrear(true);
        } else {
            sessionLineaInvestigacionCarrera.setRenderedCrear(false);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public SessionLineaInvestigacionCarrera getSessionLineaInvestigacionCarrera() {
        return sessionLineaInvestigacionCarrera;
    }

    public void setSessionLineaInvestigacionCarrera(SessionLineaInvestigacionCarrera sessionLineaInvestigacionCarrera) {
        this.sessionLineaInvestigacionCarrera = sessionLineaInvestigacionCarrera;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionUsuarioCarrera getSessionUsuarioCarrera() {
        return sessionUsuarioCarrera;
    }

    public void setSessionUsuarioCarrera(SessionUsuarioCarrera sessionUsuarioCarrera) {
        this.sessionUsuarioCarrera = sessionUsuarioCarrera;
    }
//</editor-fold>
}

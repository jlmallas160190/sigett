/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.comun.managed.session.SessionLineaInvestigacionCarrera;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuarioCarrera;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import com.jlmallas.seguridad.entity.Usuario;
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
import edu.unl.sigett.session.LineaInvestigacionCarreraFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

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
            viewId = "/faces/pages/sigett/editarLineaInvestigacionCarrera.xhtml"
    ),
    @URLMapping(
            id = "crearLineaInvestigacionCarrera",
            pattern = "/crearLineaInvestigacionCarera/",
            viewId = "/faces/pages/sigett/editarLineaInvestigacionCarrera.xhtml"
    ),
    @URLMapping(
            id = "lineasInvestigacionCarrera",
            pattern = "/lineasInvestigacionCarrera/",
            viewId = "/faces/pages/sigett/buscarLineasInvestigacionCarrera.xhtml"
    )})
public class AdministrarLineasInvestigacionCarrera implements Serializable {

    @Inject
    private SessionLineaInvestigacionCarrera sessionLineaInvestigacionCarrera;
    @Inject
    private SessionUsuario sessionUsuario;
    private List<LineaInvestigacionCarrera> lineaInvestigacionCarreras;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    @EJB
    private LineaInvestigacionCarreraFacadeLocal lineaInvestigacionCarreraFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private boolean renderedNoEditar;
    @EJB
    private LineaInvestigacionFacadeLocal lineaInvestigacionFacadeLocal;
    private String criterio;
    private boolean renderedBuscar;

    public AdministrarLineasInvestigacionCarrera() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_linea_investigacion_carrera");
            if (tienePermiso == 1) {
                sessionLineaInvestigacionCarrera.setLineaInvestigacionCarrera(new LineaInvestigacionCarrera());
                sessionLineaInvestigacionCarrera.getLineaInvestigacionCarrera().setLineaInvestigacionId(new LineaInvestigacion());
                sessionLineaInvestigacionCarrera.getLineaInvestigacionCarrera().setCarreraId(sessionUsuarioCarrera.getUsuarioCarrera().getCarreraId());
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

    public String viewLineasInvestigacionCarrera() {
        this.lineaInvestigacionCarreras = new ArrayList<>();
        return "pretty:lineasInvestigacionCarrera";
    }

    public String editar(LineaInvestigacionCarrera lineaInvestigacionCarrera) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion_carrera");
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

    public boolean disabledEditar(LineaInvestigacionCarrera lc) {
        boolean var = false;
        if (lc.getLineaInvestigacionId().getEsActivo() == false) {
            var = true;
        }
        return var;
    }

    public void eliminar(LineaInvestigacion lineaInvestigacion) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_linea_investigacion_carrera");
            if (tienePermiso == 1) {
                if (lineaInvestigacion.getEsActivo()) {
                    lineaInvestigacion.setEsActivo(false);
                    logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "DESACTIVAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                } else {
                    lineaInvestigacion.setEsActivo(true);
                    logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "ACTIVAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                }
                lineaInvestigacionFacadeLocal.edit(lineaInvestigacion);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);

                }
            }
        } catch (Exception e) {
        }
    }

    public void buscar(String criterio) {
        this.lineaInvestigacionCarreras = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");

            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_linea_investigacion_carrera");
            if (tienePermiso == 1) {
                for (LineaInvestigacionCarrera lc : lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(sessionUsuarioCarrera.getUsuarioCarrera().getCarreraId())) {
                    if (lc.getLineaInvestigacionId().getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                        this.lineaInvestigacionCarreras.add(lc);
                    }
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);

                }
            }
        } catch (Exception e) {
        }
    }

    public List<LineaInvestigacionCarrera> buscarByCarrera(Carrera carrera) {
        try {
            return lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(carrera.getId());
        } catch (Exception e) {
        }
        return null;
    }

    public String grabar(LineaInvestigacionCarrera lineaInvestigacionCarrera) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (lineaInvestigacionCarrera.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_linea_investigacion_carrera");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        lineaInvestigacionFacadeLocal.create(lineaInvestigacionCarrera.getLineaInvestigacionId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacionCarrera.getLineaInvestigacionId().getId() + "", "CREAR", "|Nombre= " + lineaInvestigacionCarrera.getLineaInvestigacionId().getDescripcion(), sessionUsuario.getUsuario()));
                        lineaInvestigacionCarreraFacadeLocal.create(lineaInvestigacionCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionCarrera", lineaInvestigacionCarrera.getId() + "", "CREAR", "|Carrera=" + lineaInvestigacionCarrera.getCarreraId() + "|LineaInvestigacion=" + lineaInvestigacionCarrera.getLineaInvestigacionId(), sessionUsuario.getUsuario()));
                        navegacion = "pretty:lineasInvestigacionCarrera";
                        sessionLineaInvestigacionCarrera.setLineaInvestigacionCarrera(new LineaInvestigacionCarrera());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            lineaInvestigacionFacadeLocal.create(lineaInvestigacionCarrera.getLineaInvestigacionId());
                            logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacionCarrera.getLineaInvestigacionId().getId() + "", "CREAR", "|Nombre= " + lineaInvestigacionCarrera.getLineaInvestigacionId().getDescripcion(), sessionUsuario.getUsuario()));
                            lineaInvestigacionCarreraFacadeLocal.create(lineaInvestigacionCarrera);
                            logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionCarrera", lineaInvestigacionCarrera.getId() + "", "CREAR", "|Carrera=" + lineaInvestigacionCarrera.getCarreraId() + "|LineaInvestigacion=" + lineaInvestigacionCarrera.getLineaInvestigacionId(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                    buscar("");
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion_carrera");//guardar Usuario
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        lineaInvestigacionFacadeLocal.edit(lineaInvestigacionCarrera.getLineaInvestigacionId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacionCarrera.getLineaInvestigacionId().getId() + "", "EDITAR", "|Nombre= " + lineaInvestigacionCarrera.getLineaInvestigacionId().getDescripcion(), sessionUsuario.getUsuario()));
                        lineaInvestigacionCarreraFacadeLocal.edit(lineaInvestigacionCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionCarrera", lineaInvestigacionCarrera.getId() + "", "EDITAR", "|Carrera=" + lineaInvestigacionCarrera.getCarreraId() + "|LineaInvestigacion=" + lineaInvestigacionCarrera.getLineaInvestigacionId(), sessionUsuario.getUsuario()));
                        navegacion = "pretty:lineasInvestigacionCarrera";
                        sessionLineaInvestigacionCarrera.setLineaInvestigacionCarrera(new LineaInvestigacionCarrera());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            lineaInvestigacionFacadeLocal.edit(lineaInvestigacionCarrera.getLineaInvestigacionId());
                            logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacionCarrera.getLineaInvestigacionId().getId() + "", "EDITAR", "|Nombre= " + lineaInvestigacionCarrera.getLineaInvestigacionId().getDescripcion(), sessionUsuario.getUsuario()));
                            lineaInvestigacionCarreraFacadeLocal.edit(lineaInvestigacionCarrera);
                            logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionCarrera", lineaInvestigacionCarrera.getId() + "", "EDITAR", "|Carrera=" + lineaInvestigacionCarrera.getCarreraId() + "|LineaInvestigacion=" + lineaInvestigacionCarrera.getLineaInvestigacionId(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.lineaInvestigacion") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                    buscar("");
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion_carrera");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }

    public boolean renderedEliminar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_linea_investigacion_carrera");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_linea_investigacion_carrera");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

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

    public List<LineaInvestigacionCarrera> getLineaInvestigacionCarreras() {
        return lineaInvestigacionCarreras;
    }

    public void setLineaInvestigacionCarreras(List<LineaInvestigacionCarrera> lineaInvestigacionCarreras) {
        this.lineaInvestigacionCarreras = lineaInvestigacionCarreras;
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

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }
//</editor-fold>
}

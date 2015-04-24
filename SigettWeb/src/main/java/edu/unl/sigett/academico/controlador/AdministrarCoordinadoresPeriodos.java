/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.comun.managed.session.SessionCoordinadorPeriodo;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.Coordinador;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import com.jlmallas.comun.entity.Persona;
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
import org.primefaces.context.RequestContext;
import edu.jlmallas.academico.service.CoordinadorFacadeLocal;
import edu.jlmallas.academico.service.CoordinadorPeriodoFacadeLocal;
import edu.jlmallas.academico.service.PeriodoCoordinacionFacadeLocal;
import com.jlmallas.comun.service.PersonaFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarCoordinador",
            pattern = "/editarCoordinador/#{sessionCoordinadorPeriodo.coordinadorPeriodo.id}",
            viewId = "/faces/pages/academico/editarCoordinadorPeriodo.xhtml"
    ),
    @URLMapping(
            id = "crearCoordinador",
            pattern = "/crearCoordinador/",
            viewId = "/faces/pages/academico/editarCoordinadorPeriodo.xhtml"
    ),
    @URLMapping(
            id = "coordinadores",
            pattern = "/coordinadores/",
            viewId = "/faces/pages/academico/buscarCoordinadoresPeriodos.xhtml"
    )})
public class AdministrarCoordinadoresPeriodos implements Serializable {
    
    @Inject
    private SessionCoordinadorPeriodo sessionCoordinadorPeriodo;
    private String criterio;
    @EJB
    private CoordinadorPeriodoFacadeLocal coordinadorPeriodoFacadeLocal;
    @EJB
    private CoordinadorFacadeLocal coordinadorFacadeLocal;
    @EJB
    private PersonaFacadeLocal personaFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private List<CoordinadorPeriodo> coordinadorPeriodos;
    private String numeroIdentificacion;
    private boolean renderedCrear;
    private boolean renderedEditar;
    private boolean renderedNoEditar;
    private boolean renderedEliminar;
    private String periodoCoordinacion;
    @EJB
    private PeriodoCoordinacionFacadeLocal periodoCoordinacionFacadeLocal;
    
    public AdministrarCoordinadoresPeriodos() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String navegarListado(Usuario usuario, Carrera carrera) {
        buscarPorCarrera("", usuario, carrera);
        renderedCrear(usuario);
        renderedEditar(usuario);
        renderedEliminar(usuario);
        return "pretty:coordinadores";
    }
    
    public String crear(Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionCoordinadorPeriodo.setCoordinadorPeriodo(new CoordinadorPeriodo());
                sessionCoordinadorPeriodo.getCoordinadorPeriodo().setCoordinadorId(new Coordinador());
                sessionCoordinadorPeriodo.setPersona(new Persona());
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "pretty:crearCoordinador";
                } else {
                    if (param.equalsIgnoreCase("crear-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarPeriodoCoordinacion').show()");
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }
    public Persona getPersona(CoordinadorPeriodo coordinadorPeriodo){
        try {
            return personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
        } catch (Exception e) {
        }
        return null;
    }
    public String editar(Usuario usuario, CoordinadorPeriodo coordinadorPeriodo) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionCoordinadorPeriodo.setCoordinadorPeriodo(coordinadorPeriodo);
                sessionCoordinadorPeriodo.setPersona(personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId()));
                if (param.equalsIgnoreCase("editar")) {
                    navegacion = "pretty:editarCoordinador";
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }
    
    public void buscarPorCarrera(String criterio, Usuario usuario, Carrera carrera) {
        this.coordinadorPeriodos = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                for (CoordinadorPeriodo coordinadorPeriodo : coordinadorPeriodoFacadeLocal.buscarPorCarrera(carrera.getId())) {
                    Persona persona = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
                    if (persona.getApellidos().toLowerCase().contains(criterio.toLowerCase())
                            || persona.getNombres().toLowerCase().contains(criterio.toLowerCase())
                            || persona.getNumeroIdentificacion().toLowerCase().contains(criterio.toLowerCase())) {
                        coordinadorPeriodos.add(coordinadorPeriodo);
                    }
                }
                renderedEditar(usuario);
                renderedEliminar(usuario);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }
    
    public void encargarCoordinacion(CoordinadorPeriodo coordinadorPeriodo, Usuario usuario, Carrera carrera) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                if (coordinadorPeriodo.getId() != null) {
                    if (coordinadorPeriodo.getEsVigente()) {
                        for (CoordinadorPeriodo c : coordinadorPeriodoFacadeLocal.buscarPorCarrera(carrera.getId())) {
                            if (c.getId() != coordinadorPeriodo.getId()) {
                                c.setEsVigente(false);
                                coordinadorPeriodoFacadeLocal.edit(c);
                            }
                        }
                    }
                    coordinadorPeriodoFacadeLocal.edit(coordinadorPeriodo);
                    buscarPorCarrera("", usuario, carrera);
                }
            }
        } catch (Exception e) {
        }
        
    }
    
    public String grabar(Usuario usuario, Carrera carrera) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        int pos = periodoCoordinacion.indexOf(":");
        PeriodoCoordinacion pc = periodoCoordinacionFacadeLocal.find(Long.parseLong(periodoCoordinacion.substring(0, pos)));
        if (pc != null) {
            sessionCoordinadorPeriodo.getCoordinadorPeriodo().setPeriodoId(pc);
        }
        try {
            if (sessionCoordinadorPeriodo.getCoordinadorPeriodo().getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_coordinador_periodo_coordinacion");
                if (tienePermiso == 1) {
                    if (sessionCoordinadorPeriodo.getPersona() != null) {
                        sessionCoordinadorPeriodo.getCoordinadorPeriodo().getCoordinadorId().setId(sessionCoordinadorPeriodo.getPersona().getId());
                        coordinadorFacadeLocal.create(sessionCoordinadorPeriodo.getCoordinadorPeriodo().getCoordinadorId());
                        coordinadorPeriodoFacadeLocal.create(sessionCoordinadorPeriodo.getCoordinadorPeriodo());
                        if (param.equalsIgnoreCase("grabar")) {
                            sessionCoordinadorPeriodo.setCoordinadorPeriodo(new CoordinadorPeriodo());
                            navegacion = "pretty:coordinadores";
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.coordinador") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                        buscarPorCarrera(criterio, usuario, carrera);
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.coordinador") + " " + bundle.getString("lbl.no_encontrado"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_coordinador_periodo_coordinacion");
                if (tienePermiso == 1) {
                    if (sessionCoordinadorPeriodo.getPersona() != null) {
                        coordinadorFacadeLocal.edit(sessionCoordinadorPeriodo.getCoordinadorPeriodo().getCoordinadorId());
                        coordinadorPeriodoFacadeLocal.edit(sessionCoordinadorPeriodo.getCoordinadorPeriodo());
                        if (param.equalsIgnoreCase("grabar")) {
                            sessionCoordinadorPeriodo.setCoordinadorPeriodo(new CoordinadorPeriodo());
                            navegacion = "pretty:coordinadores";
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.coordinador") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                        buscarPorCarrera(criterio, usuario, carrera);
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.coordinador") + ". " + bundle.getString("lbl.no_encontrado"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }
    
    public void remover(CoordinadorPeriodo coordinadorPeriodo, Usuario usuario, Carrera carrera) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                coordinadorPeriodoFacadeLocal.remove(coordinadorPeriodo);
                buscarPorCarrera(criterio, usuario, carrera);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.coordinador") + " " + bundle.getString("lbl.msm_eliminar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }
    
    public void agregarCoordinador(String numeroIdentificacion) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Persona p = personaFacadeLocal.buscarPorNumeroIdentificacion(numeroIdentificacion);
            if (p != null) {
                sessionCoordinadorPeriodo.setPersona(p);
            } else {
                sessionCoordinadorPeriodo.setPersona(new Persona());
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.no_encontrado"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedCrear(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                renderedCrear = true;
            } else {
                renderedCrear = false;
            }
        } catch (Exception e) {
        }
    }
    
    public void renderedEditar(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                renderedEditar = true;
                renderedNoEditar = false;
            } else {
                renderedNoEditar = true;
                renderedEditar = false;
            }
        } catch (Exception e) {
        }
    }
    
    public void renderedEliminar(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                renderedEliminar = true;
            } else {
                renderedEliminar = false;
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionCoordinadorPeriodo getSessionCoordinadorPeriodo() {
        return sessionCoordinadorPeriodo;
    }
    
    public void setSessionCoordinadorPeriodo(SessionCoordinadorPeriodo sessionCoordinadorPeriodo) {
        this.sessionCoordinadorPeriodo = sessionCoordinadorPeriodo;
    }
    
    public String getCriterio() {
        return criterio;
    }
    
    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }
    
    public List<CoordinadorPeriodo> getCoordinadorPeriodos() {
        return coordinadorPeriodos;
    }
    
    public void setCoordinadorPeriodos(List<CoordinadorPeriodo> coordinadorPeriodos) {
        this.coordinadorPeriodos = coordinadorPeriodos;
    }
    
    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }
    
    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }
    
    public boolean isRenderedCrear() {
        return renderedCrear;
    }
    
    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }
    
    public boolean isRenderedEditar() {
        return renderedEditar;
    }
    
    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }
    
    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }
    
    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }
    
    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }
    
    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }
    
    public String getPeriodoCoordinacion() {
        return periodoCoordinacion;
    }
    
    public void setPeriodoCoordinacion(String periodoCoordinacion) {
        this.periodoCoordinacion = periodoCoordinacion;
    }
//</editor-fold>
}

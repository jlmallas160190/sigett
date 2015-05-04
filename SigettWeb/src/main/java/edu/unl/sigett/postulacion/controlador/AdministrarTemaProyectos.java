/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import edu.unl.sigett.entity.EstadoProyecto;
import edu.unl.sigett.postulacion.managed.session.SessionProyecto;
import edu.unl.sigett.postulacion.managed.session.SessionTemaProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.TemaProyecto;
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
import edu.unl.sigett.session.ProyectoFacadeLocal;
import edu.unl.sigett.session.TemaProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarTemaProyectos implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionTemaProyecto sessionTemaProyecto;
    @Inject
    private SessionProyecto sessionProyecto;
    @EJB
    private TemaProyectoFacadeLocal temaProyectoFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ProyectoFacadeLocal proyectoFacadeLocal;
    
    private String criterio;
    private boolean renderedNoEditar;
    private boolean renderedSeleccionarTema;
    private boolean renderedCrear;
    private boolean renderedEditar;
    private boolean renderedEliminarTema;
    private boolean renderedBuscar;

    private List<TemaProyecto> temaProyectos;

    public AdministrarTemaProyectos() {
    }
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear(Usuario usuario) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String navegacion = "";
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_proyecto");
            if (tienePermiso == 1) {
                sessionTemaProyecto.setTemaProyecto(new TemaProyecto());
                sessionTemaProyecto.getTemaProyecto().setProyectoId(sessionProyecto.getProyecto());
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public String editar(TemaProyecto temaProyecto, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_tema_proyecto");
            if (tienePermiso == 1) {
                sessionTemaProyecto.setTemaProyecto(temaProyecto);
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

    public String grabar(TemaProyecto temaProyecto) {
        String navegacion = "";
        try {
            Proyecto p = proyectoFacadeLocal.find(temaProyecto.getProyectoId().getId());
            if (p.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                temaProyecto.getTemaId().setDescripcion(temaProyecto.getTemaId().getNombre());
                temaProyecto.setEsActual(true);
                if (temaProyecto.getId() == null) {
                    temaProyectoFacadeLocal.create(temaProyecto);
                } else {
                    temaProyectoFacadeLocal.edit(temaProyecto);
                    if (temaProyecto.getProyectoId() != null) {
                        temaProyecto.getProyectoId().setTemaActual(temaProyecto.getTemaId().getNombre());
                        proyectoFacadeLocal.edit(temaProyecto.getProyectoId());
                    }
                }
            }
            buscar(sessionProyecto.getProyecto(), criterio);
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void buscar(Proyecto proyecto, String criterio) {
        temaProyectos = new ArrayList<>();
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_tema_proyecto");
            if (tienePermiso == 1) {
                for (TemaProyecto temaProyecto : proyecto.getTemaProyectoList()) {
                    if (temaProyecto.isEsRemovido() == false) {
                        if (!temaProyectos.contains(temaProyecto)) {
                            if (temaProyecto.getTemaId().getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                                temaProyectos.add(temaProyecto);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void actualizaEstadoTemas(Proyecto proyecto, TemaProyecto temaProyecto) {
        for (TemaProyecto tp : proyecto.getTemaProyectoList()) {
            if (tp != temaProyecto) {
                tp.setEsActual(false);
                if (tp.getId() != null) {
                    getTemaProyectoFacadeLocal().edit(tp);
                }
            }
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_tema_proyecto");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedCrear() {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_tema_proyecto");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }

    public void renderedEditar() {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_tema_proyecto");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedEditar = false;
            renderedNoEditar = true;
        }
    }

    public void renderedEliminar() {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_tema_proyecto");
        if (tienePermiso == 1) {
            renderedEliminarTema = true;
        } else {
            renderedEliminarTema = false;
        }
    }

    public void renderedSeleccionar() {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_tema_proyecto");
        if (tienePermiso == 1) {
            renderedSeleccionarTema = true;
        } else {
            renderedSeleccionarTema = false;
        }
    }

    public void remover(TemaProyecto temaProyecto) {
        if (temaProyecto.getId() != null) {
            if (temaProyecto.getEsActual() == false) {
                temaProyecto.setEsRemovido(true);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tema removido exitosamente.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tema no se puede eliminar. Debido a que este tema ha sido selecciondo como principal del Proyecto", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            getSessionProyecto().getProyecto().getTemaProyectoList().remove(temaProyecto);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tema removido exitosamente.", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        buscar(sessionProyecto.getProyecto(), criterio);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionTemaProyecto getSessionTemaProyecto() {
        return sessionTemaProyecto;
    }

    public void setSessionTemaProyecto(SessionTemaProyecto sessionTemaProyecto) {
        this.sessionTemaProyecto = sessionTemaProyecto;
    }

    public SessionProyecto getSessionProyecto() {
        return sessionProyecto;
    }

    public void setSessionProyecto(SessionProyecto sessionProyecto) {
        this.sessionProyecto = sessionProyecto;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    /**
     * @return the temaProyectoFacadeLocal
     */
    public TemaProyectoFacadeLocal getTemaProyectoFacadeLocal() {
        return temaProyectoFacadeLocal;
    }

    /**
     * @param temaProyectoFacadeLocal the temaProyectoFacadeLocal to set
     */
    public void setTemaProyectoFacadeLocal(TemaProyectoFacadeLocal temaProyectoFacadeLocal) {
        this.temaProyectoFacadeLocal = temaProyectoFacadeLocal;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<TemaProyecto> getTemaProyectos() {
        return temaProyectos;
    }

    public void setTemaProyectos(List<TemaProyecto> temaProyectos) {
        this.temaProyectos = temaProyectos;
    }

    public boolean isRenderedSeleccionarTema() {
        return renderedSeleccionarTema;
    }

    public void setRenderedSeleccionarTema(boolean renderedSeleccionarTema) {
        this.renderedSeleccionarTema = renderedSeleccionarTema;
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

    public boolean isRenderedEliminarTema() {
        return renderedEliminarTema;
    }

    public void setRenderedEliminarTema(boolean renderedEliminarTema) {
        this.renderedEliminarTema = renderedEliminarTema;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }
//</editor-fold>
}

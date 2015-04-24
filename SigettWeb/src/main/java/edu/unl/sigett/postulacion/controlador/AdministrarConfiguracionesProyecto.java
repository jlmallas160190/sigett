/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import edu.unl.sigett.comun.controlador.AdministrarCatalogoDuracion;
import edu.unl.sigett.postulacion.managed.session.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.Proyecto;
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
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarConfiguracionesProyecto implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private AdministrarCatalogoDuracion administrarCatalogoDuracion;

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    private List<ConfiguracionProyecto> configuracionProyectos;

    private String criterio;
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedBuscar;

    public AdministrarConfiguracionesProyecto() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String editar(ConfiguracionProyecto configuracionProyecto, Usuario usuario, Proyecto proyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_configuracion_proyecto");
                if (tienePermiso == 1) {
                    configuracionProyecto.setEsEditado(true);
                    if (configuracionProyecto.getCodigo().equalsIgnoreCase("DS")) {
                        if (Integer.parseInt(configuracionProyecto.getValor()) <= 7 && Integer.parseInt(configuracionProyecto.getValor()) > 0) {
                        } else {
                            configuracionProyecto.setValor("7");
                        }
                    } else {
                        if (configuracionProyecto.getCodigo().equalsIgnoreCase("HD")) {
                            if (Integer.parseInt(configuracionProyecto.getValor()) <= 24 && Integer.parseInt(configuracionProyecto.getValor()) > 0) {
                            } else {
                                configuracionProyecto.setValor("24");
                            }
                        } else {
                            if (configuracionProyecto.getCodigo().equalsIgnoreCase("CD")) {
                                configuracionProyecto.setValor(administrarCatalogoDuracion.seleccionaCatalogoDuracion().getId() + "");
                            }
                        }
                    }
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public void agregarConfiguracionesProyecto(Proyecto proyecto) {
        if (proyecto.getConfiguracionProyectoList().isEmpty()) {
            ConfiguracionProyecto cf = new ConfiguracionProyecto();
            cf.setNombre("Días de trabajo a la semana");
            cf.setCodigo("DS");
            cf.setValor("7");
            cf.setEsEditado(true);
            cf.setTipo("numerico");
            proyecto.getConfiguracionProyectoList().add(cf);
            cf.setProyectoId(proyecto);
            ConfiguracionProyecto cf1 = new ConfiguracionProyecto();
            cf1.setNombre("Horas díarias de trabajo");
            cf1.setCodigo("HD");
            cf1.setValor("8");
            cf1.setTipo("numerico");
            cf1.setEsEditado(true);
            cf1.setProyectoId(proyecto);
            proyecto.getConfiguracionProyectoList().add(cf1);
            ConfiguracionProyecto cf2 = new ConfiguracionProyecto();
            cf2.setNombre("Catalogo duración actual");
            cf2.setCodigo("CD");
            cf2.setTipo("combo");
            cf2.setValor("1");
            cf2.setEsEditado(true);
            cf2.setProyectoId(proyecto);
            proyecto.getConfiguracionProyectoList().add(cf2);
        }
    }

    public void buscar(String criterio, Proyecto proyecto) {
        this.configuracionProyectos = new ArrayList<>();
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_configuracion_proyecto");
            if (tienePermiso == 1) {
                for (ConfiguracionProyecto configuracionProyecto : proyecto.getConfiguracionProyectoList()) {
                    if (configuracionProyecto.getNombre().toUpperCase().contains(criterio.toUpperCase())) {
                        configuracionProyectos.add(configuracionProyecto);
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para buscar Configuraciones. Consulte con el administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedEditar(Usuario usuario, Proyecto proyecto) {
        if (proyecto.getEstadoProyectoId().getId() == 2) {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_configuracion_proyecto");
            if (tienePermiso == 1) {
                renderedEditar = true;
                renderedNoEditar = false;
            } else {
                renderedNoEditar = true;
                renderedEditar = false;
            }
        } else {
            renderedNoEditar = true;
            renderedEditar = false;
        }
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_configuracion_proyecto");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionProyecto getSessionProyecto() {
        return sessionProyecto;
    }

    public void setSessionProyecto(SessionProyecto sessionProyecto) {
        this.sessionProyecto = sessionProyecto;
    }

    public List<ConfiguracionProyecto> getConfiguracionProyectos() {
        return configuracionProyectos;
    }

    public void setConfiguracionProyectos(List<ConfiguracionProyecto> configuracionProyectos) {
        this.configuracionProyectos = configuracionProyectos;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public AdministrarCatalogoDuracion getAdministrarCatalogoDuracion() {
        return administrarCatalogoDuracion;
    }

    public void setAdministrarCatalogoDuracion(AdministrarCatalogoDuracion administrarCatalogoDuracion) {
        this.administrarCatalogoDuracion = administrarCatalogoDuracion;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }
//</editor-fold>
}

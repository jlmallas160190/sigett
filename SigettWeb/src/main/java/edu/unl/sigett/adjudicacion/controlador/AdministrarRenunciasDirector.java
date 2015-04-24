/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.adjudicacion.controlador;

import edu.unl.sigett.adjudicacion.session.SessionDirectorProyecto;
import edu.unl.sigett.adjudicacion.session.SessionRenunciaDirector;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.EstadoDirector;
import edu.unl.sigett.entity.EstadoProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.RenunciaDirector;
import com.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.session.DirectorProyectoFacadeLocal;
import edu.unl.sigett.session.EstadoDirectorFacadeLocal;
import edu.unl.sigett.session.EstadoProyectoFacadeLocal;
import edu.unl.sigett.session.ProyectoFacadeLocal;
import edu.unl.sigett.session.RenunciaDirectorFacadeLocal;
import edu.unl.sigett.session.RenunciaFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarRenunciasDirector implements Serializable {

    @Inject
    private SessionRenunciaDirector sessionRenunciaDirector;
    @Inject
    private SessionDirectorProyecto sessionDirectorProyecto;
    @Inject
    private AdministrarDirectoresProyecto administrarDirectoresProyecto;
    @EJB
    private EstadoDirectorFacadeLocal estadoDirectorFacadeLocal;
    @EJB
    private RenunciaDirectorFacadeLocal renunciaDirectorFacadeLocal;
    @EJB
    private RenunciaFacadeLocal renunciaFacadeLocal;
    private List<RenunciaDirector> renunciaDirectors;
    @EJB
    private DirectorProyectoFacadeLocal directorProyectoFacadeLocal;
    @EJB
    private EstadoProyectoFacadeLocal estadoProyectoFacadeLocal;
    @EJB
    private ProyectoFacadeLocal proyectoFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    public AdministrarRenunciasDirector() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String editar(RenunciaDirector renunciaDirector, Usuario usuario) {
        String navegacion = "";
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_renuncia_director");
            if (tienePermiso == 1) {
                sessionRenunciaDirector.setRenunciaDirector(renunciaDirector);
                renunciaDirector.setEsEditado(true);
                navegacion = "editarRenunciaDirector?faces=redirect=true";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para editar Renuncia. Consulte con el administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void buscar(DirectorProyecto directorProyecto, Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_renuncia_director");
            if (tienePermiso == 1) {
                renunciaDirectors = renunciaDirectorFacadeLocal.buscarPorDirectorProyecto(directorProyecto.getId());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para buscar Renuncias. Consulte con el administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public String grabar(RenunciaDirector renunciaDirector, Usuario usuario, Proyecto proyecto, DirectorProyecto directorProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            EstadoDirector estadoDirector = estadoDirectorFacadeLocal.find((int) 2);
            directorProyecto.setFechaCulminacion(renunciaDirector.getRenuncia().getFecha());
            directorProyecto.setEstadoDirectorId(estadoDirector);
            renunciaDirector.setDirectorProyectoId(directorProyecto);
            if (renunciaDirector.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_renuncia_director");
                if (tienePermiso == 1) {
                    renunciaFacadeLocal.create(renunciaDirector.getRenuncia());
                    renunciaDirector.setId(renunciaDirector.getRenuncia().getId());
                    renunciaDirectorFacadeLocal.create(renunciaDirector);
                    directorProyectoFacadeLocal.edit(directorProyecto);
                    if (param.equalsIgnoreCase("grabar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarRenunciaDirectorProyecto').hide()");
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_renuncia_director");
                if (tienePermiso == 1) {
                    renunciaFacadeLocal.edit(renunciaDirector.getRenuncia());
                    renunciaDirectorFacadeLocal.edit(renunciaDirector);
                    directorProyectoFacadeLocal.edit(directorProyecto);
                    if (param.equalsIgnoreCase("grabar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarRenunciaDirectorProyecto').hide()");
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
            buscar(directorProyecto, usuario);
            administrarDirectoresProyecto.buscar("", usuario, proyecto);
            administrarDirectoresProyecto.renderedBuscarDirectorDisponible(usuario, proyecto);
            administrarDirectoresProyecto.renderedSortearDirectorProyecto(usuario, proyecto);
            administrarDirectoresProyecto.renderedSeleccionar(usuario, proyecto);
            if (directorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()).isEmpty()) {
                EstadoProyecto ep = estadoProyectoFacadeLocal.find((int) 2);
                if (ep != null) {
                    proyecto.setEstadoProyectoId(ep);
                    if (proyecto.getId() != null) {
                        proyectoFacadeLocal.edit(proyecto);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void eliminar(RenunciaDirector renunciaDirector, DirectorProyecto directorProyecto, Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_renuncia_director");
            EstadoDirector estadoDirector = estadoDirectorFacadeLocal.find((int) 1);
            if (tienePermiso == 1) {
                renunciaDirectorFacadeLocal.remove(renunciaDirector);
                renunciaFacadeLocal.remove(renunciaDirector.getRenuncia());
                if (renunciaDirectorFacadeLocal.buscarPorDirectorProyecto(directorProyecto.getId()).isEmpty()) {
                    directorProyecto.setEstadoDirectorId(estadoDirector);
                    directorProyectoFacadeLocal.edit(directorProyecto);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para eliminar Renuncia. Consulte con el administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionRenunciaDirector getSessionRenunciaDirector() {
        return sessionRenunciaDirector;
    }

    public void setSessionRenunciaDirector(SessionRenunciaDirector sessionRenunciaDirector) {
        this.sessionRenunciaDirector = sessionRenunciaDirector;
    }

    public SessionDirectorProyecto getSessionDirectorProyecto() {
        return sessionDirectorProyecto;
    }

    public void setSessionDirectorProyecto(SessionDirectorProyecto sessionDirectorProyecto) {
        this.sessionDirectorProyecto = sessionDirectorProyecto;
    }

    public List<RenunciaDirector> getRenunciaDirectors() {
        return renunciaDirectors;
    }

    public void setRenunciaDirectors(List<RenunciaDirector> renunciaDirectors) {
        this.renunciaDirectors = renunciaDirectors;
    }
//</editor-fold>
}

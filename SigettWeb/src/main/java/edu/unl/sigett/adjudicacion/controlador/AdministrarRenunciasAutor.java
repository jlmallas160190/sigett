/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.adjudicacion.controlador;

import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.EstadoAutor;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.RenunciaAutor;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.session.AutorProyectoFacadeLocal;
import edu.unl.sigett.session.EstadoAutorFacadeLocal;
import edu.unl.sigett.session.RenunciaAutorFacadeLocal;
import edu.unl.sigett.session.RenunciaFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.adjudicacion.session.SessionRenunciaAutor;
import edu.unl.sigett.postulacion.managed.session.SessionAutorProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarRenunciasAutor implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionRenunciaAutor sessionRenunciaAutor;
    @EJB
    private EstadoAutorFacadeLocal estadoAutorFacadeLocal;
    @Inject
    private SessionAutorProyecto sessionAutorProyecto;
    private List<RenunciaAutor> renunciaAutors;
    @EJB
    private RenunciaAutorFacadeLocal renunciaAutorFacadeLocal;
    @EJB
    private RenunciaFacadeLocal renunciaFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private AutorProyectoFacadeLocal autorProyectoFacadeLocal;

    public AdministrarRenunciasAutor() {
    }
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String editar(RenunciaAutor renunciaAutor, Usuario usuario) {
        String navegacion = "";
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_renuncia_autor");
            if (tienePermiso == 1) {
                sessionRenunciaAutor.setRenunciaAutor(renunciaAutor);
                renunciaAutor.setEsEditado(true);
                navegacion = "editarRenunciaAutor?faces=redirect=true";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para editar Renuncia. Consulte con el administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void buscar(AutorProyecto autorProyecto, Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_renuncia_autor");
            if (tienePermiso == 1) {
                renunciaAutors = renunciaAutorFacadeLocal.buscarPorAutorProyecto(autorProyecto.getId());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para buscar Renuncias. Consulte con el administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public String grabar(RenunciaAutor renunciaAutor, Usuario usuario, Proyecto proyecto, AutorProyecto autorProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            EstadoAutor estadoAutor = estadoAutorFacadeLocal.find((int) 10);
            renunciaAutor.setEsEditado(true);
            autorProyecto.setFechaCulminacion(renunciaAutor.getRenuncia().getFecha());
            autorProyecto.setEstadoAutorId(estadoAutor);
            renunciaAutor.setAutorProyectoId(autorProyecto);
            if (renunciaAutor.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_renuncia_autor");
                if (tienePermiso == 1) {
                    renunciaFacadeLocal.create(renunciaAutor.getRenuncia());
                    renunciaAutor.setId(renunciaAutor.getRenuncia().getId());
                    renunciaAutorFacadeLocal.create(renunciaAutor);
                    autorProyectoFacadeLocal.edit(autorProyecto);
                    if (param.equalsIgnoreCase("grabar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarRenunciaAutorProyecto').hide()");
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para crear Renuncia. Consulte con el administrador del Sistema.", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_renuncia_director");
                if (tienePermiso == 1) {
                    renunciaFacadeLocal.edit(renunciaAutor.getRenuncia());
                    renunciaAutorFacadeLocal.edit(renunciaAutor);
                    autorProyectoFacadeLocal.edit(autorProyecto);
                    if (param.equalsIgnoreCase("grabar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarRenunciaDirectorProyecto').hide()");
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para editar Renuncia. Consulte con el administrador del Sistema.", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void eliminar(RenunciaAutor renunciaAutor, AutorProyecto autorProyecto, Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_renuncia_autor");
            EstadoAutor estadoAutor = estadoAutorFacadeLocal.find(3);
            if (tienePermiso == 1) {
                renunciaAutorFacadeLocal.remove(renunciaAutor);
                renunciaFacadeLocal.remove(renunciaAutor.getRenuncia());
                if (renunciaAutorFacadeLocal.buscarPorAutorProyecto(autorProyecto.getId()).isEmpty()) {
                    autorProyecto.setEstadoAutorId(estadoAutor);
                    autorProyectoFacadeLocal.edit(autorProyecto);
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

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionRenunciaAutor getSessionRenunciaAutor() {
        return sessionRenunciaAutor;
    }

    public void setSessionRenunciaAutor(SessionRenunciaAutor sessionRenunciaAutor) {
        this.sessionRenunciaAutor = sessionRenunciaAutor;
    }

    public EstadoAutorFacadeLocal getEstadoAutorFacadeLocal() {
        return estadoAutorFacadeLocal;
    }

    public void setEstadoAutorFacadeLocal(EstadoAutorFacadeLocal estadoAutorFacadeLocal) {
        this.estadoAutorFacadeLocal = estadoAutorFacadeLocal;
    }

    public SessionAutorProyecto getSessionAutorProyecto() {
        return sessionAutorProyecto;
    }

    public void setSessionAutorProyecto(SessionAutorProyecto sessionAutorProyecto) {
        this.sessionAutorProyecto = sessionAutorProyecto;
    }

    public List<RenunciaAutor> getRenunciaAutors() {
        return renunciaAutors;
    }

    public void setRenunciaAutors(List<RenunciaAutor> renunciaAutors) {
        this.renunciaAutors = renunciaAutors;
    }
//</editor-fold>
}

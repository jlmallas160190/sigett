/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.rol;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.usuario.UsuarioDM;
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
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.PermisoDao;
import org.jlmallas.seguridad.dao.RolDao;
import org.jlmallas.seguridad.dao.RolPermisoDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.entity.Permiso;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.RolPermiso;
import org.jlmallas.seguridad.entity.Usuario;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author JoobjgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarRol",
            pattern = "/editarRol/#{sessionRol.rol.id}",
            viewId = "/faces/pages/academico/roles/editarRol.xhtml"
    ),
    @URLMapping(
            id = "crearRol",
            pattern = "/crearRol/",
            viewId = "/faces/pages/academico/roles/editarRol.xhtml"
    ),
    @URLMapping(
            id = "roles",
            pattern = "/roles/",
            viewId = "/faces/pages/academico/roles/index.xhtml"
    )})
public class AdministrarRoles implements Serializable {

    @Inject
    private UsuarioDM sessionUsuario;
    @Inject
    private SessionRol sessionRol;

    @EJB
    private RolDao rolFacadeLocal;
    @EJB
    private PermisoDao permisoFacadeLocal;
    @EJB
    private RolPermisoDao rolPermisoFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;

    private DualListModel<Permiso> permisosDualList;
    private List<RolPermiso> rolPermisosRemovidos;
    private List<Rol> roles;
    private List<RolPermiso> rolPermisosGrabar;

    private int numeroPermisosDisponibles = 0;
    private int numeroPermisosSeleccionados = 0;
    private String criterio;

    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;

    public AdministrarRoles() {
        permisosDualList = new DualListModel<>();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_rol");
            if (tienePermiso == 1) {
                sessionRol.setRol(new Rol());
                listarPermisos(new Rol());
                rolPermisosRemovidos = new ArrayList<>();
                rolPermisosGrabar = new ArrayList<>();
                navegacion = "pretty:crearRol";
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

    public void guardarRolesPermiso(Rol rol) {
        for (RolPermiso rolPermiso : rolPermisosGrabar) {
            rolPermiso.setRolId(rol);
            if (contienePermiso(rolPermisoFacadeLocal.buscarPorRol(rol.getId()), rolPermiso) == false) {
                rolPermisoFacadeLocal.create(rolPermiso);
                logFacadeLocal.create(logFacadeLocal.crearLog("RolPermiso", rolPermiso.getId() + "", "CREAR", "|Rol= " + rolPermiso.getRolId() + "|Permiso= " + rolPermiso.getPermisoId(), sessionUsuario.getUsuario()));
            }
        }
    }

    public void buscar(String criterio, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_rol");
            if (tienePermiso == 1) {
                roles = rolFacadeLocal.buscarPorCriterio(new Rol());
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public String abrirBuscarRoles(Usuario usuario) {
        String navegacion = "";
        try {
            renderedCrear(usuario);
            renderedEditar(usuario);
            navegacion = "pretty:roles";
        } catch (Exception e) {
        }
        return navegacion;
    }

    public boolean contienePermiso(List<RolPermiso> rolPermisos, RolPermiso rolPermiso) {
        boolean var = false;
        for (RolPermiso rp : rolPermisos) {
            if (rp.getPermisoId().equals(rolPermiso.getPermisoId())) {
                var = true;
                break;
            }
        }
        return var;
    }

    public Long devuelvePermisoEliminar(List<RolPermiso> rolPermisos, RolPermiso rolPermiso) {
        Long var = (long) 0;
        for (RolPermiso rp : rolPermisos) {
            if (rp.getPermisoId().equals(rolPermiso.getPermisoId())) {
                var = rp.getId();
                break;
            }
        }
        return var;
    }

    public void transferRolPermisos(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                Permiso permiso = permisoFacadeLocal.find(id);
                RolPermiso rolPermiso = new RolPermiso();
                rolPermiso.setPermisoId(permiso);
                if (event.isRemove()) {
                    rolPermisosRemovidos.add(rolPermiso);
                    rolPermisosGrabar.remove(rolPermiso);
                } else {
                    if (contienePermiso(rolPermisosRemovidos, rolPermiso)) {
                        rolPermisosRemovidos.remove(rolPermiso);
                    }
                    rolPermisosGrabar.add(rolPermiso);
                }
                numeroPermisosDisponibles = permisosDualList.getSource().size();
                numeroPermisosSeleccionados = permisosDualList.getTarget().size();
            }

        } catch (Exception e) {
        }
    }

    public String editar(Rol rol, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_rol");
            if (tienePermiso == 1) {
                sessionRol.setRol(rol);
                listarPermisos(rol);
                rolPermisosRemovidos = new ArrayList<>();
                rolPermisosGrabar = new ArrayList<>();
                navegacion = "pretty:editarRol";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }

        return navegacion;
    }

    public void listarPermisos(Rol rol) {
        List<Permiso> permisosRol = new ArrayList<>();
        List<Permiso> permisos = new ArrayList<>();
        try {
            if (rol.getId() != null) {
                for (RolPermiso rolPermiso : rolPermisoFacadeLocal.buscarPorRol(rol.getId())) {
                    permisosRol.add(rolPermiso.getPermisoId());
                }
                for (Permiso p : permisoFacadeLocal.findAll()) {
                    if (!permisosRol.contains(p)) {
                        permisos.add(p);
                    }
                }
            } else {
                permisos = permisoFacadeLocal.findAll();
            }
            permisosDualList = new DualListModel<>(permisos, permisosRol);
            numeroPermisosDisponibles = permisosDualList.getSource().size();
            numeroPermisosSeleccionados = permisosDualList.getTarget().size();
        } catch (Exception e) {
        }
    }

    public void removerRolPermisos(Rol rol) {
        if (rol.getId() != null) {
            for (RolPermiso rolPermiso : rolPermisosRemovidos) {
                Long id = devuelvePermisoEliminar(rolPermisoFacadeLocal.buscarPorRol(rol.getId()), rolPermiso);
                RolPermiso rp = null;
                rp = rolPermisoFacadeLocal.find(id);
                if (rp != null) {
                    logFacadeLocal.create(logFacadeLocal.crearLog("RolPermiso", rp.getId() + "", "ELIMINAR", "|Rol=" + rp.getRolId() + "|Permiso=" + rp.getPermisoId(), sessionUsuario.getUsuario()));
                    rolPermisoFacadeLocal.remove(rp);
                }
            }
        }
    }

    public String guardar(Rol rol, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (rol.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_rol");
                if (tienePermiso == 1) {
                    List<RolPermiso> rolPermisos = new ArrayList<>();
                    rolPermisos = rolPermisoFacadeLocal.buscarPorRol(rol.getId());
                    rol.setRolPermisoList(new ArrayList<RolPermiso>());
                    rolFacadeLocal.create(rol);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Rol", rol.getId() + "", "CREAR", "|Nombre= " + rol.getNombre(), usuario));
                    guardarRolesPermiso(rol);
                    rol.getRolPermisoList().addAll(rolPermisos);
                    if (param.equalsIgnoreCase("guardar")) {
                        sessionRol.setRol(new Rol());
                        navegacion = "pretty:roles";
                    } else {
                        if (param.equalsIgnoreCase("guardar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            guardarRolesPermiso(rol);
                            sessionRol.setRol(new Rol());
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
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_rol");
                if (tienePermiso == 1) {
                    List<RolPermiso> rolPermisos = rolPermisoFacadeLocal.buscarPorRol(rol.getId());
                    rolPermisos = rol.getRolPermisoList();
                    rol.setRolPermisoList(new ArrayList<RolPermiso>());
                    rolFacadeLocal.edit(rol);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Rol", rol.getId() + "", "EDITAR", "|Nombre= " + rol.getNombre(), sessionUsuario.getUsuario()));
                    guardarRolesPermiso(rol);
                    removerRolPermisos(rol);
                    rol.getRolPermisoList().addAll(rolPermisos);
                    if (param.equalsIgnoreCase("guardar")) {
                        sessionRol.setRol(new Rol());
                        navegacion = "pretty:roles";
                    } else {
                        if (param.equalsIgnoreCase("guardar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            sessionRol.setRol(new Rol());
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }

                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_rol");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }

    public void renderedEditar(Usuario usuario) {

        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_rol");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedEditar = false;
            renderedNoEditar = true;
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public List<RolPermiso> getRolPermisosRemovidos() {
        return rolPermisosRemovidos;
    }

    public void setRolPermisosRemovidos(List<RolPermiso> rolPermisosRemovidos) {
        this.rolPermisosRemovidos = rolPermisosRemovidos;
    }

    public SessionRol getSessionRol() {
        return sessionRol;
    }

    public void setSessionRol(SessionRol sessionRol) {
        this.sessionRol = sessionRol;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public DualListModel<Permiso> getPermisosDualList() {
        return permisosDualList;
    }

    public void setPermisosDualList(DualListModel<Permiso> permisosDualList) {
        this.permisosDualList = permisosDualList;
    }

    public int getNumeroPermisosDisponibles() {
        return numeroPermisosDisponibles;
    }

    public void setNumeroPermisosDisponibles(int numeroPermisosDisponibles) {
        this.numeroPermisosDisponibles = numeroPermisosDisponibles;
    }

    public int getNumeroPermisosSeleccionados() {
        return numeroPermisosSeleccionados;
    }

    public void setNumeroPermisosSeleccionados(int numeroPermisosSeleccionados) {
        this.numeroPermisosSeleccionados = numeroPermisosSeleccionados;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
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

    public List<RolPermiso> getRolPermisosGrabar() {
        return rolPermisosGrabar;
    }

    public void setRolPermisosGrabar(List<RolPermiso> rolPermisosGrabar) {
        this.rolPermisosGrabar = rolPermisosGrabar;
    }
//</editor-fold>
}

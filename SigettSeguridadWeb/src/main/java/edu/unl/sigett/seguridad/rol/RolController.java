/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.rol;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.usuario.UsuarioDM;
import edu.unl.sigett.util.CabeceraController;
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
import org.jlmallas.seguridad.dao.RolPermisoDao;
import org.jlmallas.seguridad.entity.Permiso;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.RolPermiso;
import org.jlmallas.seguridad.service.RolService;
import org.jlmallas.seguridad.service.UsuarioService;
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
            viewId = "/faces/pages/seguridad/roles/editarRol.xhtml"
    ),
    @URLMapping(
            id = "crearRol",
            pattern = "/crearRol/",
            viewId = "/faces/pages/seguridad/roles/editarRol.xhtml"
    ),
    @URLMapping(
            id = "roles",
            pattern = "/roles/",
            viewId = "/faces/pages/seguridad/roles/index.xhtml"
    )})
public class RolController implements Serializable {

    @Inject
    private UsuarioDM usuarioDM;
    @Inject
    private SessionRol sessionRol;
    @Inject
    private CabeceraController cabeceraController;

    @EJB(lookup = "java:global/SeguridadService/RolServiceImplement!org.jlmallas.seguridad.service.RolService")
    private RolService rolService;
    @EJB(lookup = "java:global/SeguridadService/PermisoDaoImplement!org.jlmallas.seguridad.dao.PermisoDao")
    private PermisoDao permisoDao;
    @EJB(lookup = "java:global/SeguridadService/RolPermisoDaoImplement!org.jlmallas.seguridad.dao.RolPermisoDao")
    private RolPermisoDao rolPermisoDao;
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logDao;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;

    public RolController() {

    }

    public void preRenderView() {
        sessionRol.setPermisosDualList(new DualListModel<Permiso>());
        buscar();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear() {
        String navegacion = "";
        try {
            sessionRol.setRol(new Rol());
            listarPermisos(new Rol());
            sessionRol.setRolPermisosRemovidos(new ArrayList<RolPermiso>());
            sessionRol.getRolPermisosGrabar().clear();
            navegacion = "pretty:crearRol";
        } catch (Exception e) {
        }

        return navegacion;
    }

    private void guardarRolesPermiso(Rol rol) {
        for (RolPermiso rolPermiso : sessionRol.getRolPermisosGrabar()) {
            rolPermiso.setRolId(rol);
            if (contienePermiso(rolPermisoDao.buscarPorRol(rol.getId()), rolPermiso) == false) {
                rolPermisoDao.create(rolPermiso);
                logDao.create(logDao.crearLog("RolPermiso", rolPermiso.getId() + "", "CREAR", "|Rol= " + rolPermiso.getRolId()
                        + "|Permiso= " + rolPermiso.getPermisoId(), usuarioDM.getUsuario()));
            }
        }
    }

    private void buscar() {
        try {
            sessionRol.setRoles(rolService.buscar(new Rol()));
        } catch (Exception e) {
        }
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
                Permiso permiso = permisoDao.find(id);
                RolPermiso rolPermiso = new RolPermiso();
                rolPermiso.setPermisoId(permiso);
                if (event.isRemove()) {
                    sessionRol.getRolPermisosRemovidos().add(rolPermiso);
                    sessionRol.getRolPermisosGrabar().remove(rolPermiso);
                } else {
                    if (contienePermiso(sessionRol.getRolPermisosRemovidos(), rolPermiso)) {
                        sessionRol.getRolPermisosRemovidos().remove(rolPermiso);
                    }
                    sessionRol.getRolPermisosGrabar().add(rolPermiso);
                }
                sessionRol.setNumeroPermisosDisponibles(sessionRol.getPermisosDualList().getSource().size());
                sessionRol.setNumeroPermisosSeleccionados(sessionRol.getPermisosDualList().getTarget().size());
            }

        } catch (NumberFormatException e) {
        }
    }

    public String editar(Rol rol) {
        String navegacion = "";
        try {
            sessionRol.setRol(rol);
            listarPermisos(rol);
            sessionRol.setRolPermisosRemovidos(new ArrayList<RolPermiso>());
            sessionRol.getRolPermisosGrabar().clear();
            navegacion = "pretty:editarRol";
        } catch (Exception e) {
            System.out.println(e);
        }

        return navegacion;
    }

    public void listarPermisos(Rol rol) {
        List<Permiso> permisosRol = new ArrayList<>();
        List<Permiso> permisos = new ArrayList<>();
        try {
            if (rol.getId() != null) {
                for (RolPermiso rolPermiso : rolPermisoDao.buscarPorRol(rol.getId())) {
                    permisosRol.add(rolPermiso.getPermisoId());
                }
                for (Permiso p : permisoDao.findAll()) {
                    if (!permisosRol.contains(p)) {
                        permisos.add(p);
                    }
                }
            } else {
                permisos = permisoDao.findAll();
            }
            sessionRol.setPermisosDualList(new DualListModel<>(permisos, permisosRol));
            sessionRol.setNumeroPermisosDisponibles(sessionRol.getPermisosDualList().getSource().size());
            sessionRol.setNumeroPermisosSeleccionados(sessionRol.getPermisosDualList().getTarget().size());
        } catch (Exception e) {
        }
    }

    public void removerRolPermisos(Rol rol) {
        if (rol.getId() != null) {
            for (RolPermiso rolPermiso : sessionRol.getRolPermisosRemovidos()) {
                Long id = devuelvePermisoEliminar(rolPermisoDao.buscarPorRol(rol.getId()), rolPermiso);
                RolPermiso rp = null;
                rp = rolPermisoDao.find(id);
                if (rp != null) {
                    logDao.create(logDao.crearLog("RolPermiso", rp.getId() + "", "ELIMINAR", "|Rol=" + rp.getRolId() + "|Permiso=" + rp.getPermisoId(), usuarioDM.getUsuario()));
                    rolPermisoDao.remove(rp);
                }
            }
        }
    }

    public String guardar(Rol rol) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (rol.getId() == null) {
                @SuppressWarnings("UnusedAssignment")
                List<RolPermiso> rolPermisos = new ArrayList<>();
                rolPermisos = rolPermisoDao.buscarPorRol(rol.getId());
                rol.setRolPermisoList(new ArrayList<RolPermiso>());
                rolService.guardar(rol);
                logDao.create(logDao.crearLog("Rol", rol.getId() + "", "CREAR", "|Nombre= " + rol.getNombre(), usuarioDM.getUsuario()));
                guardarRolesPermiso(rol);
                rol.getRolPermisoList().addAll(rolPermisos);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("grabar"), "");
                if (param.equalsIgnoreCase("guardar")) {
                    sessionRol.setRol(new Rol());
                    return "pretty:roles";
                }
                return "";
            }
            @SuppressWarnings("UnusedAssignment")
            List<RolPermiso> rolPermisos = rolPermisoDao.buscarPorRol(rol.getId());
            rolPermisos = rol.getRolPermisoList();
            rol.setRolPermisoList(new ArrayList<RolPermiso>());
            rolService.actualizar(rol);
            logDao.create(logDao.crearLog("Rol", rol.getId() + "", "EDITAR", "|Nombre= " + rol.getNombre(), usuarioDM.getUsuario()));
            guardarRolesPermiso(rol);
            removerRolPermisos(rol);
            rol.getRolPermisoList().addAll(rolPermisos);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("editar"), "");
            if (param.equalsIgnoreCase("guardar")) {
                sessionRol.setRol(new Rol());
                return "pretty:roles";
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

}

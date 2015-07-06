/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.usuario;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Carrera;
import org.jlmallas.seguridad.entity.Permiso;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.RolUsuario;
import org.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
import org.jlmallas.seguridad.entity.UsuarioPermiso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import edu.jlmallas.academico.service.CarreraService;
import org.jlmallas.seguridad.dao.PermisoDao;
import org.jlmallas.seguridad.dao.RolDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import edu.unl.sigett.util.CabeceraController;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import org.jlmallas.secure.Secure;
import org.jlmallas.seguridad.dao.UsuarioPermisoDao;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarUsuario",
            pattern = "/editarUsuario/#{usuarioCRUDDM.usuario.id}",
            viewId = "/faces/pages/seguridad/usuarios/editarUsuario.xhtml"
    ),
    @URLMapping(
            id = "crearUsuario",
            pattern = "/crearUsuario/",
            viewId = "/faces/pages/seguridad/usuarios/crearUsuario.xhtml"
    ),
    @URLMapping(
            id = "usuarios",
            pattern = "/usuarios/",
            viewId = "/faces/pages/seguridad/usuarios/index.xhtml"
    ),
    @URLMapping(
            id = "cambiarClave",
            pattern = "/cambiarClave/#{usuarioCRUDDM.usuario.id}",
            viewId = "/faces/pages/seguridad/usuarios/cambiarClave.xhtml"
    ),})
public class UsuarioController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private UsuarioCRUDDM usuarioCRUDDM;
    @Inject
    private UsuarioDM usuarioDM;
    @Inject
    private CabeceraController cabeceraController;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/AcademicoService/CarreraServiceImplement!edu.jlmallas.academico.service.CarreraService")
    private CarreraService carreraService;
    @EJB(lookup = "java:global/SigettService/UsuarioCarreraDaoImplement!edu.unl.sigett.dao.UsuarioCarreraDao")
    private UsuarioCarreraDao usuarioCarreraDao;
    @EJB(lookup = "java:global/SeguridadService/RolDaoImplement!org.jlmallas.seguridad.dao.RolDao")
    private RolDao rolDao;
    @EJB(lookup = "java:global/SeguridadService/RolUsuarioDaoImplement!org.jlmallas.seguridad.dao.RolUsuarioDao")
    private RolUsuarioDao rolUsuarioDao;
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logDao;
    @EJB(lookup = "java:global/SeguridadService/PermisoDaoImplement!org.jlmallas.seguridad.dao.PermisoDao")
    private PermisoDao permisoDao;
    @EJB(lookup = "java:global/SeguridadService/UsuarioPermisoDaoImplement!org.jlmallas.seguridad.dao.UsuarioPermisoDao")
    private UsuarioPermisoDao usuarioPermisoDao;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
//</editor-fold>
    private static final Logger LOG = Logger.getLogger(UsuarioController.class.getName());

    @PostConstruct
    public void AdministrarUsuarios() {
        usuarioCRUDDM.setRolesDualList(new DualListModel<Rol>());
        usuarioCRUDDM.setPermisosDualList(new DualListModel<Permiso>());
        usuarioCRUDDM.setCarrerasDualList(new DualListModel<Carrera>());
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String editar(Usuario usuario, Usuario session) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(session, "editar_usuario");
            if (tienePermiso == 1) {
                usuario = usuarioService.buscarPorId(new Usuario(usuario.getId()));
                usuarioCRUDDM.setUsuario(usuario);
                usuarioCRUDDM.setRolesUsuariosRemovidos(new ArrayList<RolUsuario>());
                usuarioCRUDDM.setUsuariosPermisoRemovidos(new ArrayList<UsuarioPermiso>());
                usuarioCRUDDM.setUsuariosCarrerasRemovidas(new ArrayList<UsuarioCarrera>());
                listarRoles(usuario);
                listarPermisos(usuario);
                listarCarreras(usuario);
                return ("pretty:editarUsuario");
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return "";
    }

    public String abrirBuscarUsuarios(Usuario usuario) {
        try {
            renderedCrear(usuario);
            renderedEditar(usuario);
            buscar();
            return "pretty:usuarios";
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return "";
    }

    public String crear(Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(usuario, "crear_usuario");
            if (tienePermiso == 1) {
                usuarioCRUDDM.setUsuario(new Usuario());
                listarRoles(usuarioCRUDDM.getUsuario());
                listarPermisos(usuarioCRUDDM.getUsuario());
                listarCarreras(usuarioCRUDDM.getUsuario());
                usuarioCRUDDM.setRolesUsuariosRemovidos(new ArrayList<RolUsuario>());
                usuarioCRUDDM.setUsuariosCarrerasRemovidas(new ArrayList<UsuarioCarrera>());
                usuarioCRUDDM.setUsuariosPermisoRemovidos(new ArrayList<UsuarioPermiso>());
                return ("pretty:crearUsuario");
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return "";
    }

    public void buscar() {
        try {
            usuarioCRUDDM.getUsuarios().clear();
            usuarioCRUDDM.getFilterUsuarios().clear();
            Usuario usuarioBuscar = new Usuario();
            usuarioBuscar.setApellidos("");
            usuarioCRUDDM.setUsuarios(usuarioService.buscar(usuarioBuscar));
            usuarioCRUDDM.setFilterUsuarios(usuarioCRUDDM.getUsuarios());

        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public void guardarUsuariosCarreras() {
        List<UsuarioCarrera> usuarioCarreras = new ArrayList<>();
        for (Object o : usuarioCRUDDM.getCarrerasDualList().getTarget()) {
            int pos = o.toString().indexOf(":");
            Integer id = Integer.parseInt(o.toString().substring(0, pos));
            Carrera c = carreraService.find(id);
            UsuarioCarrera usuarioCarrera = new UsuarioCarrera();
            usuarioCarrera.setCarreraId(c.getId());
            usuarioCarrera.setUsuarioId(usuarioCRUDDM.getUsuario().getId());
            usuarioCarreras.add(usuarioCarrera);
        }
        for (UsuarioCarrera uc : usuarioCarreras) {
            if (contieneCarrera(usuarioCarreraDao.buscarPorUsuario(usuarioCRUDDM.getUsuario().getId()), uc) == false) {
                usuarioCarreraDao.create(uc);
            }
        }
    }

    public void guardarUsuariosPermiso() {
        List<UsuarioPermiso> usuarioPermisos = new ArrayList<>();
        for (Object permiso : usuarioCRUDDM.getPermisosDualList().getTarget()) {
            int pos = permiso.toString().indexOf(":");
            Long id = Long.parseLong(permiso.toString().substring(0, pos));
            Permiso p = permisoDao.find(id);
            UsuarioPermiso usuarioPermiso = new UsuarioPermiso();
            usuarioPermiso.setPermisoId(p);
            usuarioPermiso.setUsuarioId(usuarioCRUDDM.getUsuario());
            usuarioPermisos.add(usuarioPermiso);
        }
        for (UsuarioPermiso usuarioPermiso : usuarioPermisos) {
            if (contienePermiso(usuarioPermisoDao.buscarPorUsuario(usuarioCRUDDM.getUsuario().getId()), usuarioPermiso) == false) {
                usuarioPermisoDao.create(usuarioPermiso);
            }
        }
    }

    public void guardarRolesUsuario() {
        List<RolUsuario> rolUsuarios = new ArrayList<>();
        for (Object r : usuarioCRUDDM.getRolesDualList().getTarget()) {
            int v = r.toString().indexOf(":");
            Long id = Long.parseLong(r.toString().substring(0, v));
            Rol rol = rolDao.find(id);
            RolUsuario rolUsuario = new RolUsuario();
            rolUsuario.setRolId(rol);
            rolUsuario.setUsuarioId(usuarioCRUDDM.getUsuario());
            rolUsuarios.add(rolUsuario);
        }
        for (RolUsuario rolUsuario : rolUsuarios) {
            if (contieneRol(rolUsuarioDao.buscarPorUsuario(usuarioCRUDDM.getUsuario().getId()), rolUsuario) == false) {
                rolUsuarioDao.create(rolUsuario);
                logDao.create(logDao.crearLog("RolUsuario", rolUsuario.getId() + "", "CREAR", "|Rol=" + rolUsuario.getRolId()
                        + "|Usuario=" + rolUsuario.getUsuarioId(), usuarioCRUDDM.getUsuario()));
            }
        }
    }

    /**
     * GUARDAR USUARIO
     *
     * @return
     */
    public String guardar() {
        try {
            Usuario usuarioBuscar = new Usuario();
            usuarioBuscar.setUsername(usuarioCRUDDM.getUsuario().getUsername());
            List<Usuario> usuarios = usuarioService.buscar(usuarioBuscar);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (usuarioService.unicoUsername(usuarioCRUDDM.getUsuario().getUsername()) == false || usuarioService.buscarPorId(new Usuario(usuarioCRUDDM.getUsuario().getId())).
                    equals(!usuarios.isEmpty() ? usuarios.get(0) : null)) {
                usuarioCRUDDM.getUsuario().setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                usuarioService.actualizar(usuarioCRUDDM.getUsuario());
                logDao.create(logDao.crearLog("Usuario", usuarioCRUDDM.getUsuario().getId() + "", "EDITAR", "|Username= "
                        + usuarioCRUDDM.getUsuario().getUsername() + "|EsActivo=" + usuarioCRUDDM.getUsuario().getEsActivo() + "|EsSuperuser= "
                        + usuarioCRUDDM.getUsuario().getEsSuperuser()
                        + "|Nombres= " + usuarioCRUDDM.getUsuario().getNombres() + "|Apellidos= " + usuarioCRUDDM.getUsuario().getApellidos()
                        + "|Email= "
                        + usuarioCRUDDM.getUsuario().getEmail(), usuarioDM.getUsuario()));
                guardarRolesUsuario();
                removerRolUsuarios();
                guardarUsuariosPermiso();
                removerUsuariosPermisos();
                guardarUsuariosCarreras();
                removerUsuariosCarreras();
                buscar();
                if (param.equalsIgnoreCase("guardar")) {
                    usuarioCRUDDM.setUsuario(new Usuario());
                    return ("pretty:usuarios");
                }
                if (param.equalsIgnoreCase("guardar-editar")) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario") + " "
                        + bundle.getString("lbl.msm_existe"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }

        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return "";
    }

    /**
     * GUARDAR UN USUARIO CON LA CLAVE ENCRIPTADA
     *
     * @param usuario
     * @return
     */
    public String guardarConClave(Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (!usuarioCRUDDM.getUsuario().getPassword().equalsIgnoreCase(usuarioCRUDDM.getConfirmarClave())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_conciden_claves"), "");
                return "";
            }
            if (usuarioCRUDDM.getUsuario().getId() == null) {
                if (usuarioService.unicoUsername(usuarioCRUDDM.getUsuario().getUsername()) == false) {
                    usuarioCRUDDM.getUsuario().setPassword(cabeceraController.getSecureService().encrypt(
                            new Secure(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(), usuario.getPassword())));
                    usuarioCRUDDM.getUsuario().setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                    usuarioService.guardar(usuarioCRUDDM.getUsuario());
                    logDao.create(logDao.crearLog("Usuario", usuarioCRUDDM.getUsuario().getId() + "", "CREAR", "|Username= "
                            + usuarioCRUDDM.getUsuario().getUsername() + "|EsActivo=" + usuarioCRUDDM.getUsuario().getEsActivo()
                            + "|EsSuperuser= " + usuarioCRUDDM.getUsuario().getEsSuperuser() + "|Nombres= " + usuarioCRUDDM.getUsuario().getNombres()
                            + "|Apellidos= " + usuarioCRUDDM.getUsuario().getApellidos() + "|Email= " + usuarioCRUDDM.getUsuario().getEmail(), usuario));
                    guardarRolesUsuario();
                    removerRolUsuarios();
                    guardarUsuariosPermiso();
                    removerUsuariosPermisos();
                    guardarUsuariosCarreras();
                    removerUsuariosCarreras();
                    buscar();
                    if (param.equalsIgnoreCase("guardar")) {
                        usuarioCRUDDM.setUsuario(new Usuario());
                        return ("pretty:usuarios");
                    }

                    if (param.equalsIgnoreCase("guardar-editar")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        return "";
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario") + " " + bundle.getString("lbl.msm_existe"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
                return "";
            }
            Usuario usuarioBuscar = new Usuario();
            usuarioBuscar.setUsername(usuarioCRUDDM.getUsuario().getUsername());
            List<Usuario> usuarios = usuarioService.buscar(usuarioBuscar);
            if (usuarioService.unicoUsername(usuarioCRUDDM.getUsuario().getUsername()) == false
                    || usuarioService.buscarPorId(new Usuario(usuarioCRUDDM.getUsuario().getId()))
                    .equals(!usuarios.isEmpty() ? usuarios.get(0) : null)) {
                usuarioCRUDDM.getUsuario().setPassword(cabeceraController.getSecureService().encrypt(
                        new Secure(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(), usuarioCRUDDM.getUsuario().getPassword())));
                usuarioCRUDDM.getUsuario().setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                usuarioService.actualizar(usuarioCRUDDM.getUsuario());
                logDao.create(logDao.crearLog("Usuario", usuarioCRUDDM.getUsuario().getId() + "", "EDITAR", "|Username= "
                        + usuarioCRUDDM.getUsuario().getUsername() + "|EsActivo=" + usuarioCRUDDM.getUsuario().getEsActivo() + "|EsSuperuser= "
                        + usuarioCRUDDM.getUsuario().getEsSuperuser() + "|Nombres= " + usuarioCRUDDM.getUsuario().getNombres() + "|Apellidos= "
                        + usuarioCRUDDM.getUsuario().getApellidos() + "|Email= " + usuarioCRUDDM.getUsuario().getEmail(), usuario));
                guardarRolesUsuario();
                removerRolUsuarios();
                buscar();
                if (param.equalsIgnoreCase("guardar")) {
                    usuarioCRUDDM.setUsuario(new Usuario());
                    return ("pretty:usuarios");
                }
                if (param.equalsIgnoreCase("grabar-editar")) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario") + " " + bundle.getString("lbl.msm_existe"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return "";
    }

    public boolean contieneCarrera(List<UsuarioCarrera> usuarioCarreras, UsuarioCarrera usuarioCarrera) {
        boolean var = false;
        if (usuarioCarreras != null) {
            for (UsuarioCarrera uc : usuarioCarreras) {
                Carrera carrera = carreraService.find(uc.getCarreraId());
                Carrera carreraBuscar = carreraService.find(usuarioCarrera.getCarreraId());
                if (carrera.equals(carreraBuscar)) {
                    var = true;
                    break;
                }
            }
        }
        return var;
    }

    public boolean contienePermiso(List<UsuarioPermiso> usuarioPermisos, UsuarioPermiso usuarioPermiso) {
        boolean var = false;
        for (UsuarioPermiso up : usuarioPermisos) {
            if (up.getPermisoId().equals(usuarioPermiso.getPermisoId())) {
                var = true;
                break;
            }
        }
        return var;
    }

    public boolean contieneRol(List<RolUsuario> rolUsuarios, RolUsuario rolUsuario) {
        boolean var = false;
        for (RolUsuario r : rolUsuarios) {
            if (rolUsuario.getRolId().equals(r.getRolId())) {
                var = true;
                break;
            }
        }
        return var;
    }

    public Long devuelveCarreraEliminar(List<UsuarioCarrera> usuarioCarreras, UsuarioCarrera usuarioCarrera) {
        Long var = (long) 0;
        for (UsuarioCarrera uc : usuarioCarreras) {
            Carrera carrera = carreraService.find(uc.getCarreraId());
            Carrera carreraBuscar = carreraService.find(usuarioCarrera.getCarreraId());
            if (carrera.equals(carreraBuscar)) {
                var = uc.getId();
                break;
            }
        }
        return var;
    }

    public Long devuelvePermisoEliminar(List<UsuarioPermiso> usuarioPermisos, UsuarioPermiso usuarioPermiso) {
        Long var = (long) 0;
        for (UsuarioPermiso up : usuarioPermisos) {
            if (usuarioPermiso.getPermisoId().equals(up.getPermisoId())) {
                var = up.getId();
                break;
            }
        }
        return var;
    }

    public Long devuelveRolEliminar(List<RolUsuario> rolUsuarios, RolUsuario rolUsuario) {
        Long var = (long) 0;
        for (RolUsuario r : rolUsuarios) {
            if (rolUsuario.getRolId().equals(r.getRolId())) {
                var = r.getId();
                break;
            }
        }
        return var;
    }

    public void listarCarreras(Usuario sessionUsuario) {
        List<Carrera> usuarioCarreras = new ArrayList<>();
        List<Carrera> carreras = new ArrayList<>();
        try {
            if (usuarioCRUDDM.getUsuario() != null) {
                if (usuarioCRUDDM.getUsuario().getId() != null) {
                    for (UsuarioCarrera uc : usuarioCarreraDao.buscarPorUsuario(usuarioCRUDDM.getUsuario().getId())) {
                        Carrera carrera = carreraService.find(uc.getCarreraId());
                        usuarioCarreras.add(carrera);
                    }
                }
            }
            for (Carrera c : carreraService.findAll()) {
                if (!usuarioCarreras.contains(c)) {
                    carreras.add(c);
                }
            }
            usuarioCRUDDM.setCarrerasDualList(new DualListModel<>(carreras, usuarioCarreras));
            usuarioCRUDDM.setNumeroCarrerasDisponibles(usuarioCRUDDM.getCarrerasDualList().getSource().size());
            usuarioCRUDDM.setNumeroCarrerasSeleccionadas(usuarioCRUDDM.getCarrerasDualList().getTarget().size());
        } catch (Exception e) {

        }
    }

    public void listarPermisos(Usuario sessionUsuario) {
        List<Permiso> usuariosPermisos = new ArrayList<>();
        List<Permiso> permisos = new ArrayList<>();
        try {
            if (usuarioCRUDDM.getUsuario() != null) {
                if (usuarioCRUDDM.getUsuario().getId() != null) {
                    for (UsuarioPermiso usuarioPermiso : usuarioCRUDDM.getUsuario().getUsuarioPermisoList()) {
                        usuariosPermisos.add(usuarioPermiso.getPermisoId());
                    }
                }
            }
            for (Permiso permiso : permisoDao.findAll()) {
                if (!usuariosPermisos.contains(permiso)) {
                    permisos.add(permiso);
                }
            }
            usuarioCRUDDM.setPermisosDualList(new DualListModel<>(permisos, usuariosPermisos));
            usuarioCRUDDM.setNumeroPermisosDisponibles(usuarioCRUDDM.getPermisosDualList().getSource().size());
            usuarioCRUDDM.setNumeroPermisosSeleccionados(usuarioCRUDDM.getPermisosDualList().getTarget().size());
        } catch (Exception e) {

        }

    }

    public void removerUsuariosCarreras() {
        if (usuarioCRUDDM.getUsuario() != null) {
            if (usuarioCRUDDM.getUsuario().getId() != null) {
                for (UsuarioCarrera usuarioCarrera : usuarioCRUDDM.getUsuariosCarrerasRemovidas()) {
                    Long id = devuelveCarreraEliminar(usuarioCarreraDao.buscarPorUsuario(usuarioCRUDDM.getUsuario().getId()), usuarioCarrera);
                    UsuarioCarrera uc = new UsuarioCarrera();
                    uc = usuarioCarreraDao.find(id);
                    if (uc != null) {
                        usuarioCarreraDao.remove(uc);
                    }
                }
            }
        }
    }

    public void removerUsuariosPermisos() {
        if (usuarioCRUDDM.getUsuario() == null) {
            return;
        }
        if (usuarioCRUDDM.getUsuario().getId() != null) {
            for (UsuarioPermiso usuarioPermiso : usuarioCRUDDM.getUsuariosPermisoRemovidos()) {
                Long id = devuelvePermisoEliminar(usuarioPermisoDao.buscarPorUsuario(usuarioCRUDDM.getUsuario().getId()), usuarioPermiso);
                UsuarioPermiso up = new UsuarioPermiso();
                up = usuarioPermisoDao.find(id);
                if (up != null) {
                    usuarioPermisoDao.remove(up);
                }
            }
        }
    }

    public void removerRolUsuarios() {
        if (usuarioCRUDDM.getUsuario().getId() != null) {
            for (RolUsuario rolUsuario : usuarioCRUDDM.getRolesUsuariosRemovidos()) {
                Long id = devuelveRolEliminar(rolUsuarioDao.buscarPorUsuario(usuarioCRUDDM.getUsuario().getId()), rolUsuario);
                RolUsuario r = new RolUsuario();
                r = rolUsuarioDao.find(id);
                if (r != null) {
                    rolUsuarioDao.remove(r);
                }
            }
        }
    }

    public void transferUsuariosPermisos(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                Permiso p = permisoDao.find(id);
                UsuarioPermiso up = new UsuarioPermiso();
                up.setPermisoId(p);
                if (event.isRemove()) {
                    usuarioCRUDDM.getUsuariosPermisoRemovidos().add(up);
                } else {
                    if (contienePermiso(usuarioCRUDDM.getUsuariosPermisoRemovidos(), up)) {
                        usuarioCRUDDM.getUsuariosPermisoRemovidos().remove(up);
                    }
                }
                usuarioCRUDDM.setNumeroPermisosDisponibles(usuarioCRUDDM.getPermisosDualList().getSource().size());
                usuarioCRUDDM.setNumeroPermisosSeleccionados(usuarioCRUDDM.getPermisosDualList().getTarget().size());
            }

        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

    }

    public void transferUsuariosCarreras(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Integer id = Integer.parseInt(item.toString().substring(0, v));
                Carrera c = carreraService.find(id);
                UsuarioCarrera uc = new UsuarioCarrera();
                uc.setCarreraId(c.getId());
                if (event.isRemove()) {
                    usuarioCRUDDM.getUsuariosCarrerasRemovidas().add(uc);
                } else {
                    if (contieneCarrera(usuarioCRUDDM.getUsuariosCarrerasRemovidas(), uc)) {
                        usuarioCRUDDM.getUsuariosCarrerasRemovidas().remove(uc);
                    }
                }
                usuarioCRUDDM.setNumeroCarrerasDisponibles(usuarioCRUDDM.getCarrerasDualList().getSource().size());
                usuarioCRUDDM.setNumeroCarrerasSeleccionadas(usuarioCRUDDM.getCarrerasDualList().getTarget().size());
            }

        } catch (NumberFormatException e) {
            LOG.info(e.getMessage());
        }

    }

    public void transferRolUsuarios(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                Rol rol = rolDao.find(id);
                RolUsuario rolUsuario = new RolUsuario();
                rolUsuario.setRolId(rol);
                if (event.isRemove()) {
                    usuarioCRUDDM.getRolesUsuariosRemovidos().add(rolUsuario);
                } else {
                    if (contieneRol(usuarioCRUDDM.getRolesUsuariosRemovidos(), rolUsuario)) {
                        usuarioCRUDDM.getRolesUsuariosRemovidos().remove(rolUsuario);
                    }
                }
            }

        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public void listarRoles(Usuario usuario) {

        List<Rol> rolesUsuarios = new ArrayList<>();
        List<Rol> roles = new ArrayList<>();
        try {
            if (usuario != null) {
                if (usuario.getId() != null) {
                    for (RolUsuario rolUsuario : usuario.getRolUsuarioList()) {
                        rolesUsuarios.add(rolUsuario.getRolId());
                    }
                }
            }
            for (Rol r : rolDao.findAll()) {
                if (!rolesUsuarios.contains(r)) {
                    roles.add(r);
                }
            }
            roles = rolDao.findAll();
            usuarioCRUDDM.setRolesDualList(new DualListModel<>(roles, rolesUsuarios));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RENDERED">

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "crear_usuario");
        if (tienePermiso == 1) {
            usuarioCRUDDM.setRenderedCrear(true);
        } else {
            usuarioCRUDDM.setRenderedCrear(false);
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "editar_usuario");
        if (tienePermiso == 1) {
            usuarioCRUDDM.setRenderedEditar(true);
            usuarioCRUDDM.setRenderedNoEditar(false);
        } else {
            usuarioCRUDDM.setRenderedEditar(false);
            usuarioCRUDDM.setRenderedNoEditar(true);
        }
    }

    //</editor-fold>
}

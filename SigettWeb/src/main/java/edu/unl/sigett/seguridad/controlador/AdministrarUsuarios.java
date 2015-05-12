/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.dao.PersonaDao;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.managed.session.SessionAdminUsuario;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.DocenteUsuario;
import edu.jlmallas.academico.entity.Estudiante;
import edu.unl.sigett.entity.EstudianteUsuario;
import com.jlmallas.soporte.entity.Objeto;
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
import edu.unl.sigett.dao.DocenteUsuarioDao;
import edu.unl.sigett.dao.EstudianteUsuarioFacadeLocal;
import com.jlmallas.soporte.session.ExcepcionFacadeLocal;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import org.jlmallas.seguridad.dao.PermisoDao;
import com.jlmallas.soporte.session.ProyectoSoftwareFacadeLocal;
import org.jlmallas.seguridad.dao.RolDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioPermisoDao;
import org.jlmallas.seguridad.dao.LogDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarUsuario",
            pattern = "/editarUsuario/#{sessionAdminUsuario.usuario.id}",
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
            pattern = "/cambiarClave/#{sessionAdminUsuario.usuario.id}",
            viewId = "/faces/pages/seguridad/usuarios/cambiarClave.xhtml"
    ),
    @URLMapping(
            id = "cambiarClaveEstudiante",
            pattern = "/cambiarClaveEstudiante",
            viewId = "/faces/cambiarClaveEstudiante.xhtml"
    ),
    @URLMapping(
            id = "cambiarClaveDocente",
            pattern = "/cambiarClaveDocente/",
            viewId = "/faces/cambiarClaveDocente.xhtml"
    )})
public class AdministrarUsuarios implements Serializable {

    @Inject
    private SessionAdminUsuario sessionAdminUsuario;

    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private UsuarioCarreraDao usuarioCarreraDao;
    @EJB
    private RolDao rolDao;
    @EJB
    private RolUsuarioDao rolUsuarioFacadeLocal;
    @EJB
    private LogDao logDao;
    @EJB
    private ExcepcionFacadeLocal excepcionFacadeLocal;
    @EJB
    private ObjetoFacadeLocal objetoFacadeLocal;
    @EJB
    private ProyectoSoftwareFacadeLocal proyectoSoftwareFacadeLocal;
    @EJB
    private PermisoDao permisoFacadeLocal;
    @EJB
    private UsuarioPermisoDao usuarioPermisoFacadeLocal;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private EstudianteUsuarioFacadeLocal estudianteUsuarioFacadeLocal;
    @EJB
    private DocenteUsuarioDao docenteUsuarioFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private ConfiguracionDao configuracionFacadeLocal;

    @PostConstruct
    public void AdministrarUsuarios() {
        sessionAdminUsuario.setRolesDualList(new DualListModel<Rol>());
        sessionAdminUsuario.setPermisosDualList(new DualListModel<Permiso>());
        sessionAdminUsuario.setCarrerasDualList(new DualListModel<Carrera>());
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String editar(Usuario usuario, Usuario session) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(session, "editar_usuario");
            if (tienePermiso == 1) {
                usuario = usuarioDao.find(usuario.getId());
                sessionAdminUsuario.setUsuario(usuario);
                sessionAdminUsuario.setRolesUsuariosRemovidos(new ArrayList<RolUsuario>());
                sessionAdminUsuario.setUsuariosPermisoRemovidos(new ArrayList<UsuarioPermiso>());
                sessionAdminUsuario.setUsuariosCarrerasRemovidas(new ArrayList<UsuarioCarrera>());
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
            String mensaje = "Error al editar Usuario.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (session != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(session.toString(), mensaje, obj));
            }
        }
        return "";
    }

    public String abrirBuscarUsuarios(Usuario usuario) {
        try {
            renderedCrear(usuario);
            renderedEditar(usuario);
            buscar(usuario);
            return "pretty:usuarios";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String crear(Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(usuario, "crear_usuario");
            if (tienePermiso == 1) {
                sessionAdminUsuario.setUsuario(new Usuario());
                listarRoles(sessionAdminUsuario.getUsuario());
                listarPermisos(sessionAdminUsuario.getUsuario());
                listarCarreras(sessionAdminUsuario.getUsuario());
                sessionAdminUsuario.setRolesUsuariosRemovidos(new ArrayList<RolUsuario>());
                sessionAdminUsuario.setUsuariosCarrerasRemovidas(new ArrayList<UsuarioCarrera>());
                sessionAdminUsuario.setUsuariosPermisoRemovidos(new ArrayList<UsuarioPermiso>());
                return ("pretty:crearUsuario");
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al crear Usuario.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
            if (usuario != null) {
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
            }
        }
        return "";
    }

    public void buscar(Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(usuario, "buscar_usuario");
            if (tienePermiso == 1) {
                Usuario usuarioBuscar = new Usuario();
                sessionAdminUsuario.setUsuario(usuario);
                sessionAdminUsuario.setUsuarios(usuarioDao.buscarPorCriterio(usuarioBuscar));
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". "
                            + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }

        } catch (Exception e) {
            String mensaje = "Error al buscar Usuarios.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (usuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
            }
        }
    }

    public void guardarUsuariosCarreras() {
        List<UsuarioCarrera> usuarioCarreras = new ArrayList<>();
        for (Object o : sessionAdminUsuario.getCarrerasDualList().getTarget()) {
            int pos = o.toString().indexOf(":");
            Integer id = Integer.parseInt(o.toString().substring(0, pos));
            Carrera c = carreraFacadeLocal.find(id);
            UsuarioCarrera usuarioCarrera = new UsuarioCarrera();
            usuarioCarrera.setCarreraId(c.getId());
            usuarioCarrera.setUsuarioId(sessionAdminUsuario.getUsuario().getId());
            usuarioCarreras.add(usuarioCarrera);
        }
        for (UsuarioCarrera uc : usuarioCarreras) {
            if (contieneCarrera(usuarioCarreraDao.buscarPorUsuario(sessionAdminUsuario.getUsuario().getId()), uc) == false) {
                usuarioCarreraDao.create(uc);
            }
        }
    }

    public void guardarUsuariosPermiso() {
        List<UsuarioPermiso> usuarioPermisos = new ArrayList<>();
        for (Object permiso : sessionAdminUsuario.getPermisosDualList().getTarget()) {
            int pos = permiso.toString().indexOf(":");
            Long id = Long.parseLong(permiso.toString().substring(0, pos));
            Permiso p = permisoFacadeLocal.find(id);
            UsuarioPermiso usuarioPermiso = new UsuarioPermiso();
            usuarioPermiso.setPermisoId(p);
            usuarioPermiso.setUsuarioId(sessionAdminUsuario.getUsuario());
            usuarioPermisos.add(usuarioPermiso);
        }
        for (UsuarioPermiso usuarioPermiso : usuarioPermisos) {
            if (contienePermiso(usuarioPermisoFacadeLocal.buscarPorUsuario(sessionAdminUsuario.getUsuario().getId()), usuarioPermiso) == false) {
                usuarioPermisoFacadeLocal.create(usuarioPermiso);
            }
        }
    }

    public void guardarRolesUsuario() {
        List<RolUsuario> rolUsuarios = new ArrayList<>();
        for (Object r : sessionAdminUsuario.getRolesDualList().getTarget()) {
            int v = r.toString().indexOf(":");
            Long id = Long.parseLong(r.toString().substring(0, v));
            Rol rol = rolDao.find(id);
            RolUsuario rolUsuario = new RolUsuario();
            rolUsuario.setRolId(rol);
            rolUsuario.setUsuarioId(sessionAdminUsuario.getUsuario());
            rolUsuarios.add(rolUsuario);
        }
        for (RolUsuario rolUsuario : rolUsuarios) {
            if (contieneRol(rolUsuarioFacadeLocal.buscarPorUsuario(sessionAdminUsuario.getUsuario().getId()), rolUsuario) == false) {
                rolUsuarioFacadeLocal.create(rolUsuario);
                logDao.create(logDao.crearLog("RolUsuario", rolUsuario.getId() + "", "CREAR", "|Rol=" + rolUsuario.getRolId()
                        + "|Usuario=" + rolUsuario.getUsuarioId(), sessionAdminUsuario.getUsuario()));
            }
        }
    }

    public String guardar(Usuario sessionUsuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario, "editar_usuario");//editar Usuario
            if (tienePermiso == 1) {
                if (usuarioDao.unicoUsername(sessionAdminUsuario.getUsuario().getUsername()) == false || usuarioDao.find(sessionAdminUsuario.getUsuario().getId()).
                        equals(usuarioDao.buscarPorUsuario(sessionAdminUsuario.getUsuario().getUsername()))) {
                    sessionAdminUsuario.getUsuario().setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                    usuarioDao.edit(sessionAdminUsuario.getUsuario());
                    logDao.create(logDao.crearLog("Usuario", sessionAdminUsuario.getUsuario().getId() + "", "EDITAR", "|Username= "
                            + sessionAdminUsuario.getUsuario().getUsername() + "|EsActivo=" + sessionAdminUsuario.getUsuario().getEsActivo() + "|EsSuperuser= "
                            + sessionAdminUsuario.getUsuario().getEsSuperuser()
                            + "|Nombres= " + sessionAdminUsuario.getUsuario().getNombres() + "|Apellidos= " + sessionAdminUsuario.getUsuario().getApellidos()
                            + "|Email= "
                            + sessionAdminUsuario.getUsuario().getEmail(), sessionUsuario));
                    guardarRolesUsuario();
                    removerRolUsuarios();
                    guardarUsuariosPermiso();
                    removerUsuariosPermisos();
                    guardarUsuariosCarreras();
                    removerUsuariosCarreras();
                    buscar(sessionUsuario);
                    if (param.equalsIgnoreCase("guardar")) {
                        sessionAdminUsuario.setUsuario(new Usuario());
                        return ("pretty:usuarios");
                    } else {
                        if (param.equalsIgnoreCase("guardar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            sessionAdminUsuario.setUsuario(new Usuario());
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario") + " "
                            + bundle.getString("lbl.msm_existe"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". "
                            + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }

            }

        } catch (Exception e) {
            String mensaje = "Error al grabar Usuario...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.toString(), mensaje, obj));
            }
        }
        return "";
    }

    public String guardarConClave(Usuario sessionUsuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (sessionAdminUsuario.getUsuario().getPassword().equalsIgnoreCase(sessionAdminUsuario.getConfirmarClave())) {
                if (sessionAdminUsuario.getUsuario().getId() == null) {
                    int tienePermiso = usuarioDao.tienePermiso(sessionUsuario, "crear_usuario");
                    if (tienePermiso == 1) {
                        if (usuarioDao.unicoUsername(sessionAdminUsuario.getUsuario().getUsername()) == false) {
                            sessionAdminUsuario.getUsuario().setPassword(configuracionFacadeLocal.encriptaClave(sessionAdminUsuario.getUsuario().getPassword()));
                            sessionAdminUsuario.getUsuario().setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                            usuarioDao.create(sessionAdminUsuario.getUsuario());
                            logDao.create(logDao.crearLog("Usuario", sessionAdminUsuario.getUsuario().getId() + "", "CREAR", "|Username= "
                                    + sessionAdminUsuario.getUsuario().getUsername() + "|EsActivo=" + sessionAdminUsuario.getUsuario().getEsActivo()
                                    + "|EsSuperuser= " + sessionAdminUsuario.getUsuario().getEsSuperuser() + "|Nombres= " + sessionAdminUsuario.getUsuario().getNombres()
                                    + "|Apellidos= " + sessionAdminUsuario.getUsuario().getApellidos() + "|Email= " + sessionAdminUsuario.getUsuario().getEmail(), sessionUsuario));
                            guardarRolesUsuario();
                            removerRolUsuarios();
                            guardarUsuariosPermiso();
                            removerUsuariosPermisos();
                            guardarUsuariosCarreras();
                            removerUsuariosCarreras();
                            buscar(sessionUsuario);
                            if (param.equalsIgnoreCase("guardar")) {
                                sessionAdminUsuario.setUsuario(new Usuario());
                                return ("pretty:usuarios");
                            } else {
                                if (param.equalsIgnoreCase("guardar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                    sessionAdminUsuario.setUsuario(new Usuario());
                                }
                            }
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario") + " " + bundle.getString("lbl.msm_existe"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    } else {
                        if (tienePermiso == 2) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                } else {
                    int tienePermiso = usuarioDao.tienePermiso(sessionUsuario, "editar_usuario");
                    if (tienePermiso == 1) {
                        if (usuarioDao.unicoUsername(sessionAdminUsuario.getUsuario().getUsername()) == false || usuarioDao.find(sessionAdminUsuario.getUsuario().getId())
                                .equals(usuarioDao.buscarPorUsuario(sessionAdminUsuario.getUsuario().getUsername()))) {
                            sessionAdminUsuario.getUsuario().setPassword(configuracionFacadeLocal.encriptaClave(sessionAdminUsuario.getUsuario().getPassword()));
                            sessionAdminUsuario.getUsuario().setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                            usuarioDao.edit(sessionAdminUsuario.getUsuario());
                            logDao.create(logDao.crearLog("Usuario", sessionAdminUsuario.getUsuario().getId() + "", "EDITAR", "|Username= "
                                    + sessionAdminUsuario.getUsuario().getUsername() + "|EsActivo=" + sessionAdminUsuario.getUsuario().getEsActivo() + "|EsSuperuser= "
                                    + sessionAdminUsuario.getUsuario().getEsSuperuser() + "|Nombres= " + sessionAdminUsuario.getUsuario().getNombres() + "|Apellidos= "
                                    + sessionAdminUsuario.getUsuario().getApellidos() + "|Email= " + sessionAdminUsuario.getUsuario().getEmail(), sessionUsuario));
                            guardarRolesUsuario();
                            removerRolUsuarios();
                            buscar(sessionUsuario);
                            if (param.equalsIgnoreCase("guardar")) {
                                sessionAdminUsuario.setUsuario(new Usuario());
                                return ("pretty:usuarios");
                            } else {
                                if (param.equalsIgnoreCase("grabar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    if (param.equalsIgnoreCase("grabar-crear")) {
                                        sessionAdminUsuario.setUsuario(new Usuario());
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                        FacesContext.getCurrentInstance().addMessage(null, message);
                                    } else {
                                        if (param.equalsIgnoreCase("cambiar-clave-estudiante")) {
//                                            navegacion = "pretty:principalEstudiante";
                                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                            FacesContext.getCurrentInstance().addMessage(null, message);
                                        } else {
                                            if (param.equalsIgnoreCase("cambiar-clave-docente")) {
//                                                navegacion = "pretty:principalDocente";
                                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                                FacesContext.getCurrentInstance().addMessage(null, message);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario") + " " + bundle.getString("lbl.msm_existe"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    } else {
                        if (tienePermiso == 2) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_conciden_claves"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            String mensaje = "Error al grabar con clave Usuario.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.toString(), mensaje, obj));
            }
        }
        return "";
    }

    public void grabarUsuarioEstudiante(Estudiante estudiante) {
        try {
            Usuario usuario = null;
            Persona personaEstudiante = personaFacadeLocal.find(estudiante.getId());
            EstudianteUsuario estudianteUsuario1 = estudianteUsuarioFacadeLocal.buscarPorEstudiante(estudiante.getId());
            if (estudianteUsuario1 != null) {
                usuario = usuarioDao.find(estudianteUsuario1.getId());
            }
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setApellidos(personaEstudiante.getApellidos());
                usuario.setNombres(personaEstudiante.getNombres());
                usuario.setEmail(personaEstudiante.getEmail());
                usuario.setEsSuperuser(false);
                usuario.setEsActivo(true);
                usuario.setPassword(configuracionFacadeLocal.encriptaClave(personaEstudiante.getNumeroIdentificacion()));
                usuario.setUsername(personaEstudiante.getNumeroIdentificacion());
                if (usuarioDao.unicoUsername(usuario.getUsername()) == false) {
                    usuarioDao.create(usuario);
                    EstudianteUsuario estudianteUsuario = new EstudianteUsuario();
                    estudianteUsuario.setEstudianteId(estudiante.getId());
                    estudianteUsuario.setId(usuario.getId());
                    estudianteUsuario.setId(usuario.getId());
                    estudianteUsuarioFacadeLocal.create(estudianteUsuario);
                    Rol rol = rolDao.find((long) 2);
                    if (rol != null) {
                        RolUsuario rolUsuario = new RolUsuario();
                        rolUsuario.setRolId(rol);
                        rolUsuario.setUsuarioId(usuario);
                        rolUsuarioFacadeLocal.create(rolUsuario);
                    }
                }
            } else {
                if (usuarioDao.unicoUsername(usuario.getUsername()) == false) {
                    usuario.setPassword(configuracionFacadeLocal.encriptaClave(personaEstudiante.getNumeroIdentificacion()));
                    usuarioDao.edit(usuario);
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean contieneCarrera(List<UsuarioCarrera> usuarioCarreras, UsuarioCarrera usuarioCarrera) {
        boolean var = false;
        if (usuarioCarreras != null) {
            for (UsuarioCarrera uc : usuarioCarreras) {
                Carrera carrera = carreraFacadeLocal.find(uc.getCarreraId());
                Carrera carreraBuscar = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
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
            Carrera carrera = carreraFacadeLocal.find(uc.getCarreraId());
            Carrera carreraBuscar = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
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
            if (sessionAdminUsuario.getUsuario() != null) {
                if (sessionAdminUsuario.getUsuario().getId() != null) {
                    for (UsuarioCarrera uc : usuarioCarreraDao.buscarPorUsuario(sessionAdminUsuario.getUsuario().getId())) {
                        Carrera carrera = carreraFacadeLocal.find(uc.getCarreraId());
                        usuarioCarreras.add(carrera);
                    }
                }
            }
            for (Carrera c : carreraFacadeLocal.findAll()) {
                if (!usuarioCarreras.contains(c)) {
                    carreras.add(c);
                }
            }
            sessionAdminUsuario.setCarrerasDualList(new DualListModel<>(carreras, usuarioCarreras));
            sessionAdminUsuario.setNumeroCarrerasDisponibles(sessionAdminUsuario.getCarrerasDualList().getSource().size());
            sessionAdminUsuario.setNumeroCarrerasSeleccionadas(sessionAdminUsuario.getCarrerasDualList().getTarget().size());
        } catch (Exception e) {
            String mensaje = "Error al buscar Usuarios Carreras.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.toString(), mensaje, obj));
            }
        }
    }

    public void listarPermisos(Usuario sessionUsuario) {
        List<Permiso> usuariosPermisos = new ArrayList<>();
        List<Permiso> permisos = new ArrayList<>();
        try {
            if (sessionAdminUsuario.getUsuario() != null) {
                if (sessionAdminUsuario.getUsuario().getId() != null) {
                    for (UsuarioPermiso usuarioPermiso : sessionAdminUsuario.getUsuario().getUsuarioPermisoList()) {
                        usuariosPermisos.add(usuarioPermiso.getPermisoId());
                    }
                }
            }
            for (Permiso permiso : permisoFacadeLocal.findAll()) {
                if (!usuariosPermisos.contains(permiso)) {
                    permisos.add(permiso);
                }
            }
            sessionAdminUsuario.setPermisosDualList(new DualListModel<>(permisos, usuariosPermisos));
            sessionAdminUsuario.setNumeroPermisosDisponibles(sessionAdminUsuario.getPermisosDualList().getSource().size());
            sessionAdminUsuario.setNumeroPermisosSeleccionados(sessionAdminUsuario.getPermisosDualList().getTarget().size());
        } catch (Exception e) {
            String mensaje = "Error al buscar Usuarios Permiso...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.toString(), mensaje, obj));
            }
        }

    }

    public void removerUsuariosCarreras() {
        if (sessionAdminUsuario.getUsuario() != null) {
            if (sessionAdminUsuario.getUsuario().getId() != null) {
                for (UsuarioCarrera usuarioCarrera : sessionAdminUsuario.getUsuariosCarrerasRemovidas()) {
                    Long id = devuelveCarreraEliminar(usuarioCarreraDao.buscarPorUsuario(sessionAdminUsuario.getUsuario().getId()), usuarioCarrera);
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
        if (sessionAdminUsuario.getUsuario() == null) {
            return;
        }
        if (sessionAdminUsuario.getUsuario().getId() != null) {
            for (UsuarioPermiso usuarioPermiso : sessionAdminUsuario.getUsuariosPermisoRemovidos()) {
                Long id = devuelvePermisoEliminar(usuarioPermisoFacadeLocal.buscarPorUsuario(sessionAdminUsuario.getUsuario().getId()), usuarioPermiso);
                UsuarioPermiso up = new UsuarioPermiso();
                up = usuarioPermisoFacadeLocal.find(id);
                if (up != null) {
                    usuarioPermisoFacadeLocal.remove(up);
                }
            }
        }
    }

    public void removerRolUsuarios() {
        if (sessionAdminUsuario.getUsuario().getId() != null) {
            for (RolUsuario rolUsuario : sessionAdminUsuario.getRolesUsuariosRemovidos()) {
                Long id = devuelveRolEliminar(rolUsuarioFacadeLocal.buscarPorUsuario(sessionAdminUsuario.getUsuario().getId()), rolUsuario);
                RolUsuario r = new RolUsuario();
                r = rolUsuarioFacadeLocal.find(id);
                if (r != null) {
                    rolUsuarioFacadeLocal.remove(r);
                }
            }
        }
    }

    public void transferUsuariosPermisos(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                Permiso p = permisoFacadeLocal.find(id);
                UsuarioPermiso up = new UsuarioPermiso();
                up.setPermisoId(p);
                if (event.isRemove()) {
                    sessionAdminUsuario.getUsuariosPermisoRemovidos().add(up);
                } else {
                    if (contienePermiso(sessionAdminUsuario.getUsuariosPermisoRemovidos(), up)) {
                        sessionAdminUsuario.getUsuariosPermisoRemovidos().remove(up);
                    }
                }
                sessionAdminUsuario.setNumeroPermisosDisponibles(sessionAdminUsuario.getPermisosDualList().getSource().size());
                sessionAdminUsuario.setNumeroPermisosSeleccionados(sessionAdminUsuario.getPermisosDualList().getTarget().size());
            }

        } catch (Exception e) {
            String mensaje = "Error en Transefer Rol Usuarios...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void transferUsuariosCarreras(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Integer id = Integer.parseInt(item.toString().substring(0, v));
                Carrera c = carreraFacadeLocal.find(id);
                UsuarioCarrera uc = new UsuarioCarrera();
                uc.setCarreraId(c.getId());
                if (event.isRemove()) {
                    sessionAdminUsuario.getUsuariosCarrerasRemovidas().add(uc);
                } else {
                    if (contieneCarrera(sessionAdminUsuario.getUsuariosCarrerasRemovidas(), uc)) {
                        sessionAdminUsuario.getUsuariosCarrerasRemovidas().remove(uc);
                    }
                }
                sessionAdminUsuario.setNumeroCarrerasDisponibles(sessionAdminUsuario.getCarrerasDualList().getSource().size());
                sessionAdminUsuario.setNumeroCarrerasSeleccionadas(sessionAdminUsuario.getCarrerasDualList().getTarget().size());
            }

        } catch (Exception e) {
            String mensaje = "Error en Transefer Rol Usuarios...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
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
                    sessionAdminUsuario.getRolesUsuariosRemovidos().add(rolUsuario);
                } else {
                    if (contieneRol(sessionAdminUsuario.getRolesUsuariosRemovidos(), rolUsuario)) {
                        sessionAdminUsuario.getRolesUsuariosRemovidos().remove(rolUsuario);
                    }
                }
            }

        } catch (Exception e) {
            String mensaje = "Error en Transefer Rol Usuarios.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
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
            sessionAdminUsuario.setRolesDualList(new DualListModel<>(roles, rolesUsuarios));
        } catch (Exception e) {
            String mensaje = "Error al buscar Roles Usuarios.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RENDERED">

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioDao.tienePermiso(usuario, "crear_usuario");
        if (tienePermiso == 1) {
            sessionAdminUsuario.setRenderedCrear(true);
        } else {
            sessionAdminUsuario.setRenderedCrear(false);
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioDao.tienePermiso(usuario, "editar_usuario");
        if (tienePermiso == 1) {
            sessionAdminUsuario.setRenderedEditar(true);
            sessionAdminUsuario.setRenderedNoEditar(false);
        } else {
            sessionAdminUsuario.setRenderedEditar(false);
            sessionAdminUsuario.setRenderedNoEditar(true);
        }
    }

    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.ConfiguracionFacadeLocal;
import com.jlmallas.comun.service.PersonaFacadeLocal;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.comun.controlador.AdministrarCatalogoDocumentoExpediente;
import edu.unl.sigett.comun.controlador.AdministrarCatalogoProyectos;
import edu.unl.sigett.academico.controlador.AdministrarDocentes;
import edu.unl.sigett.comun.controlador.AdministrarEstadoProyecto;
import edu.unl.sigett.academico.controlador.AdministrarEstudiantes;
import edu.unl.sigett.comun.controlador.AdministrarLineaInvestigacion;
import edu.unl.sigett.comun.controlador.AdministrarLineasInvestigacionCarrera;
import edu.unl.sigett.comun.controlador.AdministrarTipoProyectos;
import edu.unl.sigett.seguimiento.controlador.AdministrarEstadosActividades;
import edu.unl.sigett.seguridad.managed.session.AdminUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.DocenteUsuario;
import edu.jlmallas.academico.entity.Estudiante;
import edu.unl.sigett.entity.EstudianteUsuario;
import com.jlmallas.soporte.entity.Objeto;
import com.jlmallas.seguridad.entity.Permiso;
import com.jlmallas.seguridad.entity.Rol;
import com.jlmallas.seguridad.entity.RolUsuario;
import com.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
import com.jlmallas.seguridad.entity.UsuarioPermiso;
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
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.session.DocenteUsuarioFacadeLocal;
import edu.unl.sigett.session.EstudianteUsuarioFacadeLocal;
import com.jlmallas.soporte.session.ExcepcionFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import com.jlmallas.seguridad.session.PermisoFacadeLocal;
import com.jlmallas.soporte.session.ProyectoSoftwareFacadeLocal;
import com.jlmallas.seguridad.session.RolFacadeLocal;
import com.jlmallas.seguridad.session.RolUsuarioFacadeLocal;
import edu.unl.sigett.session.UsuarioCarreraFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioPermisoFacadeLocal;
import edu.unl.sigett.academico.managed.session.SessionDocente;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarUsuario",
            pattern = "/editarUsuario/#{adminUsuario.usuario.id}",
            viewId = "/faces/pages/seguridad/editarUsuario.xhtml"
    ),
    @URLMapping(
            id = "crearUsuario",
            pattern = "/crearUsuario/",
            viewId = "/faces/pages/seguridad/crearUsuario.xhtml"
    ),
    @URLMapping(
            id = "usuarios",
            pattern = "/usuarios/",
            viewId = "/faces/pages/seguridad/buscarUsuarios.xhtml"
    ),
    @URLMapping(
            id = "cambiarClave",
            pattern = "/cambiarClave/#{adminUsuario.usuario.id}",
            viewId = "/faces/cambiarClave.xhtml"
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
    private AdminUsuario adminUsuario;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private AdministrarLineaInvestigacion administrarLineaInvestigacion;
    @Inject
    private AdministrarLineasInvestigacionCarrera administrarLineasInvestigacionCarrera;
    @Inject
    private AdministrarEstudiantes administrarEstudiantes;
    @Inject
    private AdministrarUsuarioCarrera administrarUsuarioCarrera;
    @Inject
    private AdministrarCatalogoDocumentoExpediente requisitos;
    @Inject
    private AdministrarTipoProyectos administrarTipoProyectos;
    @Inject
    private AdministrarEstadoProyecto administrarEstadoProyecto;
    @Inject
    private AdministrarEstadosActividades administrarEstadosActividades;
    @Inject
    private AdministrarCatalogoProyectos administrarCatalogoProyectos;
    @Inject
    private SessionDocente sessionDocente;
    private DualListModel<Rol> rolesDualList;
    private DualListModel<Permiso> permisosDualList;
    private DualListModel<Carrera> carrerasDualList;
    private List<RolUsuario> rolesUsuariosRemovidos;
    private List<UsuarioCarrera> usuariosCarrerasRemovidas;
    private List<UsuarioPermiso> usuariosPermisoRemovidos;
    private List<Usuario> usuarios;
    
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private UsuarioCarreraFacadeLocal usuarioCarreraFacadeLocal;
    @EJB
    private RolFacadeLocal rolFacadeLocal;
    @EJB
    private RolUsuarioFacadeLocal rolUsuarioFacadeLocal;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    @EJB
    private ExcepcionFacadeLocal excepcionFacadeLocal;
    @EJB
    private ObjetoFacadeLocal objetoFacadeLocal;
    @EJB
    private ProyectoSoftwareFacadeLocal proyectoSoftwareFacadeLocal;
    @EJB
    private PermisoFacadeLocal permisoFacadeLocal;
    @EJB
    private UsuarioPermisoFacadeLocal usuarioPermisoFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private EstudianteUsuarioFacadeLocal estudianteUsuarioFacadeLocal;
    @EJB
    private DocenteUsuarioFacadeLocal docenteUsuarioFacadeLocal;
    @EJB
    private PersonaFacadeLocal personaFacadeLocal;
    @EJB
    private ConfiguracionFacadeLocal configuracionFacadeLocal;
    
    private int numeroCarrerasDisponibles = 0;
    private int numeroCarrerasSeleccionadas = 0;
    private int numeroPermisosDisponibles = 0;
    private int numeroPermisosSeleccionados = 0;
    private String confirmarClaveEstudiante;
    private String criterio;
    private String confirmaClave;
    
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedSincronizar;
    
    public AdministrarUsuarios() {
        confirmaClave = "";
        rolesDualList = new DualListModel<>();
        permisosDualList = new DualListModel<>();
        carrerasDualList = new DualListModel<>();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String editar(Usuario usuario, Usuario session) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(session, "editar_usuario");
            if (tienePermiso == 1) {
                adminUsuario.setUsuario(usuario);
                rolesUsuariosRemovidos = new ArrayList<>();
                usuariosPermisoRemovidos = new ArrayList<>();
                usuariosCarrerasRemovidas = new ArrayList<>();
                listarRoles(usuario);
                listarPermisos(usuario);
                listarCarreras(usuario);
                navegacion = "pretty:editarUsuario";
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
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        
        return navegacion;
    }
    
    public String abrirBuscarUsuarios(Usuario usuario) {
        String navegacion = "";
        try {
            renderedCrear(usuario);
            renderedEditar(usuario);
            navegacion = "pretty:usuarios";
        } catch (Exception e) {
        }
        return navegacion;
    }
    
    public void actualizaRendered(Usuario usuario) {
        if (usuario.getId() != null) {
            administrarCatalogoProyectos.renderedBuscar(usuario);
            renderedBuscarDocente(usuario);
            requisitos.renderedBuscar(usuario);
            administrarEstadoProyecto.renderedBuscar(usuario);
            administrarEstadosActividades.renderedBuscar(usuario);
            administrarEstudiantes.renderedBuscar(usuario);
            administrarLineaInvestigacion.renderedBuscar(usuario);
            administrarLineasInvestigacionCarrera.renderedBuscar(usuario);
            administrarTipoProyectos.renderedBuscar(usuario);
            administrarUsuarioCarrera.renderedBuscar(usuario);
        }
    }
    
    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_docente");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }
    
    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_usuario");
            if (tienePermiso == 1) {
                adminUsuario.setUsuario(new Usuario());
                listarRoles(adminUsuario.getUsuario());
                listarPermisos(adminUsuario.getUsuario());
                listarCarreras(adminUsuario.getUsuario());
                rolesUsuariosRemovidos = new ArrayList<>();
                usuariosPermisoRemovidos = new ArrayList<>();
                usuariosCarrerasRemovidas = new ArrayList<>();
                navegacion = "pretty:crearUsuario";
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
            if (sessionUsuario.getUsuario() != null) {
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return navegacion;
    }
    
    public void buscar(String criterio, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_usuario");
            if (tienePermiso == 1) {
                usuarios = usuarioFacadeLocal.buscarPorCriterios(criterio);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
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
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
    }
    
    public void guardarUsuariosCarreras(Usuario usuario) {
        List<UsuarioCarrera> usuarioCarreras = new ArrayList<>();
        for (Object o : carrerasDualList.getTarget()) {
            int pos = o.toString().indexOf(":");
            Integer id = Integer.parseInt(o.toString().substring(0, pos));
            Carrera c = carreraFacadeLocal.find(id);
            UsuarioCarrera usuarioCarrera = new UsuarioCarrera();
            usuarioCarrera.setCarreraId(c.getId());
            usuarioCarrera.setUsuarioId(usuario.getId());
            usuarioCarreras.add(usuarioCarrera);
        }
        for (UsuarioCarrera uc : usuarioCarreras) {
            if (contieneCarrera(usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId()), uc) == false) {
                usuarioCarreraFacadeLocal.create(uc);
                logFacadeLocal.create(logFacadeLocal.crearLog("UsuarioCarrera", uc.getId() + "", "CREAR", "|Usuario= " + usuario + "|Carrera= " + uc.getCarreraId(), sessionUsuario.getUsuario()));
            }
        }
    }
    
    public void guardarUsuariosPermiso(Usuario usuario) {
        List<UsuarioPermiso> usuarioPermisos = new ArrayList<>();
        for (Object permiso : permisosDualList.getTarget()) {
            int pos = permiso.toString().indexOf(":");
            Long id = Long.parseLong(permiso.toString().substring(0, pos));
            Permiso p = permisoFacadeLocal.find(id);
            UsuarioPermiso usuarioPermiso = new UsuarioPermiso();
            usuarioPermiso.setPermisoId(p);
            usuarioPermiso.setUsuarioId(usuario);
            usuarioPermisos.add(usuarioPermiso);
            
        }
        for (UsuarioPermiso usuarioPermiso : usuarioPermisos) {
            if (contienePermiso(usuarioPermisoFacadeLocal.buscarPorUsuario(usuario.getId()), usuarioPermiso) == false) {
                usuarioPermisoFacadeLocal.create(usuarioPermiso);
                logFacadeLocal.create(logFacadeLocal.crearLog("UsuarioPermiso", usuarioPermiso.getId() + "", "CREAR", "|Usuario= " + usuario + "|Permiso= " + usuarioPermiso.getPermisoId(), sessionUsuario.getUsuario()));
            }
            
        }
    }
    
    public void guardarRolesUsuario(Usuario usuario) {
        List<RolUsuario> rolUsuarios = new ArrayList<>();
        for (Object r : rolesDualList.getTarget()) {
            int v = r.toString().indexOf(":");
            Long id = Long.parseLong(r.toString().substring(0, v));
            Rol rol = rolFacadeLocal.find(id);
            RolUsuario rolUsuario = new RolUsuario();
            rolUsuario.setRolId(rol);
            rolUsuario.setUsuarioId(usuario);
            rolUsuarios.add(rolUsuario);
        }
        for (RolUsuario rolUsuario : rolUsuarios) {
            if (contieneRol(rolUsuarioFacadeLocal.buscarPorUsuario(usuario.getId()), rolUsuario) == false) {
                rolUsuarioFacadeLocal.create(rolUsuario);
                logFacadeLocal.create(logFacadeLocal.crearLog("RolUsuario", rolUsuario.getId() + "", "CREAR", "|Rol=" + rolUsuario.getRolId() + "|Usuario=" + rolUsuario.getUsuarioId(), sessionUsuario.getUsuario()));
            }
        }
    }
    
    public String guardar(Usuario usuario, Usuario session) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(session, "editar_usuario");//editar Usuario
            if (tienePermiso == 1) {
                if (usuarioFacadeLocal.unicoUsername(usuario.getUsername()) == false || usuarioFacadeLocal.find(usuario.getId()).equals(usuarioFacadeLocal.buscarPorUsuario(usuario.getUsername()))) {
                    usuario.setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                    usuarioFacadeLocal.edit(usuario);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Usuario", usuario.getId() + "", "EDITAR", "|Username= " + usuario.getUsername() + "|EsActivo=" + usuario.getEsActivo() + "|EsSuperuser= " + usuario.getEsSuperuser() + "|Nombres= " + usuario.getNombres() + "|Apellidos= " + usuario.getApellidos() + "|Email= " + usuario.getEmail(), session));
                    guardarRolesUsuario(usuario);
                    removerRolUsuarios(usuario);
                    guardarUsuariosPermiso(usuario);
                    removerUsuariosPermisos(usuario);
                    guardarUsuariosCarreras(usuario);
                    removerUsuariosCarreras(usuario);
                    buscar("", usuario);
                    if (param.equalsIgnoreCase("guardar")) {
                        navegacion = "pretty:usuarios";
                        adminUsuario.setUsuario(new Usuario());
                    } else {
                        if (param.equalsIgnoreCase("guardar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            adminUsuario.setUsuario(new Usuario());
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario") + " " + bundle.getString("lbl.msm_existe"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                if (tienePermiso == 2) {
                    navegacion = "";
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
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
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return navegacion;
    }
    
    public String guardarConClave(Usuario usuario, Usuario sessionUsr, String confirmaClave) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (usuario.getPassword().equalsIgnoreCase(confirmaClave)) {
                if (usuario.getId() == null) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsr, "crear_usuario");
                    if (tienePermiso == 1) {
                        if (usuarioFacadeLocal.unicoUsername(usuario.getUsername()) == false) {
                            usuario.setPassword(configuracionFacadeLocal.encriptaClave(usuario.getPassword()));
                            usuario.setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                            usuarioFacadeLocal.create(usuario);
                            logFacadeLocal.create(logFacadeLocal.crearLog("Usuario", usuario.getId() + "", "CREAR", "|Username= " + usuario.getUsername() + "|EsActivo=" + usuario.getEsActivo() + "|EsSuperuser= " + usuario.getEsSuperuser() + "|Nombres= " + usuario.getNombres() + "|Apellidos= " + usuario.getApellidos() + "|Email= " + usuario.getEmail(), sessionUsr));
                            guardarRolesUsuario(usuario);
                            removerRolUsuarios(usuario);
                            guardarUsuariosPermiso(usuario);
                            removerUsuariosPermisos(usuario);
                            guardarUsuariosCarreras(usuario);
                            removerUsuariosCarreras(usuario);
                            buscar("", sessionUsr);
                            if (param.equalsIgnoreCase("guardar")) {
                                navegacion = "pretty:usuarios";
                                adminUsuario.setUsuario(new Usuario());
                            } else {
                                if (param.equalsIgnoreCase("guardar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                    adminUsuario.setUsuario(new Usuario());
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
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsr, "editar_usuario");
                    if (tienePermiso == 1) {
                        if (usuarioFacadeLocal.unicoUsername(usuario.getUsername()) == false || usuarioFacadeLocal.find(usuario.getId()).equals(usuarioFacadeLocal.buscarPorUsuario(usuario.getUsername()))) {
                            usuario.setPassword(configuracionFacadeLocal.encriptaClave(usuario.getPassword()));
                            usuario.setUsuarioPermisoList(new ArrayList<UsuarioPermiso>());
                            usuarioFacadeLocal.edit(usuario);
                            logFacadeLocal.create(logFacadeLocal.crearLog("Usuario", usuario.getId() + "", "EDITAR", "|Username= " + usuario.getUsername() + "|EsActivo=" + usuario.getEsActivo() + "|EsSuperuser= " + usuario.getEsSuperuser() + "|Nombres= " + usuario.getNombres() + "|Apellidos= " + usuario.getApellidos() + "|Email= " + usuario.getEmail(), sessionUsr));
                            guardarRolesUsuario(usuario);
                            removerRolUsuarios(usuario);
                            buscar("", sessionUsr);
                            if (param.equalsIgnoreCase("guardar")) {
                                navegacion = "pretty:usuarios";
                                adminUsuario.setUsuario(new Usuario());
                            } else {
                                if (param.equalsIgnoreCase("grabar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    if (param.equalsIgnoreCase("grabar-crear")) {
                                        adminUsuario.setUsuario(new Usuario());
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                        FacesContext.getCurrentInstance().addMessage(null, message);
                                    } else {
                                        if (param.equalsIgnoreCase("cambiar-clave-estudiante")) {
                                            navegacion = "pretty:principalEstudiante";
                                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                            FacesContext.getCurrentInstance().addMessage(null, message);
                                        } else {
                                            if (param.equalsIgnoreCase("cambiar-clave-docente")) {
                                                navegacion = "pretty:principalDocente";
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
            if (sessionUsr != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsr.toString(), mensaje, obj));
            }
        }
        return navegacion;
    }
    
    public void grabarUsuarioEstudiante(Estudiante estudiante) {
        try {
            Usuario usuario = null;
            Persona personaEstudiante = personaFacadeLocal.find(estudiante.getId());
            EstudianteUsuario estudianteUsuario1 = estudianteUsuarioFacadeLocal.buscarPorEstudiante(estudiante.getId());
            if (estudianteUsuario1 != null) {
                usuario = usuarioFacadeLocal.find(estudianteUsuario1.getId());
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
                if (usuarioFacadeLocal.unicoUsername(usuario.getUsername()) == false) {
                    usuarioFacadeLocal.create(usuario);
                    EstudianteUsuario estudianteUsuario = new EstudianteUsuario();
                    estudianteUsuario.setEstudianteId(estudiante.getId());
                    estudianteUsuario.setId(usuario.getId());
                    estudianteUsuario.setId(usuario.getId());
                    estudianteUsuarioFacadeLocal.create(estudianteUsuario);
                    Rol rol = rolFacadeLocal.find((long) 2);
                    if (rol != null) {
                        RolUsuario rolUsuario = new RolUsuario();
                        rolUsuario.setRolId(rol);
                        rolUsuario.setUsuarioId(usuario);
                        rolUsuarioFacadeLocal.create(rolUsuario);
                    }
                }
            } else {
                if (usuarioFacadeLocal.unicoUsername(usuario.getUsername()) == false) {
                    usuario.setPassword(configuracionFacadeLocal.encriptaClave(personaEstudiante.getNumeroIdentificacion()));
                    usuarioFacadeLocal.edit(usuario);
                }
            }
        } catch (Exception e) {
        }
    }
    
    public void grabarUsuarioDocente(Docente docente) {
        try {
            Usuario usuario = null;
            DocenteUsuario du = docenteUsuarioFacadeLocal.buscarPorDocente(docente.getId());
            Persona personaDocente = personaFacadeLocal.find(docente.getId());
            if (du != null) {
                usuario = usuarioFacadeLocal.find(du.getId());
            }
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setApellidos(personaDocente.getApellidos());
                usuario.setNombres(personaDocente.getNombres());
                usuario.setEmail(personaDocente.getEmail());
                usuario.setEsSuperuser(false);
                usuario.setEsActivo(true);
                usuario.setPassword(configuracionFacadeLocal.encriptaClave(personaDocente.getNumeroIdentificacion()));
                usuario.setUsername(personaDocente.getNumeroIdentificacion());
                if (usuarioFacadeLocal.unicoUsername(usuario.getUsername()) == false) {
                    usuarioFacadeLocal.create(usuario);
                    DocenteUsuario docenteUsuario = new DocenteUsuario();
                    docenteUsuario.setDocenteId(docente.getId());
                    docenteUsuario.setId(usuario.getId());
                    docenteUsuario.setId(usuario.getId());
                    docenteUsuarioFacadeLocal.create(docenteUsuario);
                    Rol rol = rolFacadeLocal.find((long) 1);
                    if (rol != null) {
                        RolUsuario rolUsuario = new RolUsuario();
                        rolUsuario.setRolId(rol);
                        rolUsuario.setUsuarioId(usuario);
                        rolUsuarioFacadeLocal.create(rolUsuario);
                    }
                }
            } else {
                usuario.setPassword(configuracionFacadeLocal.encriptaClave(personaDocente.getNumeroIdentificacion()));
                usuarioFacadeLocal.edit(usuario);
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
    
    public void renderedBuscarDocente(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            sessionDocente.setRenderedBuscar(true);
        } else {
            sessionDocente.setRenderedBuscar(false);
        }
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
    
    public void listarCarreras(Usuario usuario) {
        List<Carrera> usuarioCarreras = new ArrayList<>();
        List<Carrera> carreras = new ArrayList<>();
        try {
            if (usuario != null) {
                if (usuario.getId() != null) {
                    for (UsuarioCarrera uc : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
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
            carrerasDualList = new DualListModel<>(carreras, usuarioCarreras);
            numeroCarrerasDisponibles = carrerasDualList.getSource().size();
            numeroCarrerasSeleccionadas = carrerasDualList.getTarget().size();
        } catch (Exception e) {
            String mensaje = "Error al buscar Usuarios Carreras.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
    }
    
    public void listarPermisos(Usuario usuario) {
        List<Permiso> usuariosPermisos = new ArrayList<>();
        List<Permiso> permisos = new ArrayList<>();
        try {
            if (usuario != null) {
                if (usuario.getId() != null) {
                    for (UsuarioPermiso usuarioPermiso : usuario.getUsuarioPermisoList()) {
                        usuariosPermisos.add(usuarioPermiso.getPermisoId());
                    }
                }
            }
            for (Permiso permiso : permisoFacadeLocal.findAll()) {
                if (!usuariosPermisos.contains(permiso)) {
                    permisos.add(permiso);
                }
            }
            permisosDualList = new DualListModel<>(permisos, usuariosPermisos);
            numeroPermisosDisponibles = permisosDualList.getSource().size();
            numeroPermisosSeleccionados = permisosDualList.getTarget().size();
        } catch (Exception e) {
            String mensaje = "Error al buscar Usuarios Permiso...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        
    }
    
    public void removerUsuariosCarreras(Usuario usuario) {
        if (usuario != null) {
            if (usuario.getId() != null) {
                for (UsuarioCarrera usuarioCarrera : usuariosCarrerasRemovidas) {
                    Long id = devuelveCarreraEliminar(usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId()), usuarioCarrera);
                    UsuarioCarrera uc = new UsuarioCarrera();
                    uc = usuarioCarreraFacadeLocal.find(id);
                    if (uc != null) {
                        logFacadeLocal.create(logFacadeLocal.crearLog("UsuarioCarrera", uc.getId() + "", "ELIMINAR", "ELIMINAR: |Usuario= " + usuario + "|Carrera= " + uc.getCarreraId(), sessionUsuario.getUsuario()));
                        usuarioCarreraFacadeLocal.remove(uc);
                    }
                }
            }
        }
    }
    
    public void removerUsuariosPermisos(Usuario usuario) {
        if (usuario != null) {
            if (usuario.getId() != null) {
                for (UsuarioPermiso usuarioPermiso : usuariosPermisoRemovidos) {
                    Long id = devuelvePermisoEliminar(usuarioPermisoFacadeLocal.buscarPorUsuario(usuario.getId()), usuarioPermiso);
                    UsuarioPermiso up = new UsuarioPermiso();
                    up = usuarioPermisoFacadeLocal.find(id);
                    if (up != null) {
                        logFacadeLocal.create(logFacadeLocal.crearLog("UsuarioPermiso", up.getId() + "", "ELIMINAR", "ELIMINAR: |Pemiso= " + up.getPermisoId() + "|Usuario= " + usuario, sessionUsuario.getUsuario()));
                        usuarioPermisoFacadeLocal.remove(up);
                    }
                }
            }
        }
    }
    
    public String abrirCambiarClaveEstudiante() {
        String navegacion = "";
        try {
            rolesUsuariosRemovidos = new ArrayList<>();
            usuariosPermisoRemovidos = new ArrayList<>();
            usuariosCarrerasRemovidas = new ArrayList<>();
            navegacion = "pretty:cambiarClaveEstudiante";
        } catch (Exception e) {
        }
        return navegacion;
    }
    
    public String abrirCambiarClaveDocente() {
        String navegacion = "";
        try {
            rolesUsuariosRemovidos = new ArrayList<>();
            usuariosPermisoRemovidos = new ArrayList<>();
            usuariosCarrerasRemovidas = new ArrayList<>();
            navegacion = "pretty:cambiarClaveDocente";
        } catch (Exception e) {
        }
        return navegacion;
    }
    
    public void removerRolUsuarios(Usuario usuario) {
        if (usuario.getId() != null) {
            for (RolUsuario rolUsuario : rolesUsuariosRemovidos) {
                Long id = devuelveRolEliminar(rolUsuarioFacadeLocal.buscarPorUsuario(usuario.getId()), rolUsuario);
                RolUsuario r = new RolUsuario();
                r = rolUsuarioFacadeLocal.find(id);
                if (r != null) {
                    logFacadeLocal.create(logFacadeLocal.crearLog("RolUsuario", r.getId() + "", "ELIMINAR", "|Rol= " + rolUsuario.getRolId() + "|Usuario= " + rolUsuario.getUsuarioId(), sessionUsuario.getUsuario()));
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
                    usuariosPermisoRemovidos.add(up);
                } else {
                    if (contienePermiso(usuariosPermisoRemovidos, up)) {
                        usuariosPermisoRemovidos.remove(up);
                    }
                }
                numeroPermisosDisponibles = permisosDualList.getSource().size();
                numeroPermisosSeleccionados = permisosDualList.getTarget().size();
            }
            
        } catch (Exception e) {
            String mensaje = "Error en Transefer Rol Usuarios...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
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
                    usuariosCarrerasRemovidas.add(uc);
                } else {
                    if (contieneCarrera(usuariosCarrerasRemovidas, uc)) {
                        usuariosCarrerasRemovidas.remove(uc);
                    }
                }
                numeroCarrerasDisponibles = carrerasDualList.getSource().size();
                numeroCarrerasSeleccionadas = carrerasDualList.getTarget().size();
            }
            
        } catch (Exception e) {
            String mensaje = "Error en Transefer Rol Usuarios...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        
    }
    
    public void transferRolUsuarios(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                Rol rol = rolFacadeLocal.find(id);
                RolUsuario rolUsuario = new RolUsuario();
                rolUsuario.setRolId(rol);
                if (event.isRemove()) {
                    rolesUsuariosRemovidos.add(rolUsuario);
                } else {
                    if (contieneRol(rolesUsuariosRemovidos, rolUsuario)) {
                        rolesUsuariosRemovidos.remove(rolUsuario);
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
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
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
            for (Rol r : rolFacadeLocal.findAll()) {
                if (!rolesUsuarios.contains(r)) {
                    roles.add(r);
                }
            }
            roles = rolFacadeLocal.findAll();
            rolesDualList = new DualListModel<>(roles, rolesUsuarios);
        } catch (Exception e) {
            String mensaje = "Error al buscar Roles Usuarios.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Usuario");
                if (obj == null) {
                    obj = new Objeto(null, "Usuario", "Usuario");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RENDERED">

    public void renderedCrear(Usuario usu) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_usuario");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }
    
    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_usuario");
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
    
    public boolean isRenderedSincronizar() {
        return renderedSincronizar;
    }
    
    public void setRenderedSincronizar(boolean renderedSincronizar) {
        this.renderedSincronizar = renderedSincronizar;
    }
    
    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }
    
    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }
    
    public AdminUsuario getAdminUsuario() {
        return adminUsuario;
    }
    
    public void setAdminUsuario(AdminUsuario adminUsuario) {
        this.adminUsuario = adminUsuario;
    }
    
    public String getCriterio() {
        return criterio;
    }
    
    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }
    
    public String getConfirmaClave() {
        return confirmaClave;
    }
    
    public void setConfirmaClave(String confirmaClave) {
        this.confirmaClave = confirmaClave;
    }
    
    public DualListModel<Rol> getRolesDualList() {
        return rolesDualList;
    }
    
    public void setRolesDualList(DualListModel<Rol> rolesDualList) {
        this.rolesDualList = rolesDualList;
    }
    
    public List<RolUsuario> getRolesUsuariosRemovidos() {
        return rolesUsuariosRemovidos;
    }
    
    public void setRolesUsuariosRemovidos(List<RolUsuario> rolesUsuariosRemovidos) {
        this.rolesUsuariosRemovidos = rolesUsuariosRemovidos;
    }
    
    public DualListModel<Permiso> getPermisosDualList() {
        return permisosDualList;
    }
    
    public void setPermisosDualList(DualListModel<Permiso> permisosDualList) {
        this.permisosDualList = permisosDualList;
    }
    
    public List<UsuarioPermiso> getUsuariosPermisoRemovidos() {
        return usuariosPermisoRemovidos;
    }
    
    public void setUsuariosPermisoRemovidos(List<UsuarioPermiso> usuariosPermisoRemovidos) {
        this.usuariosPermisoRemovidos = usuariosPermisoRemovidos;
    }
    
    public DualListModel<Carrera> getCarrerasDualList() {
        return carrerasDualList;
    }
    
    public void setCarrerasDualList(DualListModel<Carrera> carrerasDualList) {
        this.carrerasDualList = carrerasDualList;
    }
    
    public List<UsuarioCarrera> getUsuariosCarrerasRemovidas() {
        return usuariosCarrerasRemovidas;
    }
    
    public void setUsuariosCarrerasRemovidas(List<UsuarioCarrera> usuariosCarrerasRemovidas) {
        this.usuariosCarrerasRemovidas = usuariosCarrerasRemovidas;
    }
    
    public int getNumeroCarrerasDisponibles() {
        return numeroCarrerasDisponibles;
    }
    
    public void setNumeroCarrerasDisponibles(int numeroCarrerasDisponibles) {
        this.numeroCarrerasDisponibles = numeroCarrerasDisponibles;
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
    
    public int getNumeroCarrerasSeleccionadas() {
        return numeroCarrerasSeleccionadas;
    }
    
    public void setNumeroCarrerasSeleccionadas(int numeroCarrerasSeleccionadas) {
        this.numeroCarrerasSeleccionadas = numeroCarrerasSeleccionadas;
    }
    
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }
    
    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }
    
    public String getConfirmarClaveEstudiante() {
        return confirmarClaveEstudiante;
    }
    
    public void setConfirmarClaveEstudiante(String confirmarClaveEstudiante) {
        this.confirmarClaveEstudiante = confirmarClaveEstudiante;
    }
//</editor-fold>
}

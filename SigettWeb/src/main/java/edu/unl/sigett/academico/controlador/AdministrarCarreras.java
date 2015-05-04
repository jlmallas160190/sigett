/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.jlmallas.api.http.UrlConexion;
import org.jlmallas.api.http.dto.SeguridadHttp;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionCarrera;
import edu.jlmallas.academico.entity.Area;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.jlmallas.academico.entity.Nivel;
import com.jlmallas.soporte.entity.Objeto;
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
import org.primefaces.event.FileUploadEvent;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.session.ConfiguracionCarreraFacadeLocal;
import com.jlmallas.soporte.session.ExcepcionFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.NivelService;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import com.jlmallas.soporte.session.ProyectoSoftwareFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.academico.managed.session.SessionArea;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import javax.annotation.PostConstruct;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarCarrera",
            pattern = "/editarCarrera/#{sessionCarrera.carrera.id}",
            viewId = "/faces/pages/academico/areas/editarCarrera.xhtml"
    ),
    @URLMapping(
            id = "crearCarrera",
            pattern = "/crearCarrera/",
            viewId = "/faces/pages/academico/areas/editarCarrera.xhtml"
    )
})
public class AdministrarCarreras implements Serializable {

    @Inject
    private SessionCarrera sessionCarrera;
    @Inject
    private SessionArea sessionArea;
    @Inject
    SessionUsuario sessionUsuario;
    @EJB
    private NivelService nivelFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private ExcepcionFacadeLocal excepcionFacadeLocal;
    @EJB
    private ObjetoFacadeLocal objetoFacadeLocal;
    @EJB
    private ProyectoSoftwareFacadeLocal proyectoSoftwareFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ConfiguracionCarreraFacadeLocal configuracionCarreraFacadeLocal;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;

    @PostConstruct
    public void init() {
        buscar(sessionUsuario.getUsuario(), sessionArea.getArea());
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_carrera");
            if (tienePermiso == 1) {
                sessionCarrera.setCarrera(new Carrera());
                sessionCarrera.setEsEditado(false);
                sessionCarrera.setNivel("");
                navegacion = "pretty:crearCarrera";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al crear Carrera.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (usuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Carrera");
                if (obj == null) {
                    obj = new Objeto(null, "Carrera", "Carrera");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
            }
        }
        return navegacion;
    }

    public List<Nivel> listadoNiveles() {
        try {
            return nivelFacadeLocal.findAll();
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }
        return null;
    }

    public String grabarCarrera(Carrera carrera, String nivel, Area area, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (carrera.getIdSga() == null) {
                carrera.setIdSga("");
            }
            int posNivel = nivel.indexOf(":");
            Nivel nivelObj = nivelFacadeLocal.find(Integer.parseInt(nivel.substring(0, posNivel)));
            if (nivelObj != null) {
                carrera.setNivelId(nivelObj);
            }
            area.getCarreraList().add(carrera);
            carrera.setAreaId(area);
            if (sessionCarrera.getContents() != null) {
                carrera.setLogo(sessionCarrera.getContents());
            }
            if (carrera.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_carrera");
                if (tienePermiso == 1) {
                    carreraFacadeLocal.create(carrera);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Carrera", carrera.getId() + "", "CREAR", "|Nombre=" + carrera.getNombre() + "|IdSga=" + carrera.getIdSga() + "|Área=" + carrera.getAreaId().getId(), usuario));
                    ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera();
                    configuracionCarrera1.setNombre("Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación");
                    configuracionCarrera1.setCodigo("MA");
                    configuracionCarrera1.setValor("?");
                    configuracionCarrera1.setObservacion("S/N");
                    configuracionCarrera1.setCarreraId(carrera.getId());
                    configuracionCarrera1.setTipo("numerico");
                    configuracionCarreraFacadeLocal.create(configuracionCarrera1);

                    ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera();
                    configuracionCarrera3.setNombre("Número de Oficio");
                    configuracionCarrera3.setCodigo("NO");
                    configuracionCarrera3.setValor("1");
                    configuracionCarrera3.setObservacion("Número de Oficio");
                    configuracionCarrera3.setCarreraId(carrera.getId());
                    configuracionCarrera3.setTipo("numerico");
                    configuracionCarreraFacadeLocal.create(configuracionCarrera3);

                    ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera();
                    configuracionCarrera2.setNombre("Número de Modulo aprobado para ser egresado");
                    configuracionCarrera2.setCodigo("ME");
                    configuracionCarrera2.setValor("?");
                    configuracionCarrera2.setObservacion("S/N");
                    configuracionCarrera2.setCarreraId(carrera.getId());
                    configuracionCarrera2.setTipo("numerico");
                    configuracionCarreraFacadeLocal.create(configuracionCarrera2);

                    ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera();
                    configuracionCarrera.setNombre("ID de Oferta Academica Actual de la Carrera");
                    configuracionCarrera.setCodigo("OA");
                    configuracionCarrera.setValor("?");
                    configuracionCarrera.setObservacion("S/N");
                    configuracionCarrera.setCarreraId(carrera.getId());
                    configuracionCarrera.setTipo("boton");
                    configuracionCarreraFacadeLocal.create(configuracionCarrera);

                    ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera();
                    configuracionCarrera4.setNombre("Número de Acta de tesis");
                    configuracionCarrera4.setCodigo("NA");
                    configuracionCarrera4.setValor("?");
                    configuracionCarrera4.setObservacion("S/N");
                    configuracionCarrera4.setCarreraId(carrera.getId());
                    configuracionCarrera4.setTipo("numerico");
                    configuracionCarreraFacadeLocal.create(configuracionCarrera4);
                    if (param.equalsIgnoreCase("grabar")) {
                        carrera = new Carrera();
                        navegacion = "pretty:editarArea";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.carrera") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                carrera = new Carrera();
                            }
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_carrera");
                if (tienePermiso == 1) {
                    carreraFacadeLocal.edit(carrera);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Carrera", carrera.getId() + "", "EDITAR", "|Nombre=" + carrera.getNombre() + "|IdSga=" + carrera.getIdSga() + "|Área=" + carrera.getAreaId().getId(), usuario));
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "MA") == null) {
                        ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera();
                        configuracionCarrera1.setNombre("Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación");
                        configuracionCarrera1.setCodigo("MA");
                        configuracionCarrera1.setValor("?");
                        configuracionCarrera1.setObservacion("S/N");
                        configuracionCarrera1.setCarreraId(carrera.getId());
                        configuracionCarrera1.setTipo("numerico");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera1);
                    }
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "ME") == null) {
                        ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera();
                        configuracionCarrera2.setNombre("Número de Modulo aprobado para ser egresado");
                        configuracionCarrera2.setCodigo("ME");
                        configuracionCarrera2.setValor("?");
                        configuracionCarrera2.setObservacion("S/N");
                        configuracionCarrera2.setCarreraId(carrera.getId());
                        configuracionCarrera2.setTipo("numerico");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera2);
                    }
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "OA") == null) {
                        ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera();
                        configuracionCarrera.setNombre("ID de Oferta Academica Actual de la Carrera");
                        configuracionCarrera.setCodigo("OA");
                        configuracionCarrera.setValor("?");
                        configuracionCarrera.setObservacion("S/N");
                        configuracionCarrera.setCarreraId(carrera.getId());
                        configuracionCarrera.setTipo("boton");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera);
                    }
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "NO") == null) {
                        ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera();
                        configuracionCarrera3.setNombre("Número de Oficio");
                        configuracionCarrera3.setCodigo("NO");
                        configuracionCarrera3.setValor("1");
                        configuracionCarrera3.setObservacion("Número de Oficio");
                        configuracionCarrera3.setCarreraId(carrera.getId());
                        configuracionCarrera3.setTipo("numerico");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera3);
                    }
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "NA") == null) {
                        ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera();
                        configuracionCarrera4.setNombre("Número de Acta de tesis");
                        configuracionCarrera4.setCodigo("NA");
                        configuracionCarrera4.setValor("?");
                        configuracionCarrera4.setObservacion("S/N");
                        configuracionCarrera4.setCarreraId(carrera.getId());
                        configuracionCarrera4.setTipo("numerico");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera4);
                    }

                    if (param.equalsIgnoreCase("grabar")) {
                        carrera = new Carrera();
                        navegacion = "pretty:editarArea";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.carrera") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                carrera = new Carrera();
                            }
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
            buscar(usuario, area);
        } catch (Exception e) {
            String mensaje = "Error al agregar Carrera.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (usuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Carrera");
                if (obj == null) {
                    obj = new Objeto(null, "Carrera", "Carrera");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
            }
        }
        return navegacion;
    }

    public String editar(Carrera carrera, Usuario usuario) {
        String navagacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_carrera");
            if (tienePermiso == 1) {
                sessionCarrera.setCarrera(carrera);
                sessionCarrera.setEsEditado(true);
                sessionCarrera.setNivel(carrera.getNivelId().toString());
                navagacion = "pretty:editarCarrera";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al editar Carrera.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (usuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Carrera");
                if (obj == null) {
                    obj = new Objeto(null, "Carrera", "Carrera");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
            }
        }
        return navagacion;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            sessionCarrera.setContents((event.getFile().getContents()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Carrera> listarPorArea(Area area) {
        try {
            Carrera carreraBuscar = new Carrera();
            carreraBuscar.setAreaId(area);
            return carreraFacadeLocal.buscarPorCriterio(carreraBuscar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void buscar(Usuario usuario, Area area) {
        sessionCarrera.setCarreras(new ArrayList<Carrera>());
        try {
            Carrera carreraBuscar = new Carrera();
            carreraBuscar.setAreaId(area);
            sessionCarrera.setCarreras(carreraFacadeLocal.buscarPorCriterio(carreraBuscar));
        } catch (Exception e) {
            String mensaje = "Error al buscar Carreras por Areas.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Objeto obj = objetoFacadeLocal.buscarPorNombre("Area");
            if (usuario != null) {
                if (obj == null) {
                    obj = new Objeto(null, "Area", "Area");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
            }
        }
    }

    public void grabarCarreras() {
        for (Carrera carrera : sessionCarrera.getCarrerasGrabar()) {
            Carrera c = null;
            carrera.setAreaId(sessionArea.getArea());
            if (!carrera.getIdSga().equalsIgnoreCase("")) {
                c = carreraFacadeLocal.buscarIdSga(carrera.getIdSga());
            } else {
                if (carrera.getId() != null) {
                    c = carreraFacadeLocal.find(carrera.getIdSga());
                }
            }
            if (c == null) {
                if (carrera.getNombreTitulo() != null) {
                    carrera.setNombreTitulo("S/N");
                }
                carreraFacadeLocal.create(carrera);
                ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera();
                configuracionCarrera1.setNombre("Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación");
                configuracionCarrera1.setCodigo("MA");
                configuracionCarrera1.setValor("?");
                configuracionCarrera1.setObservacion("S/N");
                configuracionCarrera1.setCarreraId(carrera.getId());
                configuracionCarrera1.setTipo("numerico");
                configuracionCarreraFacadeLocal.create(configuracionCarrera1);
                ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera();
                configuracionCarrera3.setNombre("Número de Oficio");
                configuracionCarrera3.setCodigo("NO");
                configuracionCarrera3.setValor("1");
                configuracionCarrera3.setObservacion("Número de Oficio");
                configuracionCarrera3.setCarreraId(carrera.getId());
                configuracionCarrera3.setTipo("numerico");
                configuracionCarreraFacadeLocal.create(configuracionCarrera3);
                ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera();
                configuracionCarrera2.setNombre("Número de Modulo aprobado para ser egresado");
                configuracionCarrera2.setCodigo("ME");
                configuracionCarrera2.setValor("?");
                configuracionCarrera2.setObservacion("S/N");
                configuracionCarrera2.setCarreraId(carrera.getId());
                configuracionCarrera2.setTipo("numerico");
                configuracionCarreraFacadeLocal.create(configuracionCarrera2);
                ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera();
                configuracionCarrera.setNombre("ID de Oferta Academica Actual de la Carrera");
                configuracionCarrera.setCodigo("OA");
                configuracionCarrera.setValor("?");
                configuracionCarrera.setObservacion("S/N");
                configuracionCarrera.setCarreraId(carrera.getId());
                configuracionCarrera.setTipo("boton");
                configuracionCarreraFacadeLocal.create(configuracionCarrera);

                ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera();
                configuracionCarrera4.setNombre("Número de Acta de tesis");
                configuracionCarrera4.setCodigo("NA");
                configuracionCarrera4.setValor("?");
                configuracionCarrera4.setObservacion("S/N");
                configuracionCarrera4.setCarreraId(carrera.getId());
                configuracionCarrera4.setTipo("numerico");
                configuracionCarreraFacadeLocal.create(configuracionCarrera4);
            } else {
                if (carrera.isEsEditado()) {
                    c.setNivelId(carrera.getNivelId());
                    c.setNombre(carrera.getNombre());
                    c.setIdSga(carrera.getIdSga());
                    carrera = c;
                    if (carrera.getNombreTitulo() != null) {
                        carrera.setNombreTitulo("S/N");
                    }
                    carreraFacadeLocal.edit(carrera);
                    carrera.setEsEditado(false);
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "MA") == null) {
                        ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera();
                        configuracionCarrera1.setNombre("Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación");
                        configuracionCarrera1.setCodigo("MA");
                        configuracionCarrera1.setValor("?");
                        configuracionCarrera1.setObservacion("S/N");
                        configuracionCarrera1.setCarreraId(carrera.getId());
                        configuracionCarrera1.setTipo("numerico");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera1);
                    }
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "ME") == null) {
                        ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera();
                        configuracionCarrera2.setNombre("Número de Modulo aprobado para ser egresado");
                        configuracionCarrera2.setCodigo("ME");
                        configuracionCarrera2.setValor("?");
                        configuracionCarrera2.setObservacion("S/N");
                        configuracionCarrera2.setCarreraId(carrera.getId());
                        configuracionCarrera2.setTipo("numerico");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera2);
                    }
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "OA") == null) {
                        ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera();
                        configuracionCarrera.setNombre("ID de Oferta Academica Actual de la Carrera");
                        configuracionCarrera.setCodigo("OA");
                        configuracionCarrera.setValor("?");
                        configuracionCarrera.setObservacion("S/N");
                        configuracionCarrera.setTipo("boton");
                        configuracionCarrera.setCarreraId(carrera.getId());
                        configuracionCarreraFacadeLocal.create(configuracionCarrera);
                    }
                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "NO") == null) {
                        ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera();
                        configuracionCarrera3.setNombre("Número de Oficio");
                        configuracionCarrera3.setCodigo("NO");
                        configuracionCarrera3.setValor("1");
                        configuracionCarrera3.setObservacion("Número de Oficio");
                        configuracionCarrera3.setCarreraId(carrera.getId());
                        configuracionCarrera3.setTipo("numerico");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera3);
                    }

                    if (configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "NA") == null) {
                        ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera();
                        configuracionCarrera4.setNombre("Número de Acta de tesis");
                        configuracionCarrera4.setCodigo("NA");
                        configuracionCarrera4.setValor("?");
                        configuracionCarrera4.setObservacion("S/N");
                        configuracionCarrera4.setCarreraId(carrera.getId());
                        configuracionCarrera4.setTipo("numerico");
                        configuracionCarreraFacadeLocal.create(configuracionCarrera4);
                    }
                }
            }
        }
        sessionCarrera.setCarrerasGrabar(new ArrayList<Carrera>());
        buscar(sessionUsuario.getUsuario(), sessionArea.getArea());
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedSgaWs(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_carrera");
        if (tienePermiso == 1) {
            sessionCarrera.setRenderedSincronizar(true);
        } else {
            sessionCarrera.setRenderedSincronizar(false);
        }
    }

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_carrera");
        if (tienePermiso == 1) {
            sessionCarrera.setRenderedCrear(true);
        } else {
            sessionCarrera.setRenderedCrear(false);
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_carrera");
        if (tienePermiso == 1) {
            sessionCarrera.setRenderedEditar(true);
            sessionCarrera.setRenderedNoEditar(false);
        } else {
            sessionCarrera.setRenderedEditar(false);
            sessionCarrera.setRenderedNoEditar(true);
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES">

    public void sgawsListaCarreras(Usuario usuario, Area area) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_carrera") == 1) {
            try {
                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 7).getValor();
                String s = serviceUrl + "?siglas=" + area.getSigla();
                SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralFacadeLocal.find((int) 5).getValor(),
                        s, configuracionGeneralFacadeLocal.find((int) 6).getValor());
                UrlConexion conexion = new UrlConexion();
                String datosJson = conexion.conectar(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement jsonElement = parser.parse(datosJson);
                    parseJsonElement(jsonElement);
                    grabarCarreras();
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private void parseJsonElement(JsonElement elemento) throws Exception {
        if (elemento.isJsonObject()) {
            sessionCarrera.setCarreraWs(new Carrera());
            JsonObject obj = elemento.getAsJsonObject();
            java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
            java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                sessionCarrera.setKey(entrada.getKey());
                try {
                    String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                    JsonParser jp = new JsonParser();
                    JsonElement jsonElement = jp.parse(e);
                    parseJsonElement(jsonElement);
                } catch (Exception e) {
                    parseJsonElement(entrada.getValue());
                }
            }
            return;
        }
        if (elemento.isJsonArray()) {
            JsonArray array = elemento.getAsJsonArray();
            sessionCarrera.setKeyEntero(0);
            sessionCarrera.setCarreraWs(new Carrera());
            java.util.Iterator<JsonElement> iter = array.iterator();
            while (iter.hasNext()) {

                JsonElement entrada = iter.next();
                parseJsonElement(entrada);
            }
            return;
        }
        if (elemento.isJsonPrimitive()) {
            JsonPrimitive valor = elemento.getAsJsonPrimitive();
            if (sessionCarrera.getKeyEntero() == 0) {
                sessionCarrera.getCarreraWs().setIdSga(valor.getAsString());
                sessionCarrera.setKeyEntero(sessionCarrera.getKeyEntero() + 1);
                return;
            }
            if (sessionCarrera.getKeyEntero() == 1) {
                sessionCarrera.getCarreraWs().setNombre(new String(valor.getAsString().getBytes()));
                sessionCarrera.getCarreraWs().setSigla("S/N");
                sessionCarrera.setKeyEntero(sessionCarrera.getKeyEntero() + 1);
                return;
            }
            if (sessionCarrera.getKeyEntero() == 2) {
                sessionCarrera.setKeyEntero(sessionCarrera.getKeyEntero() + 1);
                return;
            }
            if (sessionCarrera.getKeyEntero() == 3) {
                sessionCarrera.getCarreraWs().setModalidad(new String(valor.getAsString().getBytes()));
                sessionCarrera.setKeyEntero(sessionCarrera.getKeyEntero() + 1);
            }
            if (sessionCarrera.getKeyEntero() == 4) {
                Nivel nivelBuscar = new Nivel();
                nivelBuscar.setNombre(valor.getAsString());
                Nivel n = nivelFacadeLocal.buscarPorNombre(nivelBuscar);
                if (n != null) {
                    sessionCarrera.getCarreraWs().setNivelId(n);
                } else {
                    n = new Nivel();
                    n.setNombre(valor.getAsString());
                    nivelFacadeLocal.create(n);
                    sessionCarrera.getCarreraWs().setNivelId(n);
                }
                sessionCarrera.setKeyEntero(sessionCarrera.getKeyEntero() + 1);
                return;
            }
            sessionCarrera.setKeyEntero(sessionCarrera.getKeyEntero() + 1);
            sessionCarrera.getCarreraWs().setEsEditado(true);
            sessionCarrera.getCarrerasGrabar().add(sessionCarrera.getCarreraWs());
        }

    }

    public void sgaWebServicesParalelosCarrera(Carrera c, String param) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        ConexionServicio conexionServicio = new ConexionServicio();
//        String serviceUrl = configuracionGeneralFacadeLocal.find((int) 41).getValor();
//        String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//        String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//        String ofertaIdActual = configuracionCarreraFacadeLocal.buscarPorCarreraId(c.getId(), "OA").getValor();
//        try {
//            String s = serviceUrl + "?id_oferta=" + ofertaIdActual + ";id_carrera=" + c.getIdSga();
//            String datosJson = conexionServicio.conectar(s, userService, passwordService);
//            if (!datosJson.equalsIgnoreCase("")) {
//                JsonParser parser = new JsonParser();
//                JsonElement datos = parser.parse(datosJson);
//                recorrerElementosJsonParalelosCarrera(datos, c, param);
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//
//        } catch (Exception e) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
    }

    private void recorrerElementosJsonParalelosCarrera(JsonElement elemento, Carrera c, String param) throws Exception {
        try {
//            if (elemento.isJsonObject()) {
//                JsonObject obj = elemento.getAsJsonObject();
//                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
//                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
//                while (iter.hasNext()) {
//                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
//                    keyWsParalelosCarrera = entrada.getKey();
//                    try {
//                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
//                        JsonParser jp = new JsonParser();
//                        JsonElement jsonElement = jp.parse(e);
//                        recorrerElementosJsonParalelosCarrera(jsonElement, c, param);
//                        
//                    } catch (Exception e) {
//                        recorrerElementosJsonParalelosCarrera(entrada.getValue(), c, param);
//                    }
//                    
//                }
//                
//            } else if (elemento.isJsonArray()) {
//                JsonArray array = elemento.getAsJsonArray();
//                keyWsParalelosCarreraEntero = 0;
//                java.util.Iterator<JsonElement> iter = array.iterator();
//                while (iter.hasNext()) {
//                    JsonElement entrada = iter.next();
//                    recorrerElementosJsonParalelosCarrera(entrada, c, param);
//                }
//            } else if (elemento.isJsonPrimitive()) {
//                JsonPrimitive valor = elemento.getAsJsonPrimitive();
//                if (valor.isNumber()) {
//                    if (keyWsParalelosCarreraEntero == 0) {
//                        String paraleloId = valor.getAsInt() + "";
//                        if (param.equalsIgnoreCase("estudiantes")) {
//                            administrarEstudiantesCarrera.sgaWebServicesEstadoEstudiantesParalelo(paraleloId, c);
//                        } else {
////                            administrarDocentesCarrera.sgaWebServicesUnidadesDocenteParalelo(paraleloId, c);
//
//                        }
//                        keyWsParalelosCarreraEntero++;
//                    }
//                }
//            }

        } catch (Exception e) {

        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public SessionCarrera getSessionCarrera() {
        return sessionCarrera;
    }

    public void setSessionCarrera(SessionCarrera sessionCarrera) {
        this.sessionCarrera = sessionCarrera;
    }

//</editor-fold>
}

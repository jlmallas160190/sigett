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
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionArea;
import edu.unl.sigett.academico.managed.session.SessionCarrera;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.jlmallas.academico.entity.Area;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.jlmallas.academico.entity.Nivel;
import com.jlmallas.soporte.entity.Objeto;
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
import org.primefaces.event.FileUploadEvent;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.session.ConfiguracionCarreraFacadeLocal;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import com.jlmallas.soporte.session.ExcepcionFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import edu.jlmallas.academico.service.NivelFacadeLocal;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import com.jlmallas.soporte.session.ProyectoSoftwareFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

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
            viewId = "/faces/pages/academico/editarCarrera.xhtml"
    ),
    @URLMapping(
            id = "crearCarrera",
            pattern = "/crearCarrera/",
            viewId = "/faces/pages/academico/editarCarrera.xhtml"
    ),
    @URLMapping(
            id = "carreras",
            pattern = "/carreras/",
            viewId = "/faces/pages/academico/buscarCarreras.xhtml"
    )})
public class AdministrarCarreras implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionArea sessionArea;
    @Inject
    private SessionCarrera sessionCarrera;
    @Inject
    private AdministrarEstudiantesCarrera administrarEstudiantesCarrera;
    @EJB
    private NivelFacadeLocal nivelFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    @EJB
    private ExcepcionFacadeLocal excepcionFacadeLocal;
    @EJB
    private ObjetoFacadeLocal objetoFacadeLocal;
    @EJB
    private ProyectoSoftwareFacadeLocal proyectoSoftwareFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private ConfiguracionCarreraFacadeLocal configuracionCarreraFacadeLocal;

    private String key;
    private int keyEntero;
    private String keyWsParalelosCarrera;
    private int keyWsParalelosCarreraEntero;
    private byte[] contents;
    private String nivel;
    private String criterioCarreras = "";

    private boolean esEditado = false;
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedSincronizar;

    private List<Carrera> carrerasGrabar;
    private List<Carrera> carreras;

    public AdministrarCarreras() {

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
                esEditado = false;
                nivel = "";
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
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Carrera");
                if (obj == null) {
                    obj = new Objeto(null, "Carrera", "Carrera");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return navegacion;
    }

    public List<Nivel> listadoNiveles() {
        try {
            return nivelFacadeLocal.findAll();
        } catch (Exception e) {
            String mensaje = "Error al lista Niveles Carrera.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Carrera");
                if (obj == null) {
                    obj = new Objeto(null, "Carrera", "Carrera");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
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
            if (contents != null) {
                carrera.setLogo(contents);
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
                esEditado = true;
                nivel = carrera.getNivelId().toString();
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
            if (sessionUsuario.getUsuario() != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Carrera");
                if (obj == null) {
                    obj = new Objeto(null, "Carrera", "Carrera");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
            }
        }
        return navagacion;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            contents = (event.getFile().getContents());
        } catch (Exception e) {
        }
    }

    public List<Carrera> listarPorArea(Area area) {
        try {
            return carreraFacadeLocal.buscarPorArea(area.getId());
        } catch (Exception e) {
        }
        return null;
    }

    public void buscar(String criterio, Usuario usuario, Area area) {
        carreras = new ArrayList<>();
        try {
            for (Carrera c : carreraFacadeLocal.buscarPorArea(area.getId())) {
                if (c.getNombre().toUpperCase().contains(criterio.toUpperCase())) {
                    carreras.add(c);
                }
            }

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

    public void grabarCarreras(Area a) {
        for (Carrera carrera : a.getCarreraList()) {
            Carrera c = null;
            if (!carrera.getIdSga().equalsIgnoreCase("")) {
                c = carreraFacadeLocal.buscarIdSga(carrera.getIdSga());
            } else {
                if (carrera.getId() != null) {
                    c = carreraFacadeLocal.find(carrera.getId());
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
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedSgaWs(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_carrera");
        if (tienePermiso == 1) {
            renderedSincronizar = true;
        } else {
            renderedSincronizar = false;
        }
    }

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_carrera");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_carrera");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedEditar = false;
            renderedNoEditar = true;
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES">

    public void sgawsListaCarreras(Usuario usuario, Area area) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_carrera") == 1) {
//            String serviceUrl = configuracionGeneralFacadeLocal.find((int) 7).getValor();
//            String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//            String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//            careraIndex = 0;
//            this.carrerasGrabar = new ArrayList<>();
//            ConexionServicio conexionServicio = new ConexionServicio();
//            try {
//                String s = serviceUrl + "?siglas=" + area.getSigla();
//                String datosJson = conexionServicio.conectar(s, userService, passwordService);
//                if (!datosJson.equalsIgnoreCase("")) {
//                    JsonParser parser = new JsonParser();
//                    JsonElement datos = parser.parse(conexionServicio.conectar(s, userService, passwordService));
//                    dumpJsonElement(datos);
//                    grabarCarreras(area);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } catch (Exception e) {
//                String mensaje = bundle.getString("lbl.no_sincronizar_web_services");
//                if (e.getMessage() != null) {
//                    mensaje = mensaje + "|Informe= " + e.getMessage();
//                }
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//                Objeto obj = objetoFacadeLocal.buscarPorNombre("Carrera");
//                if (sessionUsuario.getUsuario() != null) {
//                    if (obj == null) {
//                        obj = new Objeto(null, "Carrera", "Carrera");
//                        obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
//                        objetoFacadeLocal.create(obj);
//                    }
//                    excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(sessionUsuario.getUsuario().toString(), mensaje, obj));
//                }
//
//            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    Carrera carrera = null;

    private void dumpJsonElement(JsonElement elemento) throws Exception {
        if (elemento.isJsonObject()) {
            carrera = new Carrera();
            JsonObject obj = elemento.getAsJsonObject();
            java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
            java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                key = entrada.getKey();
                try {
                    String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                    JsonParser jp = new JsonParser();
                    JsonElement jsonElement = jp.parse(e);
                    dumpJsonElement(jsonElement);

                } catch (Exception e) {
                    dumpJsonElement(entrada.getValue());
                }

            }

        } else if (elemento.isJsonArray()) {
            JsonArray array = elemento.getAsJsonArray();
            keyEntero = 0;
            carrera = new Carrera();

            java.util.Iterator<JsonElement> iter = array.iterator();
            while (iter.hasNext()) {

                JsonElement entrada = iter.next();
                dumpJsonElement(entrada);
            }
        } else if (elemento.isJsonPrimitive()) {
            JsonPrimitive valor = elemento.getAsJsonPrimitive();
            if (valor.isBoolean()) {
                if (!key.equalsIgnoreCase("result")) {

                } else {
                    keyEntero++;
                }

            } else if (valor.isNumber()) {
                if (!key.equalsIgnoreCase("result")) {

                } else {
                    if (keyEntero == 0) {
                        carrera.setId((Integer) valor.getAsInt());
                        carrera.setIdSga(carrera.getId() + "");
                    }
                    keyEntero++;
                }
            } else if (valor.isString()) {
                if (!key.equalsIgnoreCase("result")) {
                    if (key.equalsIgnoreCase("Nombre")) {
                        carrera.setNombre(new String(valor.getAsString().getBytes()));
                        carrera.setSigla("S/N");
                    } else {
                        if (key.equalsIgnoreCase("Nivel")) {

                        }
                    }
                } else {
                    if (keyEntero == 0) {

                    } else {
                        if (keyEntero == 1) {
                            carrera.setNombre(new String(valor.getAsString().getBytes()));
                            carrera.setSigla("S/N");
                        } else {
                            if (keyEntero == 4) {
                                Nivel n = nivelFacadeLocal.buscarPorNombre(valor.getAsString());
                                if (n != null) {
                                    carrera.setNivelId(n);
                                } else {
                                    n = new Nivel();
                                    n.setNombre(valor.getAsString());
                                    nivelFacadeLocal.create(n);
                                    logFacadeLocal.create(logFacadeLocal.crearLog("NIVEL", n.getId() + "", "CREAR DESDE WS", "|Nombre= " + n.getNombre(), sessionUsuario.getUsuario()));
                                    carrera.setNivelId(n);
                                }
                            } else {
                                if (keyEntero == 3) {
                                    carrera.setModalidad(new String(valor.getAsString().getBytes()));
                                }
                            }
                        }
                    }
                    keyEntero++;
                }

            }
        }
        if (carrera.getNivelId() != null && !carrerasGrabar.contains(carrera)) {
            Carrera c = carreraFacadeLocal.buscarIdSga(carrera.getIdSga());
            if (c == null) {
                sessionArea.getArea().getCarreraList().add(carrera);
                carrera.setEsEditado(true);
                carrerasGrabar.add(carrera);
                carrera.setAreaId(sessionArea.getArea());
            } else {
                carrera.setEsEditado(true);
                sessionArea.getArea().getCarreraList().set(careraIndex, carrera);
                carrerasGrabar.remove(carrera);
                carrerasGrabar.add(carrera);
                carrera.setAreaId(sessionArea.getArea());
            }
            careraIndex++;
        }
    }
    int careraIndex = 0;

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
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    keyWsParalelosCarrera = entrada.getKey();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        recorrerElementosJsonParalelosCarrera(jsonElement, c, param);

                    } catch (Exception e) {
                        recorrerElementosJsonParalelosCarrera(entrada.getValue(), c, param);
                    }

                }

            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                keyWsParalelosCarreraEntero = 0;
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    recorrerElementosJsonParalelosCarrera(entrada, c, param);
                }
            } else if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (valor.isNumber()) {
                    if (keyWsParalelosCarreraEntero == 0) {
                        String paraleloId = valor.getAsInt() + "";
                        if (param.equalsIgnoreCase("estudiantes")) {
                            administrarEstudiantesCarrera.sgaWebServicesEstadoEstudiantesParalelo(paraleloId, c);
                        } else {
//                            administrarDocentesCarrera.sgaWebServicesUnidadesDocenteParalelo(paraleloId, c);

                        }
                        keyWsParalelosCarreraEntero++;
                    }
                }
            }

        } catch (Exception e) {

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

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public String getCriterioCarreras() {
        return criterioCarreras;
    }

    public void setCriterioCarreras(String criterioCarreras) {
        this.criterioCarreras = criterioCarreras;
    }

    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionArea getSessionArea() {
        return sessionArea;
    }

    public void setSessionArea(SessionArea sessionArea) {
        this.sessionArea = sessionArea;
    }

    public SessionCarrera getSessionCarrera() {
        return sessionCarrera;
    }

    public void setSessionCarrera(SessionCarrera sessionCarrera) {
        this.sessionCarrera = sessionCarrera;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
    }

    public List<Carrera> getCarrerasGrabar() {
        return carrerasGrabar;
    }

    public void setCarrerasGrabar(List<Carrera> carrerasGrabar) {
        this.carrerasGrabar = carrerasGrabar;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }
//</editor-fold>
}

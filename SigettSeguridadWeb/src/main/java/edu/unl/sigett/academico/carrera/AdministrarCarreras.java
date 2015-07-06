/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.carrera;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.TipoConfiguracionEnum;
import com.jlmallas.comun.enumeration.URLWSEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.httpClient.ConexionDTO;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Area;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.Nivel;
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
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.NivelService;
import edu.unl.sigett.academico.areaAcademica.SessionArea;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.seguridad.usuario.UsuarioDM;
import edu.unl.sigett.util.CabeceraController;
import org.jlmallas.httpClient.NetClientService;
import org.jlmallas.secure.Secure;
import org.jlmallas.seguridad.service.UsuarioService;

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
    UsuarioDM usuarioDM;
    @Inject
    private CabeceraController cabeceraController;
    @EJB(lookup = "java:global/AcademicoService/NivelServiceImplement!edu.jlmallas.academico.service.NivelService")
    private NivelService nivelService;
    @EJB(lookup = "java:global/AcademicoService/CarreraServiceImplement!edu.jlmallas.academico.service.CarreraService")
    private CarreraService carreraService;
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logFacadeLocal;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/SigettService/ConfiguracionCarreraServiceImplement!edu.unl.sigett.service.ConfiguracionCarreraService")
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;

    public void init() {
        buscar(usuarioDM.getUsuario(), sessionArea.getArea());

    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(usuario, "crear_carrera");
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
        }
        return navegacion;
    }

    public List<Nivel> listadoNiveles() {
        try {
            return nivelService.findAll();
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
            Nivel nivelObj = nivelService.find(Integer.parseInt(nivel.substring(0, posNivel)));
            if (nivelObj != null) {
                carrera.setNivelId(nivelObj);
            }
            area.getCarreraList().add(carrera);
            carrera.setAreaId(area);
            if (sessionCarrera.getContents() != null) {
                carrera.setLogo(sessionCarrera.getContents());
            }
            if (carrera.getId() == null) {
                int tienePermiso = usuarioService.tienePermiso(usuario, "crear_carrera");
                if (tienePermiso == 1) {
                    carreraService.create(carrera);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Carrera", carrera.getId() + "", "CREAR", "|Nombre=" + carrera.getNombre() + "|IdSga="
                            + carrera.getIdSga() + "|Área=" + carrera.getAreaId().getId(), usuario));
                    ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera(null,
                            "Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación", "MA", carrera.getId(), "S/N",
                            TipoConfiguracionEnum.NUMERICO.getTipo());
                    configuracionCarreraService.guardar(configuracionCarrera1);

                    ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera(null,
                            "Número de Oficio", "NO", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                    configuracionCarreraService.guardar(configuracionCarrera3);

                    ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera(null,
                            "Número de Módulo para ser egresado", "ME", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                    configuracionCarreraService.guardar(configuracionCarrera2);
                    ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera(null,
                            "Id de oferta académica", "OA", carrera.getId(), "S/N", TipoConfiguracionEnum.BOTON.getTipo());
                    configuracionCarreraService.guardar(configuracionCarrera);

                    ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera(null,
                            "Número de acta", "NA", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                    configuracionCarreraService.guardar(configuracionCarrera4);
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
                int tienePermiso = usuarioService.tienePermiso(usuario, "editar_carrera");
                if (tienePermiso == 1) {
                    carreraService.edit(carrera);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Carrera", carrera.getId() + "", "EDITAR", "|Nombre=" + carrera.getNombre() + "|IdSga=" + carrera.getIdSga() + "|Área=" + carrera.getAreaId().getId(), usuario));
                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "MA")) == null) {
                        ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera(null,
                                "Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación", "MA", carrera.getId(), "S/N",
                                TipoConfiguracionEnum.NUMERICO.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera1);
                    }
                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "ME")) == null) {
                        ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera(null,
                                "Número de Módulo para ser egresado", "ME", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera2);
                    }
                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "OA")) == null) {
                        ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera(null,
                                "Id de oferta académica", "OA", carrera.getId(), "S/N", TipoConfiguracionEnum.BOTON.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera);
                    }
                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "NO")) == null) {
                        ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera(null,
                                "Número de Oficio", "NO", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera3);
                    }

                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "NA")) == null) {
                        ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera(null,
                                "Número de acta", "NA", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera4);
                    }

                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:editarArea";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.carrera") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
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
        }
        return navegacion;
    }

    public String editar(Carrera carrera, Usuario usuario) {
        String navagacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(usuario, "editar_carrera");
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
            return carreraService.buscarPorCriterio(carreraBuscar);
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
            sessionCarrera.setCarreras(carreraService.buscarPorCriterio(carreraBuscar));
        } catch (Exception e) {
        }
    }

    /**
     * GRABAR CARRERAS LUEGO DE SINCRONIZAR CON EL SERVICIO WEB
     */
    public void grabarCarreras() {
        for (Carrera carrera : sessionCarrera.getCarrerasGrabar()) {
            Carrera c = null;
            carrera.setAreaId(sessionArea.getArea());
            if (!carrera.getIdSga().equalsIgnoreCase("")) {
                c = carreraService.buscarIdSga(carrera.getIdSga());
            } else {
                if (carrera.getId() != null) {
                    c = carreraService.find(carrera.getIdSga());
                }
            }
            if (c == null) {
                if (carrera.getNombreTitulo() != null) {
                    carrera.setNombreTitulo("S/N");
                }
                carreraService.create(carrera);
                ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera(null,
                        "Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación", "MA", carrera.getId(), "S/N",
                        TipoConfiguracionEnum.NUMERICO.getTipo());
                configuracionCarreraService.guardar(configuracionCarrera1);

                ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera(null,
                        "Número de Oficio", "NO", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                configuracionCarreraService.guardar(configuracionCarrera3);

                ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera(null,
                        "Número de Módulo para ser egresado", "ME", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                configuracionCarreraService.guardar(configuracionCarrera2);
                ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera(null,
                        "Id de oferta académica", "OA", carrera.getId(), "S/N", TipoConfiguracionEnum.BOTON.getTipo());
                configuracionCarreraService.guardar(configuracionCarrera);

                ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera(null,
                        "Número de acta", "NA", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                configuracionCarreraService.guardar(configuracionCarrera4);
            } else {
                if (carrera.isEsEditado()) {
                    c.setNivelId(carrera.getNivelId());
                    c.setNombre(carrera.getNombre());
                    c.setIdSga(carrera.getIdSga());
                    carrera = c;
                    if (carrera.getNombreTitulo() != null) {
                        carrera.setNombreTitulo("S/N");
                    }
                    carreraService.edit(carrera);
                    carrera.setEsEditado(false);
                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "MA")) == null) {
                        ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera(null,
                                "Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación", "MA", carrera.getId(), "S/N",
                                TipoConfiguracionEnum.NUMERICO.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera1);
                    }
                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "ME")) == null) {
                        ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera(null,
                                "Número de Módulo para ser egresado", "ME", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera2);
                    }
                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "OA")) == null) {
                        ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera(null,
                                "Id de oferta académica", "OA", carrera.getId(), "S/N", TipoConfiguracionEnum.BOTON.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera);
                    }
                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "NO")) == null) {
                        ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera(null,
                                "Número de Oficio", "NO", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera3);
                    }

                    if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "NA")) == null) {
                        ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera(null,
                                "Número de acta", "NA", carrera.getId(), "S/N", TipoConfiguracionEnum.NUMERICO.getTipo());
                        configuracionCarreraService.guardar(configuracionCarrera4);
                    }
                }
            }
        }
        sessionCarrera.setCarrerasGrabar(new ArrayList<Carrera>());
        buscar(usuarioDM.getUsuario(), sessionArea.getArea());
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedSgaWs(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "sga_ws_carrera");
        if (tienePermiso == 1) {
            sessionCarrera.setRenderedSincronizar(true);
        } else {
            sessionCarrera.setRenderedSincronizar(false);
        }
    }

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "crear_carrera");
        if (tienePermiso == 1) {
            sessionCarrera.setRenderedCrear(true);
        } else {
            sessionCarrera.setRenderedCrear(false);
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "editar_carrera");
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
        if (usuarioService.tienePermiso(usuario, "sga_ws_carrera") == 1) {
            try {
                String claveWS = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String usuarioWs = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String url = configuracionService.buscar(new Configuracion(URLWSEnum.URLCARRERAWS.getTipo())).get(0).getValor() + "?siglas=" + area.getSigla();
                ConexionDTO seguridad = new ConexionDTO(claveWS, url, usuarioWs);
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
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
                Nivel n = nivelService.buscarPorNombre(nivelBuscar);
                if (n != null) {
                    sessionCarrera.getCarreraWs().setNivelId(n);
                } else {
                    n = new Nivel();
                    n.setNombre(valor.getAsString());
                    nivelService.create(n);
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

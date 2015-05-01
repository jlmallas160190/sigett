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
import com.jlmallas.api.http.UrlConexion;
import com.jlmallas.api.http.dto.SeguridadHttp;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionArea;
import edu.jlmallas.academico.entity.Area;
import edu.jlmallas.academico.entity.Carrera;
import com.jlmallas.soporte.entity.Objeto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
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
import edu.jlmallas.academico.service.AreaService;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import com.jlmallas.soporte.session.ExcepcionFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import com.jlmallas.soporte.session.ProyectoSoftwareFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;
import edu.unl.sigett.academico.managed.session.SessionCarrera;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.session.ProyectoCarreraOfertaFacadeLocal;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarArea",
            pattern = "/editarArea/#{sessionArea.area.id}",
            viewId = "/faces/pages/academico/areas/editarArea.xhtml"
    ),
    @URLMapping(
            id = "crearArea",
            pattern = "/crearArea/",
            viewId = "/faces/pages/academico/areas/editarArea.xhtml"
    ),
    @URLMapping(
            id = "areas",
            pattern = "/areas/",
            viewId = "/faces/pages/academico/areas/index.xhtml"
    )})
public class AdministrarAreas implements Serializable {

    @Inject
    private SessionArea sessionArea;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionCarrera sessionCarrera;
    @EJB
    private AreaService areaFacadeLocal;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    @EJB
    private ExcepcionFacadeLocal excepcionFacadeLocal;
    @EJB
    private ObjetoFacadeLocal objetoFacadeLocal;
    @EJB
    private ProyectoSoftwareFacadeLocal proyectoSoftwareFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private ProyectoCarreraOfertaFacadeLocal proyectoCarreraOfertaFacadeLocal;

    private List<Area> directorioAreas;

    private String criterioDirectorioArea;

    public AdministrarAreas() {

    }

    @PostConstruct
    public void init() {
        buscar(sessionUsuario.getUsuario());
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public List<Area> listarTodo() {
        try {
            return areaFacadeLocal.findAll();
        } catch (Exception e) {
        }
        return null;
    }

    public String abrirBuscarAreas(Usuario usuario) {
        String navegacion = "";
        try {
            buscar(usuario);
            renderedCrear(usuario);
            renderedEditar(usuario);
            renderedSgaWs(usuario);
            navegacion = "pretty:areas";
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String crear(Usuario usuario) {
        String navagacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_area");
            if (tienePermiso == 1) {
                sessionArea.setArea(new Area());
                sessionCarrera.setCarreras(sessionArea.getArea().getCarreraList());
                this.renderedCrearCarrera(usuario);
                this.renderedEditarCarrera(usuario);
                this.renderedSgaWsCarrera(usuario);
                navagacion = "pretty:crearArea";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al crear Area...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (usuario != null) {
                if (usuario != null) {
                    Objeto obj = objetoFacadeLocal.buscarPorNombre("Area");
                    if (obj == null) {
                        obj = new Objeto(null, "Area", "Area");
                        obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                        objetoFacadeLocal.create(obj);
                    }
                    excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
                }
            }
        }
        return navagacion;
    }

    public String editar(Area area, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_area");
            if (tienePermiso == 1) {
                sessionArea.setArea(area);
                sessionCarrera.setCarreras(sessionArea.getArea().getCarreraList());
                this.renderedCrearCarrera(usuario);
                this.renderedEditarCarrera(usuario);
                this.renderedSgaWsCarrera(usuario);
                buscar(usuario);
                navegacion = "pretty:editarArea";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al editar Area.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (usuario != null) {
                if (usuario != null) {
                    Objeto obj = objetoFacadeLocal.buscarPorNombre("Area");
                    if (obj == null) {
                        obj = new Objeto(null, "Area", "Area");
                        obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                        objetoFacadeLocal.create(obj);
                    }
                    excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
                }
            }
        }
        return navegacion;
    }

    public String grabar(Area area, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (area.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_area");
                if (tienePermiso == 1) {
                    areaFacadeLocal.guardar(area);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Area", area.getId() + "", "CREAR", "|Nombre= " + area.getNombre() + "|Sigla= "
                            + area.getSigla(), usuario));
                    buscar(usuario);
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionArea.setArea(new Area());
                        return navegacion = "pretty:areas";
                    }
                    if (param.equalsIgnoreCase("grabar-editar")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        return navegacion;
                    }
                    if (param.equalsIgnoreCase("grabar-crear")) {
                        sessionArea.setArea(new Area());
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    return navegacion;
                }

                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                return navegacion;
            }
            if (area.getId() != null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_area");
                if (tienePermiso == 1) {
                    sessionArea.getArea().setCarreraList(new ArrayList<Carrera>());
                    areaFacadeLocal.actualizar(area);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Area", area.getId() + "", "EDITAR", "|Nombre= " + area.getNombre() + "|Sigla= "
                            + area.getSigla(), usuario));
                    buscar(usuario);
                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:areas";
                        return navegacion;
                    }
                    if (param.equalsIgnoreCase("grabar-editar")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        return navegacion;
                    }
                    if (param.equalsIgnoreCase("grabar-crear")) {
                        sessionArea.setArea(new Area());
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    return navegacion;
                }
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". "
                            + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                return navegacion;
            }
        } catch (Exception e) {
            String mensaje = "Error al grabar Area...";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (usuario != null) {
                if (usuario != null) {
                    Objeto obj = objetoFacadeLocal.buscarPorNombre("Area");
                    if (obj == null) {
                        obj = new Objeto(null, "Area", "Area");
                        obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                        objetoFacadeLocal.create(obj);
                    }
                    excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
                }
            }
        }
        return navegacion;
    }

    public void directorioAreas(String criterio) {
        this.directorioAreas = new ArrayList<>();
        try {
            Area areaBuscar = new Area();
            areaBuscar.setNombre(criterio);
            areaBuscar.setSigla(criterio);
            areaBuscar.setSecretario(criterio);
            for (Area a : areaFacadeLocal.buscarPorCriterio(areaBuscar)) {
                for (Carrera c : a.getCarreraList()) {
                    if (directorioAreas.contains(a)) {
                        for (ProyectoCarreraOferta p : proyectoCarreraOfertaFacadeLocal.buscarPorCarrera(c.getId())) {
                            if (p.getEsActivo()) {
                                if (contieneArea(directorioAreas, a) == false) {
                                    directorioAreas.add(a);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public List<Carrera> directorioCarreras(Area a) {
        List<Carrera> directorioCarreras = new ArrayList<>();
        try {
            for (Carrera c : a.getCarreraList()) {
                for (ProyectoCarreraOferta proyectoCarreraOferta : proyectoCarreraOfertaFacadeLocal.buscarPorCarrera(c.getId())) {
                    if (proyectoCarreraOferta.getEsActivo()) {
                        if (contieneCarrera(directorioCarreras, c) == false) {
                            directorioCarreras.add(c);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return directorioCarreras;
    }

    public boolean contieneArea(List<Area> cs, Area a) {
        boolean var = false;
        for (Area area : cs) {
            if (area == a) {
                var = true;
                break;
            }
        }
        return var;
    }

    public boolean contieneCarrera(List<Carrera> cs, Carrera c) {
        boolean var = false;
        for (Carrera carrera : cs) {
            if (carrera == c) {
                var = true;
                break;
            }
        }
        return var;
    }

    public void buscar(Usuario usuario) {
        sessionArea.setAreas(new ArrayList<Area>());
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_area");
            if (tienePermiso == 1) {
                sessionArea.setAreas(areaFacadeLocal.buscarPorCriterio(new Area()));
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            String mensaje = "Error al buscar Areas.";
            if (e.getMessage() != null) {
                mensaje = mensaje + "|Informe= " + e.getMessage();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (usuario != null) {
                Objeto obj = objetoFacadeLocal.buscarPorNombre("Area");
                if (obj == null) {
                    obj = new Objeto(null, "Area", "Area");
                    obj.setProyectoSoftwareId(proyectoSoftwareFacadeLocal.find((int) 1));
                    objetoFacadeLocal.create(obj);
                }
                excepcionFacadeLocal.create(excepcionFacadeLocal.crearExcepcion(usuario.toString(), mensaje, obj));
            }
        }
    }

    private void guardarAreas(Usuario usuario) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-Real-IP");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        for (Area a : sessionArea.getAreasWS()) {
            Area areaBuscar = new Area();
            areaBuscar.setSigla(a.getSigla());
            List<Area> areas = areaFacadeLocal.buscarPorCriterio(areaBuscar);
            Area a1 = !areas.isEmpty() ? areas.get(0) : null;
            if (a1 == null) {
                areaFacadeLocal.guardar(a);
                logFacadeLocal.create(logFacadeLocal.crearLog("Area", a.getId() + "", "CREAR", "|Nombre= " + a.getNombre() + "|Sigla= "
                        + a.getSigla()+"|"+ipAddress, usuario));
            } else {
                a1.setNombre(a.getNombre());
                a1.setSigla(a.getSigla());
                a1.setSecretario(a.getSecretario());
                a = a1;
                areaFacadeLocal.actualizar(a);
                logFacadeLocal.create(logFacadeLocal.crearLog("Area", a.getId() + "", "EDITAR", "|Nombre= " + a.getNombre() + "|Sigla= "
                        + a.getSigla()+"|"+ipAddress, usuario));
            }
        }
        buscar(usuario);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS WEB SERVICES CONSUMES">
    public void sgawsListaAreas(Usuario usuario) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_area") == 1) {
                SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralFacadeLocal.find((int) 5).getValor(),
                        configuracionGeneralFacadeLocal.find((int) 4).getValor(), configuracionGeneralFacadeLocal.find((int) 6).getValor());
                UrlConexion conexion = new UrlConexion();
                String datosJson = conexion.conectar(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement jsonElement = parser.parse(datosJson);
                    parseElementosJson(jsonElement);
                    guardarAreas(usuario);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }

    }

    private void parseElementosJson(JsonElement elemento) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                sessionArea.setAreaWs(new Area());
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    sessionArea.setKey(entrada.getKey());
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parseElementosJson(jsonElement);

                    } catch (Exception e) {
                        parseElementosJson(entrada.getValue());
                    }

                }
                return;
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionArea.setKeyEntero(0);
                sessionArea.setAreaWs(new Area());
                sessionArea.setEsNuevaArea(true);
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parseElementosJson(entrada);
                }
                return;
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (!valor.isString()) {
                    return;
                }
                if (sessionArea.getKeyEntero() == 0) {
                    sessionArea.getAreaWs().setSigla(new String(valor.getAsString().getBytes()));
                    sessionArea.setKeyEntero(sessionArea.getKeyEntero() + 1);
                    return;
                }
                if (sessionArea.getKeyEntero() == 1) {
                    sessionArea.getAreaWs().setNombre(new String(valor.getAsString().getBytes()));
                    sessionArea.setKeyEntero(sessionArea.getKeyEntero() + 1);
                    return;
                }
                if (sessionArea.getKeyEntero() == 2) {
                    sessionArea.getAreaWs().setSecretario(new String(valor.getAsString().getBytes()));
                    sessionArea.setKeyEntero(sessionArea.getKeyEntero() + 1);
                }
                sessionArea.setKeyEntero(sessionArea.getKeyEntero() + 1);
                sessionArea.getAreasWS().add(sessionArea.getAreaWs());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedSgaWsCarrera(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_carrera");
        if (tienePermiso == 1) {
            sessionCarrera.setRenderedSincronizar(true);
        } else {
            sessionCarrera.setRenderedSincronizar(false);
        }
    }

    public void renderedCrearCarrera(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_carrera");
        if (tienePermiso == 1) {
            sessionCarrera.setRenderedCrear(true);
        } else {
            sessionCarrera.setRenderedCrear(false);
        }
    }

    public void renderedEditarCarrera(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_carrera");
        if (tienePermiso == 1) {
            sessionCarrera.setRenderedEditar(true);
            sessionCarrera.setRenderedNoEditar(false);
        } else {
            sessionCarrera.setRenderedEditar(false);
            sessionCarrera.setRenderedNoEditar(true);
        }
    }

    public void renderedSgaWs(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_area");
        if (tienePermiso == 1) {
            sessionArea.setRenderedSincronizar(true);
        } else {
            sessionArea.setRenderedSincronizar(false);
        }
    }

    public boolean renderedCarreras(Area area) {
        boolean var = false;
        if (area.getId() != null) {
            var = true;
        }
        return var;
    }

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_area");
        if (tienePermiso == 1) {
            sessionArea.setRenderedCrear(true);
        } else {
            sessionArea.setRenderedCrear(false);
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_area");
        if (tienePermiso == 1) {
            sessionArea.setRenderedEditar(true);
            sessionArea.setRenderedNoEditar(false);
        } else {
            sessionArea.setRenderedEditar(false);
            sessionArea.setRenderedNoEditar(true);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public SessionArea getSessionArea() {
        return sessionArea;
    }

    public void setSessionArea(SessionArea sessionArea) {
        this.sessionArea = sessionArea;
    }

    public String getCriterioDirectorioArea() {
        return criterioDirectorioArea;
    }

    public void setCriterioDirectorioArea(String criterioDirectorioArea) {
        this.criterioDirectorioArea = criterioDirectorioArea;
    }

    public List<Area> getDirectorioAreas() {
        return directorioAreas;
    }

    public void setDirectorioAreas(List<Area> directorioAreas) {
        this.directorioAreas = directorioAreas;
    }
//</editor-fold>
}

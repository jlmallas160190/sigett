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
import edu.unl.sigett.session.ProyectoCarreraOfertaFacadeLocal;
import java.util.HashMap;
import java.util.Map;

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
            viewId = "/faces/pages/academico/editarArea.xhtml"
    ),
    @URLMapping(
            id = "crearArea",
            pattern = "/crearArea/",
            viewId = "/faces/pages/academico/editarArea.xhtml"
    ),
    @URLMapping(
            id = "areas",
            pattern = "/areas/",
            viewId = "/faces/pages/academico/buscarAreas.xhtml"
    )})
public class AdministrarAreas implements Serializable {

    @Inject
    private SessionArea sessionArea;
    @Inject
    private AdministrarCarreras administrarCarreras;
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

    private List<Area> areas;
    private List<Carrera> carrerasGrabar = new ArrayList<>();
    private List<Area> directorioAreas;

    private boolean renderedNoEditar;

    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedSincronizar;

    private String areaConverter;

    private String criterioDirectorioArea;
    private String criterio;

    public AdministrarAreas() {

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
                carrerasGrabar = new ArrayList<>();
                administrarCarreras.renderedCrear(usuario);
                administrarCarreras.renderedEditar(usuario);
                administrarCarreras.renderedSgaWs(usuario);
                administrarCarreras.buscar("", usuario, sessionArea.getArea());
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
                administrarCarreras.buscar("", usuario, sessionArea.getArea());
                administrarCarreras.renderedCrear(usuario);
                administrarCarreras.renderedEditar(usuario);
                administrarCarreras.renderedSgaWs(usuario);
                buscar("", usuario);
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
                    logFacadeLocal.create(logFacadeLocal.crearLog("Area", area.getId() + "", "CREAR", "|Nombre= " + area.getNombre() + "|Sigla= " + area.getSigla(), usuario));
                    buscar("", usuario);
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionArea.setArea(new Area());
                        navegacion = "pretty:areas";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                sessionArea.setArea(new Area());
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_area");
                if (tienePermiso == 1) {
                    carrerasGrabar = new ArrayList<>();
                    sessionArea.getArea().setCarreraList(new ArrayList<Carrera>());
                    areaFacadeLocal.actualizar(area);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Area", area.getId() + "", "EDITAR", "|Nombre= " + area.getNombre() + "|Sigla= " + area.getSigla(), usuario));
                    carrerasGrabar = area.getCarreraList();
                    buscar("", usuario);
                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:areas";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                sessionArea.setArea(new Area());
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
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

    public void buscar(String criterio, Usuario usuario) {
        this.areas = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_area");
            if (tienePermiso == 1) {
                Area areaBuscar = new Area();
                areaBuscar.setNombre(criterio);
                areaBuscar.setSigla(criterio);
                areaBuscar.setSecretario(criterio);
                areas = areaFacadeLocal.buscarPorCriterio(areaBuscar);
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
        for (Area a : sessionArea.getAreasWS()) {
            Area areaBuscar = new Area();
            areaBuscar.setSigla(a.getSigla());
            List<Area> areas = areaFacadeLocal.buscarPorCriterio(areaBuscar);
            Area a1 = !areas.isEmpty() ? areas.get(0) : null;
            if (a1 == null) {
                areaFacadeLocal.guardar(a);
                logFacadeLocal.create(logFacadeLocal.crearLog("Area", a.getId() + "", "CREAR", "|Nombre= " + a.getNombre() + "|Sigla= " + a.getSigla(), usuario));
            } else {
                a1.setNombre(a.getNombre());
                a1.setSigla(a.getSigla());
                a1.setSecretario(a.getSecretario());
                a = a1;
                areaFacadeLocal.actualizar(a);
                logFacadeLocal.create(logFacadeLocal.crearLog("Area", a.getId() + "", "EDITAR", "|Nombre= " + a.getNombre() + "|Sigla= " + a.getSigla(), usuario));
            }

        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS WEB SERVICES CONSUMES">
    public void sgawsListaAreas(Usuario usuario) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_area") == 1) {
            Map parametros = new HashMap();
            String serviceUrl = configuracionGeneralFacadeLocal.find((int) 4).getValor();
            String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
            String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
            String s = serviceUrl;
            parametros.put("url", s);
            parametros.put("clave", passwordService);
            parametros.put("usuario", userService);
            parametros.put("msm_sincronizado", bundle.getString("lbl.sincronizado"));
            parametros.put("msm_no_sincronizado", bundle.getString("lbl.no_sincronizar_web_services"));
//            Map resultado = areaConsumeService.getDatosArea(parametros);
//            sessionArea.setAreasWS((List<Area>) resultado.get("areas"));
            guardarAreas(usuario);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedSgaWs(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_area");
        if (tienePermiso == 1) {
            renderedSincronizar = true;
        } else {
            renderedSincronizar = false;
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
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_area");
        if (tienePermiso == 1) {
            renderedNoEditar = false;
            renderedEditar = true;
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

    public String getAreaConverter() {
        return areaConverter;
    }

    public void setAreaConverter(String areaConverter) {
        this.areaConverter = areaConverter;
    }

    public SessionArea getSessionArea() {
        return sessionArea;
    }

    public void setSessionArea(SessionArea sessionArea) {
        this.sessionArea = sessionArea;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<Carrera> getCarrerasGrabar() {
        return carrerasGrabar;
    }

    public void setCarrerasGrabar(List<Carrera> carrerasGrabar) {
        this.carrerasGrabar = carrerasGrabar;
    }

    public String getCriterioDirectorioArea() {
        return criterioDirectorioArea;
    }

    public void setCriterioDirectorioArea(String criterioDirectorioArea) {
        this.criterioDirectorioArea = criterioDirectorioArea;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<Area> getDirectorioAreas() {
        return directorioAreas;
    }

    public void setDirectorioAreas(List<Area> directorioAreas) {
        this.directorioAreas = directorioAreas;
    }
//</editor-fold>
}

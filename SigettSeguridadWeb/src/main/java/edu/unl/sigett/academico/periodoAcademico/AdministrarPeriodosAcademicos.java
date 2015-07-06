/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.periodoAcademico;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.URLWSEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.httpClient.ConexionDTO;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.entity.PeriodoAcademico;
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
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.jlmallas.academico.service.PeriodoAcademicoService;
import edu.unl.sigett.academico.ofertaAcademica.SessionOfertaAcademica;
import edu.unl.sigett.seguridad.usuario.UsuarioDM;
import edu.unl.sigett.util.CabeceraController;
import javax.annotation.PostConstruct;
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
            id = "editarPeriodoAcademico",
            pattern = "/periodoAcademico/#{sessionPeriodoAcademico.periodoAcademico.id}",
            viewId = "/faces/pages/academico/periodosAcademicos/editarPeriodoAcademico.xhtml"
    ),
    @URLMapping(
            id = "crearPeriodoAcademico",
            pattern = "/crearPeriodoAcademico/",
            viewId = "/faces/pagesacademico/academico/periodosAcademicos/editarPeriodoAcademico.xhtml"
    ),
    @URLMapping(
            id = "periodosAcademicos",
            pattern = "/periodosAcademicos/",
            viewId = "/faces/pages/academico/periodosAcademicos/index.xhtml"
    )})
public class AdministrarPeriodosAcademicos implements Serializable {

    @Inject
    private UsuarioDM usuarioDM;
    @Inject
    private SessionPeriodoAcademico sessionPeriodoAcademico;
    @Inject
    private SessionOfertaAcademica sessionOfertaAcademica;
    @Inject
    private CabeceraController cabeceraController;

    @EJB(lookup = "java:global/AcademicoService/PeriodoAcademicoServiceImplement!edu.jlmallas.academico.service.PeriodoAcademicoService")
    private PeriodoAcademicoService periodoAcademicoFacadeLocal;
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logFacadeLocal;
    @EJB(lookup = "java:global/AcademicoService/OfertaAcademicaServiceImplement!edu.jlmallas.academico.service.OfertaAcademicaService")
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    @PostConstruct
    public void init() {
        buscar(usuarioDM.getUsuario());
    }

    public String abrirBuscarPeriodosAcademicos(Usuario usuario) {
        String navegacion = "";
        try {
            buscar(usuario);
            renderedCrear(usuario);
            renderedEditar(usuario);
            renderedSgaWs(usuario);
            navegacion = "pretty:periodosAcademicos";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navegacion;
    }

    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(usuario, "crear_periodo_academico");
            if (tienePermiso == 1) {
                sessionPeriodoAcademico.setPeriodoAcademico(new PeriodoAcademico());
                sessionPeriodoAcademico.getPeriodoAcademico().setIdSga("0");
                this.renderedCrearOferta(usuario);
                this.renderedEditarOferta(usuario);
                this.renderedSincronizarOferta(usuario);
                sessionOfertaAcademica.getOfertaAcademicas().clear();
                sessionOfertaAcademica.getOfertaAcademicasFilter().clear();
                renderedOfertas(sessionPeriodoAcademico.getPeriodoAcademico());
                navegacion = "pretty:crearPeriodoAcademico";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navegacion;
    }

    public String editar(PeriodoAcademico periodoAcademico, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioService.tienePermiso(usuarioDM.getUsuario(), "editar_periodo_academico");
            if (tienePermiso == 1) {
                sessionOfertaAcademica.getOfertaAcademicas().clear();
                sessionOfertaAcademica.getOfertaAcademicasFilter().clear();
                periodoAcademico = periodoAcademicoFacadeLocal.buscarPorId(periodoAcademico.getId());
                sessionPeriodoAcademico.setPeriodoAcademico(periodoAcademico);
                sessionOfertaAcademica.getOfertaAcademicas().addAll(periodoAcademico.getOfertaAcademicaList());
                sessionOfertaAcademica.setOfertaAcademicasFilter(sessionOfertaAcademica.getOfertaAcademicas());
                this.renderedCrearOferta(usuario);
                this.renderedEditarOferta(usuario);
                this.renderedSincronizarOferta(usuario);
                renderedOfertas(sessionPeriodoAcademico.getPeriodoAcademico());
                if (param.equalsIgnoreCase("editar")) {
                    navegacion = "pretty:editarPeriodoAcademico";
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public String grabar(PeriodoAcademico periodoAcademico, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (periodoAcademico.getId() == null) {
                int tienePermiso = usuarioService.tienePermiso(usuario, "crear_periodo_academico");
                if (tienePermiso == 1) {
                    periodoAcademicoFacadeLocal.guardar(periodoAcademico);
                    logFacadeLocal.create(logFacadeLocal.crearLog("PeriodoAcademico", periodoAcademico.getId() + "", "CREAR", "|Fecha Inicio" + periodoAcademico.getFechaInicio() + "|Fecha Fin= " + periodoAcademico.getFechaFin(), usuarioDM.getUsuario()));
                    renderedOfertas(periodoAcademico);
                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:periodosAcademicos";
                        sessionPeriodoAcademico.setPeriodoAcademico(new PeriodoAcademico());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                sessionPeriodoAcademico.setPeriodoAcademico(new PeriodoAcademico());
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
                int tienePermiso = usuarioService.tienePermiso(usuarioDM.getUsuario(), "editar_periodo_academico");
                if (tienePermiso == 1) {
                    grabarOfertas(sessionOfertaAcademica.getOfertaAcademicas());
                    OfertaAcademica primerOferta = ofertaAcademicaService.primerOfertaPorFechaYPeriodoLectivo(periodoAcademico.getId());
                    if (primerOferta != null) {
                        periodoAcademico.setFechaInicio(primerOferta.getFechaInicio());
                    }
                    OfertaAcademica ultimaOferta = ofertaAcademicaService.ultimaOfertaPorFechaYPeriodoLectivo(periodoAcademico.getId());
                    if (ultimaOferta != null) {
                        periodoAcademico.setFechaFin(ultimaOferta.getFechaFin());
                    }
                    periodoAcademicoFacadeLocal.actualizar(periodoAcademico);
                    logFacadeLocal.create(logFacadeLocal.crearLog("PeriodoAcademico", periodoAcademico.getId() + "", "EDITAR", "|Fecha Inicio" + periodoAcademico.getFechaInicio() + "|Fecha Fin= " + periodoAcademico.getFechaFin(), usuarioDM.getUsuario()));
                    renderedOfertas(periodoAcademico);
                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:periodosAcademicos";
                        sessionPeriodoAcademico.setPeriodoAcademico(new PeriodoAcademico());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                sessionPeriodoAcademico.setPeriodoAcademico(new PeriodoAcademico());
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
            buscar(usuario);
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void grabarOfertas(List<OfertaAcademica> ofertaAcademicas) {
        for (OfertaAcademica ofertaAcademica : ofertaAcademicas) {
            ofertaAcademica.setPeriodoAcademicoId(sessionPeriodoAcademico.getPeriodoAcademico());
            if (ofertaAcademica.getIdSga() == null) {
                ofertaAcademica.setIdSga("");
            }
            OfertaAcademica oa = ofertaAcademicaService.buscarPorIdSga(ofertaAcademica.getIdSga());
            if (oa != null) {
                continue;
            }
            if (ofertaAcademica.getId() == null) {
                ofertaAcademicaService.create(ofertaAcademica);
                logFacadeLocal.create(logFacadeLocal.crearLog("OfertaAcademica", ofertaAcademica.getId() + "", "CREAR", "|IdSga= "
                        + ofertaAcademica.getIdSga() + "|Nombre= " + ofertaAcademica.getNombre() + "|FechaInicio= "
                        + ofertaAcademica.getFechaInicio() + "|FechaFin" + ofertaAcademica.getFechaFin(), usuarioDM.getUsuario()));
            } else {
                ofertaAcademica.setFechaFin(ofertaAcademica.getFechaFin());
                ofertaAcademica.setFechaInicio(ofertaAcademica.getFechaInicio());
                ofertaAcademica.setIdSga(ofertaAcademica.getIdSga());
                ofertaAcademica.setNombre(ofertaAcademica.getNombre());
                ofertaAcademicaService.edit(ofertaAcademica);
                logFacadeLocal.create(logFacadeLocal.crearLog("OfertaAcademica", ofertaAcademica.getId() + "", "EDITAR", "|IdSga= "
                        + ofertaAcademica.getIdSga() + "|Nombre= " + ofertaAcademica.getNombre() + "|FechaInicio= " + ofertaAcademica.getFechaInicio()
                        + "|FechaFin" + ofertaAcademica.getFechaFin(), usuarioDM.getUsuario()));
            }
        }
    }

    public void buscar(Usuario usuario) {
        try {
            sessionPeriodoAcademico.setPeriodoAcademicos(new ArrayList<PeriodoAcademico>());
            sessionPeriodoAcademico.setPeriodoAcademicosFilter(new ArrayList<PeriodoAcademico>());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(usuario, "buscar_periodo_academico");
            if (tienePermiso == 1) {
                PeriodoAcademico periodoAcademicoBuscar = new PeriodoAcademico();
                sessionPeriodoAcademico.setPeriodoAcademicos(periodoAcademicoFacadeLocal.buscarPorCriterio(periodoAcademicoBuscar));
                sessionPeriodoAcademico.setPeriodoAcademicosFilter(sessionPeriodoAcademico.getPeriodoAcademicos());
                return;
            }
            if (tienePermiso == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void grabarPeriodos() {
        for (PeriodoAcademico pa : sessionPeriodoAcademico.getPeriodoAcademicosGrabar()) {

            PeriodoAcademico p = periodoAcademicoFacadeLocal.buscarPorIdSga(new PeriodoAcademico(null, null, null, pa.getIdSga()));
            if (p == null) {
                periodoAcademicoFacadeLocal.guardar(pa);
                logFacadeLocal.create(logFacadeLocal.crearLog("PeriodoAcademico", pa.getId() + "", "CREAR", "|Fecha Inicio" + pa.getFechaInicio() + "|Fecha Fin= " + pa.getFechaFin(), usuarioDM.getUsuario()));
                continue;
            }
            if (p != null) {
                p.setFechaFin(pa.getFechaFin());
                p.setFechaInicio(pa.getFechaInicio());
                p.setIdSga(pa.getIdSga());
                periodoAcademicoFacadeLocal.actualizar(p);
                logFacadeLocal.create(logFacadeLocal.crearLog("PeriodoAcademico", pa.getId() + "", "EDITAR", "|Fecha Inicio" + pa.getFechaInicio() + "|Fecha Fin= " + pa.getFechaFin(), usuarioDM.getUsuario()));
                continue;
            }
        }
        sessionPeriodoAcademico.setPeriodoAcademicosGrabar(new ArrayList<PeriodoAcademico>());
        buscar(usuarioDM.getUsuario());
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES CONSUMES">

    public void sgawsPeriodosAcademicos(Usuario usuario) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (usuarioService.tienePermiso(usuario, "sga_ws_periodo_academico") == 1) {
                String claveWS = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String usuarioWs = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String url = configuracionService.buscar(new Configuracion(URLWSEnum.URLPERIODOLECTIVOWS.getTipo())).get(0).getValor();
                ConexionDTO seguridad = new ConexionDTO(claveWS, url, usuarioWs);
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement jsonElement = parser.parse(datosJson);
                    recorrerElementosJson(jsonElement);
                    grabarPeriodos();
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
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private void recorrerElementosJson(JsonElement elemento) throws Exception {
        if (elemento.isJsonObject()) {
            sessionPeriodoAcademico.setPeriodoAcademicoWs(new PeriodoAcademico());
            JsonObject obj = elemento.getAsJsonObject();
            java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
            java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                sessionPeriodoAcademico.setKey(entrada.getKey());
                try {
                    String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                    JsonParser jp = new JsonParser();
                    JsonElement jsonElement = jp.parse(e);
                    recorrerElementosJson(jsonElement);
                } catch (Exception e) {
                    recorrerElementosJson(entrada.getValue());
                }
            }
            return;
        }
        if (elemento.isJsonArray()) {
            JsonArray array = elemento.getAsJsonArray();
            sessionPeriodoAcademico.setKeyEntero(0);
            sessionPeriodoAcademico.setPeriodoAcademicoWs(new PeriodoAcademico());
            sessionPeriodoAcademico.setEsNuevoPeriodo(true);
            java.util.Iterator<JsonElement> iter = array.iterator();
            while (iter.hasNext()) {
                JsonElement entrada = iter.next();
                recorrerElementosJson(entrada);
            }
            return;
        }
        if (elemento.isJsonPrimitive()) {
            JsonPrimitive valor = elemento.getAsJsonPrimitive();
            if (sessionPeriodoAcademico.getKeyEntero() == 0) {
                sessionPeriodoAcademico.getPeriodoAcademicoWs().setIdSga(valor.getAsString());
                sessionPeriodoAcademico.setKeyEntero(sessionPeriodoAcademico.getKeyEntero() + 1);
                return;
            }
            if (sessionPeriodoAcademico.getKeyEntero() == 1) {
                String fecha = valor.getAsString();
                int pos = fecha.indexOf("-");
                if (pos > 0) {
                    String fechaInicio = fecha.substring(0, pos);
                    String fechaFin = fecha.substring(pos + 1);
                    sessionPeriodoAcademico.getPeriodoAcademicoWs().setFechaInicio(cabeceraController.getUtilService().parserFecha(fechaInicio + "-01-01", "yyyy-MM-dd"));
                    sessionPeriodoAcademico.getPeriodoAcademicoWs().setFechaFin(cabeceraController.getUtilService().parserFecha(fechaFin + "-01-01", "yyyy-MM-dd"));
                }
                sessionPeriodoAcademico.setKeyEntero(sessionPeriodoAcademico.getKeyEntero() + 1);
            }
            sessionPeriodoAcademico.setKeyEntero(sessionPeriodoAcademico.getKeyEntero() + 1);
            if (sessionPeriodoAcademico.isEsNuevoPeriodo() == true && sessionPeriodoAcademico.getPeriodoAcademicoWs().getIdSga() != null
                    && sessionPeriodoAcademico.getPeriodoAcademicoWs().getFechaInicio() != null
                    && sessionPeriodoAcademico.getPeriodoAcademicoWs().getFechaFin() != null) {
                sessionPeriodoAcademico.getPeriodoAcademicosGrabar().add(sessionPeriodoAcademico.getPeriodoAcademicoWs());
            }
        }

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedCrearOferta(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "crear_oferta_academica");
        if (tienePermiso == 1) {
            sessionOfertaAcademica.setRenderedCrear(true);
        } else {
            sessionOfertaAcademica.setRenderedCrear(false);
        }
    }

    public void renderedEditarOferta(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "editar_oferta_academica");
        if (tienePermiso == 1) {
            sessionOfertaAcademica.setRenderedEditar(true);
            sessionOfertaAcademica.setRenderedNoEditar(false);
        } else {
            sessionOfertaAcademica.setRenderedEditar(false);
            sessionOfertaAcademica.setRenderedNoEditar(true);
        }
    }

    public void renderedSincronizarOferta(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "sga_ws_oferta_academica");
        if (tienePermiso == 1) {
            sessionOfertaAcademica.setRenderedSincronizar(true);
        } else {
            sessionOfertaAcademica.setRenderedSincronizar(false);
        }
    }

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuarioDM.getUsuario(), "crear_periodo_academico");
        if (tienePermiso == 1) {
            sessionPeriodoAcademico.setRenderedCrear(true);
        } else {
            sessionPeriodoAcademico.setRenderedCrear(false);
        }
    }

    public void renderedSgaWs(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "sga_ws_periodo_academico");
        if (tienePermiso == 1) {
            sessionPeriodoAcademico.setRenderedSincronizar(true);
        } else {
            sessionPeriodoAcademico.setRenderedSincronizar(false);
        }
    }

    public void renderedOfertas(PeriodoAcademico periodoAcademico) {
        if (periodoAcademico.getId() != null) {
            sessionOfertaAcademica.setRenderedTodo(true);
        } else {
            sessionOfertaAcademica.setRenderedTodo(false);
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuarioDM.getUsuario(), "editar_periodo_academico");
        if (tienePermiso == 1) {
            sessionPeriodoAcademico.setRenderedEditar(true);
            sessionPeriodoAcademico.setRenderedNoEditar(false);
        } else {
            sessionPeriodoAcademico.setRenderedEditar(false);
            sessionPeriodoAcademico.setRenderedNoEditar(true);
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

//</editor-fold>
}

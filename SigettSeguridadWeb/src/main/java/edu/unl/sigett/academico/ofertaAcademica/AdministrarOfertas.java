package edu.unl.sigett.academico.ofertaAcademica;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.URLWSEnum;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.httpClient.ConexionDTO;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.entity.PeriodoAcademico;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.seguridad.usuario.UsuarioDM;
import edu.unl.sigett.util.CabeceraController;
import org.jlmallas.httpClient.NetClientService;
import org.jlmallas.secure.Secure;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarOfertaAcademica",
            pattern = "/editarOfertaAcademica/#{sessionOfertaAcademica.ofertaAcademica.id}",
            viewId = "/faces/pages/academico/periodosAcademicos/editarOfertaAcademica.xhtml"
    ),
    @URLMapping(
            id = "crearOfertaAcademica",
            pattern = "/crearOfertaAcademica/",
            viewId = "/faces/pages/academico/periodosAcademicos/editarOfertaAcademica.xhtml"
    )})
public class AdministrarOfertas implements Serializable {

    @Inject
    private UsuarioDM usuarioDM;
    @Inject
    private SessionOfertaAcademica sessionOfertaAcademica;
    @Inject
    private CabeceraController cabeceraController;
    @EJB
    private ConfiguracionDao configuracionDao;
    @EJB
    private UsuarioDao usuarioFacadeLocal;

    public AdministrarOfertas() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">   
    public String crear(Usuario usuario, PeriodoAcademico periodoAcademico) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_oferta_academica");
            if (tienePermiso == 1) {
                sessionOfertaAcademica.setOfertaAcademica(new OfertaAcademica());
                sessionOfertaAcademica.getOfertaAcademica().setIdSga("0");
                sessionOfertaAcademica.getOfertaAcademica().setPeriodoAcademicoId(periodoAcademico);
                sessionOfertaAcademica.setEsEditado(false);
                navegacion = "pretty:crearOfertaAcademica";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(OfertaAcademica ofertaAcademica, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuarioDM.getUsuario(), "editar_oferta_academica");
            if (tienePermiso == 1) {
                sessionOfertaAcademica.setOfertaAcademica(ofertaAcademica);
                sessionOfertaAcademica.setEsEditado(true);
                navegacion = "pretty:editarOfertaAcademica";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String agregar(OfertaAcademica ofertaAcademica, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            ofertaAcademica.setNombre(new String(ofertaAcademica.getNombre().getBytes()));
            if (ofertaAcademica.getIdSga() == null) {
                ofertaAcademica.setIdSga("");
            }
            if (sessionOfertaAcademica.isEsEditado() == false) {
                sessionOfertaAcademica.getOfertaAcademicas().add(ofertaAcademica);
                sessionOfertaAcademica.setOfertaAcademicasFilter(sessionOfertaAcademica.getOfertaAcademicas());
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            if (param.equalsIgnoreCase("agregar")) {
                navegacion = "pretty:editarPeriodoAcademico";
                sessionOfertaAcademica.setOfertaAcademica(new OfertaAcademica());
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public OfertaAcademica contieneOferta(List<OfertaAcademica> ofs, OfertaAcademica o) {
        OfertaAcademica oferta = null;
        for (OfertaAcademica of : ofs) {
            if (of.getIdSga().equalsIgnoreCase(o.getIdSga())) {
                oferta = of;
                break;
            }
        }
        return oferta;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_oferta_academica");
        if (tienePermiso == 1) {
            sessionOfertaAcademica.setRenderedCrear(true);
        } else {
            sessionOfertaAcademica.setRenderedCrear(false);
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_oferta_academica");
        if (tienePermiso == 1) {
            sessionOfertaAcademica.setRenderedEditar(true);
            sessionOfertaAcademica.setRenderedNoEditar(false);
        } else {
            sessionOfertaAcademica.setRenderedEditar(false);
            sessionOfertaAcademica.setRenderedNoEditar(true);
        }
    }

    public void renderedSincronizar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_oferta_academica");
        if (tienePermiso == 1) {
            sessionOfertaAcademica.setRenderedSincronizar(true);
        } else {
            sessionOfertaAcademica.setRenderedSincronizar(false);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES CONSUMES">   
    public void sgawsOfertasAcademicas(String periodoId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (usuarioFacadeLocal.tienePermiso(usuarioDM.getUsuario(), "sga_ws_oferta_academica") == 1) {
                String claveWS = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(),
                        configuracionDao.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String usuarioWs = configuracionDao.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String url = configuracionDao.buscar(new Configuracion(URLWSEnum.URLOFERTAACADEMICAWS.getTipo())).get(0).getValor() + "?id_periodo=" + periodoId;
                ConexionDTO seguridad = new ConexionDTO(claveWS, url, usuarioWs);
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement jsonElement = parser.parse(datosJson);
                    recorrerElementosJson(jsonElement);
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
            sessionOfertaAcademica.setOfertaAcademicaWS(new OfertaAcademica());
            JsonObject obj = elemento.getAsJsonObject();
            java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
            java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                sessionOfertaAcademica.setKey(entrada.getKey());
                try {
                    String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                    JsonParser jp = new JsonParser();
                    JsonElement jsonElement = jp.parse(e);
                    recorrerElementosJson(jsonElement);
                    sessionOfertaAcademica.setOfertaAcademicasFilter(sessionOfertaAcademica.getOfertaAcademicas());
                } catch (Exception e) {
                    recorrerElementosJson(entrada.getValue());
                }

            }
            return;
        }
        if (elemento.isJsonArray()) {
            JsonArray array = elemento.getAsJsonArray();
            sessionOfertaAcademica.setKeyEntero(0);
            sessionOfertaAcademica.setOfertaAcademicaWS(new OfertaAcademica());
            java.util.Iterator<JsonElement> iter = array.iterator();
            while (iter.hasNext()) {
                JsonElement entrada = iter.next();
                recorrerElementosJson(entrada);
            }
            return;
        }
        if (elemento.isJsonPrimitive()) {
            JsonPrimitive valor = elemento.getAsJsonPrimitive();
            if (sessionOfertaAcademica.getKeyEntero() == 0) {
                sessionOfertaAcademica.getOfertaAcademicaWS().setIdSga(new String(valor.getAsString().getBytes()));
                sessionOfertaAcademica.setKeyEntero(sessionOfertaAcademica.getKeyEntero() + 1);
                return;
            }
            if (sessionOfertaAcademica.getKeyEntero() == 1) {
                sessionOfertaAcademica.getOfertaAcademicaWS().setNombre(new String(valor.getAsString().getBytes()));
                sessionOfertaAcademica.setKeyEntero(sessionOfertaAcademica.getKeyEntero() + 1);
                return;
            }
            if (sessionOfertaAcademica.getKeyEntero() == 2) {
                sessionOfertaAcademica.getOfertaAcademicaWS().setFechaInicio(cabeceraController.getUtilService().parserFecha(new String(
                        valor.getAsString().getBytes()), "yyyy-MM-dd"));
                sessionOfertaAcademica.setKeyEntero(sessionOfertaAcademica.getKeyEntero() + 1);
                return;
            }
            if (sessionOfertaAcademica.getKeyEntero() == 3) {
                sessionOfertaAcademica.getOfertaAcademicaWS().setFechaFin(cabeceraController.getUtilService().parserFecha(new String(
                        valor.getAsString().getBytes()), "yyyy-MM-dd"));
                sessionOfertaAcademica.setKeyEntero(sessionOfertaAcademica.getKeyEntero() + 1);
            }
            sessionOfertaAcademica.setKeyEntero(sessionOfertaAcademica.getKeyEntero() + 1);
            if (sessionOfertaAcademica.getOfertaAcademicaWS().getIdSga() != null
                    && sessionOfertaAcademica.getOfertaAcademicaWS().getFechaInicio() != null
                    && sessionOfertaAcademica.getOfertaAcademicaWS().
                    getFechaFin() != null) {
                sessionOfertaAcademica.getOfertaAcademicas().add(sessionOfertaAcademica.getOfertaAcademicaWS());
            }
        }
    }

//</editor-fold>
}

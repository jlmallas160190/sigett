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
import com.jlmallas.api.date.DateResource;
import com.jlmallas.api.http.UrlConexion;
import com.jlmallas.api.http.dto.SeguridadHttp;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionOfertaAcademica;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.entity.PeriodoAcademico;
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
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

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
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionOfertaAcademica sessionOfertaAcademica;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    private List<OfertaAcademica> ofertaAcademicas = new ArrayList<OfertaAcademica>();

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
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_oferta_academica");
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
            if (usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_oferta_academica") == 1) {
                this.ofertaAcademicas = new ArrayList<>();

                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 15).getValor();
                String s = serviceUrl + "?id_periodo=" + periodoId;
                SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralFacadeLocal.find((int) 5).getValor(),
                        s, configuracionGeneralFacadeLocal.find((int) 6).getValor());
                UrlConexion conexion = new UrlConexion();
                String datosJson = conexion.conectar(seguridad);
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
                DateResource dateResource = new DateResource();
                sessionOfertaAcademica.getOfertaAcademicaWS().setFechaInicio(dateResource.DeStringADate(new String(
                        valor.getAsString().getBytes()), "yyyy-MM-dd"));
                sessionOfertaAcademica.setKeyEntero(sessionOfertaAcademica.getKeyEntero() + 1);
                return;
            }
            if (sessionOfertaAcademica.getKeyEntero() == 3) {
                DateResource dateResource = new DateResource();
                sessionOfertaAcademica.getOfertaAcademicaWS().setFechaFin(dateResource.DeStringADate(new String(
                        valor.getAsString().getBytes()), "yyyy-MM-dd"));
                sessionOfertaAcademica.setKeyEntero(sessionOfertaAcademica.getKeyEntero() + 1);
            }
            sessionOfertaAcademica.setKeyEntero(sessionOfertaAcademica.getKeyEntero() + 1);
            if (sessionOfertaAcademica.getOfertaAcademicaWS().getIdSga() != null
                    && sessionOfertaAcademica.getOfertaAcademicaWS().getFechaInicio() != null &&
                    sessionOfertaAcademica.getOfertaAcademicaWS().
                    getFechaFin() != null) {
                
                sessionOfertaAcademica.getOfertaAcademicas().add(sessionOfertaAcademica.getOfertaAcademicaWS());
                return;
            }
            return;
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET"> 
    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionOfertaAcademica getSessionOfertaAcademica() {
        return sessionOfertaAcademica;
    }

    public void setSessionOfertaAcademica(SessionOfertaAcademica sessionOfertaAcademica) {
        this.sessionOfertaAcademica = sessionOfertaAcademica;
    }

    public List<OfertaAcademica> getOfertaAcademicas() {
        return ofertaAcademicas;
    }

    public void setOfertaAcademicas(List<OfertaAcademica> ofertaAcademicas) {
        this.ofertaAcademicas = ofertaAcademicas;
    }
//</editor-fold>
}

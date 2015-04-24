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
import edu.unl.sigett.academico.managed.session.SessionOfertaAcademica;
import edu.unl.sigett.academico.managed.session.SessionPeriodoAcademico;
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
import edu.jlmallas.academico.service.OfertaAcademicaFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;
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
            id = "editarOfertaAcademica",
            pattern = "/editarOfertaAcademica/#{sessionOfertaAcademica.ofertaAcademica.id}",
            viewId = "/faces/pages/academico/editarOfertaAcademica.xhtml"
    ),
    @URLMapping(
            id = "crearOfertaAcademica",
            pattern = "/crearOfertaAcademica/",
            viewId = "/faces/pages/academico/editarOfertaAcademica.xhtml"
    )})
public class AdministrarOfertas implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionPeriodoAcademico sessionPeriodoAcademico;
    @Inject
    private SessionOfertaAcademica sessionOfertaAcademica;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    @EJB
    private OfertaAcademicaFacadeLocal ofertaAcademicaFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    private boolean esEditado = false;
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
                esEditado = false;
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
                esEditado = true;
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
            if (esEditado == false) {
                sessionPeriodoAcademico.getPeriodoAcademico().getOfertaAcademicaList().add(ofertaAcademica);
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
        if (usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_oferta_academica") == 1) {
            this.ofertaAcademicas = new ArrayList<>();
            try {
                Map parametros = new HashMap();
                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 15).getValor();
                String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
                String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
                String s = serviceUrl + "?id_periodo=" + periodoId;
                parametros.put("url", s);
                parametros.put("clave", passwordService);
                parametros.put("usuario", userService);
                parametros.put("msm_sincronizado", bundle.getString("lbl.sincronizado"));
                parametros.put("msm_no_sincronizado", bundle.getString("lbl.no_sincronizar_web_services"));
//                Map resultado = ofertaAcademicaConsumeService.getDatosOfertaAcademica(parametros);
//                ofertaAcademicas = (List<OfertaAcademica>) resultado.get("ofertas");
                for (OfertaAcademica oa : ofertaAcademicas) {
                    if (oa == null) {
                        oa = ofertaAcademicaFacadeLocal.buscarPorIdSga(oa.getIdSga());
                    }
                    if (oa == null) {
                        sessionPeriodoAcademico.getPeriodoAcademico().getOfertaAcademicaList().add(oa);
                        oa.setPeriodoAcademicoId(sessionPeriodoAcademico.getPeriodoAcademico());
                    }
                }

//                String s = serviceUrl + "?id_periodo=" + periodoId;
//                ConexionServicio conexionServicio = new ConexionServicio();
//                String datosJson = conexionServicio.conectar(s, userService, passwordService);
//                if (!datosJson.equalsIgnoreCase("")) {
//                    JsonParser parser = new JsonParser();
//                    JsonElement datos = parser.parse(conexionServicio.conectar(s, userService, passwordService));
//                    dumpJsonElement(datos);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
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

    public SessionPeriodoAcademico getSessionPeriodoAcademico() {
        return sessionPeriodoAcademico;
    }

    public void setSessionPeriodoAcademico(SessionPeriodoAcademico sessionPeriodoAcademico) {
        this.sessionPeriodoAcademico = sessionPeriodoAcademico;
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

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
    }
//</editor-fold>
}

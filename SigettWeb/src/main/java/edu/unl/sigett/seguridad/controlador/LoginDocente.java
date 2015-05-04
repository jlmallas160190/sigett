/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import com.jlmallas.comun.service.ConfiguracionFacadeLocal;
import edu.unl.sigett.finalizacion.controlador.AdministrarTribunales;
import edu.unl.sigett.postulacion.controlador.AdministrarDocentesProyecto;
import edu.unl.sigett.seguimiento.controlador.AdministrarActividades;
import edu.unl.sigett.seguimiento.controlador.AdministrarProyectosDirector;
import edu.unl.sigett.seguridad.managed.session.SessionDocenteUsuario;
import edu.unl.sigett.entity.DocenteUsuario;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import edu.unl.sigett.session.DocenteUsuarioFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.service.DocenteFacadeLocal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class LoginDocente implements Serializable {

    @Inject
    private SessionDocenteUsuario sessionDocenteUsuario;
    @Inject
    private AdministrarActividades administrarActividades;
    @Inject
    private AdministrarDocentesProyecto administrarDocentesProyecto;
    @Inject
    private AdministrarProyectosDirector administrarProyectosDirector;
    @Inject
    private AdministrarTribunales administrarTribunales;

    private boolean resultado;

    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private DocenteUsuarioFacadeLocal docenteUsuarioFacadeLocal;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    @EJB
    private DocenteFacadeLocal docenteFacadeLocal;
    @EJB
    private ConfiguracionFacadeLocal configuracionFacadeLocal;

    public LoginDocente() {
    }

    public String logout() {
        sessionDocenteUsuario.setDocenteUsuario(new DocenteUsuario());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "pretty:loginDocente";
    }

    public String logear(String username, String password) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String value = configuracionGeneralFacadeLocal.find(40).getValor();
        int esLogin = 3;
        if (value.equalsIgnoreCase("SI")) {
            sgaWebServicesValidaDocente(username, password);
            if (resultado) {
                esLogin = 1;
            }
        } else {
            int var = usuarioFacadeLocal.logear(username, configuracionFacadeLocal.encriptaClave(password));
            esLogin = var;
        }

        if (esLogin == 1) {
            Usuario usuario = usuarioFacadeLocal.buscarPorUsuario(username);
            if (usuario != null) {
                usuario.setUsername(username);
                usuario.setPassword(configuracionFacadeLocal.encriptaClave(password));
                usuarioFacadeLocal.edit(usuario);
                if (usuario.getEsActivo()) {
                    DocenteUsuario docenteUsuario = docenteUsuarioFacadeLocal.find(usuario.getId());
                    if (docenteUsuario != null) {
                        sessionDocenteUsuario.setDocenteUsuario(docenteUsuario);
                        sessionDocenteUsuario.setDocente(docenteFacadeLocal.find(docenteUsuario.getDocenteId()));
                        administrarActividades.notificacionActividadesPorRevisar(sessionDocenteUsuario.getDocente());
                        administrarDocentesProyecto.docenteProyectosParaPertinencia(sessionDocenteUsuario.getDocente());
                        administrarDocentesProyecto.setDocenteProyectosPorDocente(administrarDocentesProyecto.getDocentesProyectosParaPertinencia());
                        administrarProyectosDirector.notificacionesProyectos(sessionDocenteUsuario.getDocente());
                        administrarTribunales.buscarPorDocente(sessionDocenteUsuario.getDocenteUsuario().getDocenteId(), "");
                        navegacion = "pretty:principalDocente";
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_user") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_user_no_active") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } else {
            if (esLogin == 3) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.datos_incorrectos"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                if (esLogin == 0) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.clave_incorrecta"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    if (esLogin == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario_incorrecto"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        }
        return navegacion;
    }

    public void sgaWebServicesValidaDocente(String cedula, String clave) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            Map parametros = new HashMap();
            String serviceUrl = configuracionGeneralFacadeLocal.find((int) 39).getValor();
            String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
            String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
            String s = serviceUrl + "?cedula=" + cedula + ";clave=" + clave;
            parametros.put("url", s);
            parametros.put("clave", passwordService);
            parametros.put("usuario", userService);
            parametros.put("msm_sincronizado", bundle.getString("lbl.sincronizado"));
            parametros.put("msm_no_sincronizado", bundle.getString("lbl.no_sincronizar_web_services"));
//            resultado = docenteConsumeService.validarDocente(parametros);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public SessionDocenteUsuario getSessionDocenteUsuario() {
        return sessionDocenteUsuario;
    }

    public void setSessionDocenteUsuario(SessionDocenteUsuario sessionDocenteUsuario) {
        this.sessionDocenteUsuario = sessionDocenteUsuario;
    }

}

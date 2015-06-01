/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import com.jlmallas.comun.dao.ConfiguracionDao;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.util.CabeceraController;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.jlmallas.secure.SecureDTO;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class LoginAdmin implements Serializable {
    
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private UsuarioService usuarioService;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject CabeceraController cabeceraController;
    
    public LoginAdmin() {
    }
    
    public String logout() {
        sessionUsuario.setUsuario(new Usuario());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "pretty:loginAdmin";
    }
    
    public String logear(String username, String password) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        int var = usuarioService.logear(username,cabeceraController.getSecureService().encrypt(
                                    new SecureDTO(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(),password)));
        if (var == 1) {
            sessionUsuario.setUsuario(usuarioFacadeLocal.buscarPorUsuario(username));
            if (sessionUsuario.getUsuario().getEsActivo()) {
                navegacion = "pretty:principalAdmin";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_user_no_active") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            if (var == 3) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_session_activa") + "", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                if (var == 0) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.clave_incorrecta") + "", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    if (var == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario_incorrecto") + "", "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        }
        return navegacion;
    }
    
    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }
    
    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }
    
}

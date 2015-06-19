/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.usuario;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
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
import org.jlmallas.secure.Secure;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "login",
            pattern = "/login/",
            viewId = "/faces/login.xhtml"
    ),
    @URLMapping(
            id = "inicio",
            pattern = "/inicio/",
            viewId = "/faces/pages/inicio/principal.xhtml"
    )
})
public class LoginController implements Serializable {

    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private UsuarioService usuarioService;
    @Inject
    private UsuarioDM sessionUsuario;
    @Inject
    CabeceraController cabeceraController;

    public LoginController() {
    }

    public String logout() {
        sessionUsuario.setUsuario(new Usuario());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "pretty:login";
    }

    public String logear(String username, String password) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        int var = usuarioService.logear(username, cabeceraController.getSecureService().encrypt(
                new Secure(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(), password)));
        if (var == 1) {
            sessionUsuario.setUsuario(usuarioFacadeLocal.buscarPorUsuario(username));
            if (sessionUsuario.getUsuario().getEsActivo()) {
                return "pretty:inicio";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_user_no_active") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            return "";
        }
        if (var == 3) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_session_activa") + "", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }
        if (var == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.clave_incorrecta") + "", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }
        if (var == 2) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario_incorrecto") + "", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }
        return "";
    }

    public UsuarioDM getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(UsuarioDM sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.usuario;

import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.util.CabeceraController;
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
import org.jlmallas.secure.SecureDTO;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class Login implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private UsuarioService usuarioService;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionUsuario sessionUsuario;
    //</editor-fold>

    public Login() {
    }

    public String logout() {
        sessionUsuario.setUsuario(new Usuario());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "pretty:login";
    }

    public String getRol(Usuario usuario) {
        String rol = "";
        if (usuario != null) {
            if (usuario.getId() != null) {
                if (usuario.getEsSuperuser()) {
                    rol = "SuperUsuario";
                } else {
                    if (!usuario.getRolUsuarioList().isEmpty()) {
                        rol = usuario.getRolUsuarioList().get(0).getRolId().getNombre();
                    }
                }
            }
        }
        return rol;

    }

    public String logear(String username, String password) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        int var = usuarioService.logear(username, cabeceraController.getSecureService().encrypt(
                new SecureDTO(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(), password)));
        if (var == 1) {
            if (sessionUsuario.getUsuario().getId() == null) {
                List<Usuario> usuarios = usuarioService.buscar(new Usuario(null, null, username, null, null, null, Boolean.TRUE, Boolean.FALSE));
                sessionUsuario.setUsuario(!usuarios.isEmpty() ? usuarios.get(0) : null);
                if (sessionUsuario.getUsuario().getEsActivo()) {
                    navegacion = "pretty:principal";
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_user_no_active") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_session_activa") + "", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            if (var == 3) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.datos_incorrectos") + "", "");
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

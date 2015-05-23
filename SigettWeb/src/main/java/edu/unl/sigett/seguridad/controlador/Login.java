/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import com.jlmallas.comun.dao.ConfiguracionDao;
import edu.unl.sigett.comun.controlador.AdministrarConfiguraciones;
import edu.unl.sigett.postulacion.controlador.AdministrarDocentesProyecto;
import edu.unl.sigett.proyecto.AdministrarProyectos;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import org.jlmallas.seguridad.entity.RolUsuario;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class Login implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private AdministrarDocentesProyecto administrarDocentesProyecto;
    @Inject
    private AdministrarProyectos administrarProyectos;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ConfiguracionDao configuracionFacadeLocal;

    private int intervalo;
    private boolean viewNotificacionesSecretarioAbogado;

    public Login() {
        this.viewNotificacionesSecretarioAbogado = false;
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
        int var = usuarioFacadeLocal.logear(username, configuracionFacadeLocal.encriptaClave(password));
        if (var == 1) {
            if (sessionUsuario.getUsuario().getId() == null) {
                sessionUsuario.setUsuario(usuarioFacadeLocal.buscarPorUsuario(username));
                if (sessionUsuario.getUsuario().getEsActivo()) {
                    for (RolUsuario rolUsuario : sessionUsuario.getUsuario().getRolUsuarioList()) {
                        if (rolUsuario.getRolId().getId() == 6) {
                            viewNotificacionesSecretarioAbogado = true;
                            break;
                        }
                    }
                    administrarDocentesProyecto.docenteProyectosSinPertinencia(sessionUsuario.getUsuario());
                    administrarProyectos.buscarProyectosInicio(sessionUsuario.getUsuario());
                    administrarProyectos.buscarProyectosAdjudicaciónDirector(sessionUsuario.getUsuario());
                    administrarProyectos.buscarProyectosPorCulminar(sessionUsuario.getUsuario());
                    administrarProyectos.buscarProyectosCaducados(sessionUsuario.getUsuario());
                    administrarProyectos.buscarProyectosEnSustentacionPrivada(sessionUsuario.getUsuario());
                    administrarProyectos.buscarProyectosEnSustentacionPublica(sessionUsuario.getUsuario());
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

    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public boolean isViewNotificacionesSecretarioAbogado() {
        return viewNotificacionesSecretarioAbogado;
    }

    public void setViewNotificacionesSecretarioAbogado(boolean viewNotificacionesSecretarioAbogado) {
        this.viewNotificacionesSecretarioAbogado = viewNotificacionesSecretarioAbogado;
    }

}

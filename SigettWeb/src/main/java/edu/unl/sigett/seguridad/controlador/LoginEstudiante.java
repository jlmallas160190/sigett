/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.dao.ConfiguracionDao;
import edu.unl.sigett.seguimiento.controlador.AdministrarActividades;
import edu.unl.sigett.seguridad.managed.session.SessionEstudianteUsuario;
import edu.unl.sigett.entity.EstudianteUsuario;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.EstudianteUsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.dao.EstudianteDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class LoginEstudiante implements Serializable {

    @Inject
    private SessionEstudianteUsuario sessionEstudianteUsuario;
    @Inject
    private AdministrarActividades administrarActividades;

    private boolean resultado;

    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private EstudianteUsuarioDao estudianteUsuarioFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private EstudianteDao estudianteFacadeLocal;
    @EJB
    private ConfiguracionDao configuracionFacadeLocal;

    public LoginEstudiante() {
    }

    public String logout() {
        sessionEstudianteUsuario.setEstudianteUsuario(new EstudianteUsuario());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "pretty:loginEstudiante";
    }

    public String logear(String username, String password) {
        String navegacion = "";
//        int esLogin = 3;
//        String value = configuracionGeneralFacadeLocal.find(40).getValor();
//        if (value.equalsIgnoreCase("SI")) {
//            sgaWebServicesValidaEstudiante(username, password);
//            if (resultado) {
//                esLogin = 1;
//            }
//        } else {
//            int var = usuarioFacadeLocal.logear(username, configuracionFacadeLocal.encriptaClave(password));
//            esLogin = var;
//        }
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        if (esLogin == 1) {
//            Usuario usuario = usuarioFacadeLocal.buscarPorUsuario(username);
//            if (usuario != null) {
//                usuario.setUsername(username);
//                usuario.setPassword(configuracionFacadeLocal.encriptaClave(password));
//                usuarioFacadeLocal.edit(usuario);
//                if (usuario.getEsActivo()) {
//                    EstudianteUsuario estudianteUsuario = estudianteUsuarioFacadeLocal.find(usuario.getId());
//                    if (estudianteUsuario != null) {
//                        sessionEstudianteUsuario.setEstudianteUsuario(estudianteUsuario);
//                        sessionEstudianteUsuario.setEstudiante(estudianteFacadeLocal.find(estudianteUsuario.getEstudianteId()));
//                        administrarActividades.notificacionActividadesAutorProyecto(sessionEstudianteUsuario.getEstudiante());
//                        navegacion = "pretty:principalEstudiante";
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_user") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_user_no_active") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
//        } else {
//            if (esLogin == 3) {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.datos_incorrectos"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            } else {
//                if (esLogin == 0) {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.clave_incorrecta"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    if (esLogin == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario_incorrecto"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            }
//        }
        return navegacion;
    }

    public void sgaWebServicesValidaEstudiante(String cedula, String clave) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        ConexionServicio conexionServicio = new ConexionServicio();
//        String serviceUrl = configuracionGeneralFacadeLocal.find((int) 38).getValor();
//        String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//        String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//        this.resultado = false;
//        try {
//            String s = serviceUrl + "?cedula=" + cedula + ";clave=" + clave;
//            String datosJson = conexionServicio.conectar(s, userService, passwordService);
//            if (!datosJson.equalsIgnoreCase("")) {
//                JsonParser parser = new JsonParser();
//                JsonElement datos = parser.parse(datosJson);
//                recorrerElementosJson(datos);
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//
//        } catch (Exception e) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
    }

    private void recorrerElementosJson(JsonElement elemento) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        recorrerElementosJson(jsonElement);

                    } catch (Exception e) {
                        recorrerElementosJson(entrada.getValue());
                    }

                }

            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    recorrerElementosJson(entrada);
                }
            } else if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (valor.isBoolean()) {
                    resultado = valor.getAsBoolean();
                }
            }

        } catch (Exception e) {
            resultado = false;
        }
    }

    public SessionEstudianteUsuario getSessionEstudianteUsuario() {
        return sessionEstudianteUsuario;
    }

    public void setSessionEstudianteUsuario(SessionEstudianteUsuario sessionEstudianteUsuario) {
        this.sessionEstudianteUsuario = sessionEstudianteUsuario;
    }

}

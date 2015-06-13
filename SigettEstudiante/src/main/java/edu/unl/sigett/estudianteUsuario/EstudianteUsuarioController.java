/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.estudianteUsuario;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.URLWSEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Estudiante;
import edu.jlmallas.academico.service.EstudianteService;
import edu.unl.sigett.entity.EstudianteUsuario;
import edu.unl.sigett.service.EstudianteUsuarioService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.jlmallas.httpClient.ConexionDTO;
import org.jlmallas.httpClient.NetClientService;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.secure.SecureDTO;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "loginEstudiante",
            pattern = "/login",
            viewId = "/faces/pages/seguridad/login.xhtml"
    ),
    @URLMapping(
            id = "inicio",
            pattern = "/inicio",
            viewId = "/faces/pages/inicio/index.xhtml"
    )
})
public class EstudianteUsuarioController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private EstudianteUsuarioService estudianteUsuarioService;
    @EJB
    private EstudianteService estudianteService;
    @EJB
    private PersonaService personaService;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionEstudianteUsuario sessionEstudianteUsuario;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(EstudianteUsuarioController.class.getName());

    public EstudianteUsuarioController() {
    }

    public String logout() {
        sessionEstudianteUsuario.setEstudianteUsuarioDTO(new EstudianteUsuarioDTO());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "pretty:login";
    }

    public String logear() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String value = configuracionService.buscar(new Configuracion(ConfiguracionEnum.LOGINESTUDIANTEWS.getTipo())).get(0).getValor();
        if (value.equals(ValorEnum.NO.getTipo())) {
            int var = usuarioService.logear(sessionEstudianteUsuario.getEstudianteUsuarioDTO().getUsuario().getUsername(),
                    cabeceraController.getSecureService().encrypt(new SecureDTO(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                                    sessionEstudianteUsuario.getEstudianteUsuarioDTO().getUsuario().getPassword())));
            if (var == 0) {
                this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.clave_incorrecta") + "", "");
            }
            if (var == 1) {
                List<Usuario> usuarios = usuarioService.buscar(new Usuario(null, null, sessionEstudianteUsuario.getEstudianteUsuarioDTO().getUsuario().
                        getUsername(), null, null, null, Boolean.TRUE, Boolean.FALSE));
                if (usuarios == null) {
                    return "";
                }
                EstudianteUsuario estudianteUsuario = estudianteUsuarioService.buscarPorId(!usuarios.isEmpty() ? usuarios.get(0).getId() : null);
                if (estudianteUsuario == null) {
                    this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.datos_incorrectos"), "");
                    return "";
                }
                sessionEstudianteUsuario.setEstudianteUsuarioDTO(new EstudianteUsuarioDTO(!usuarios.isEmpty() ? usuarios.get(0) : null,
                        estudianteService.buscarPorId(new Estudiante(estudianteUsuario.getEstudianteId())),
                        personaService.buscarPorId(new Persona(estudianteUsuario.getEstudianteId())),new Rol(null,
                                cabeceraController.getValueFromProperties(PropertiesFileEnum.ETIQUETASES, "estudiante"))));
                return "pretty:inicio";
            }
            if (var == 2) {
                this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario_incorrecto"), "");
                return "";
            }
            if (var == 3) {
                this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.datos_incorrectos"), "");
                return "";
            }
            return "";
        }
        sgaWebServicesValidaEstudiante(sessionEstudianteUsuario.getEstudianteUsuarioDTO().getUsuario().
                getUsername(), sessionEstudianteUsuario.getEstudianteUsuarioDTO().getUsuario().getPassword());
        if (!sessionEstudianteUsuario.getValidarEstudianteWS()) {
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario_incorrecto"), "");
            return "";
        }
        return "pretty:inicio";
    }
//<editor-fold defaultstate="collapsed" desc="SERVICIOS WEB">

    public void sgaWebServicesValidaEstudiante(String cedula, String clave) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            sessionEstudianteUsuario.setValidarEstudianteWS(Boolean.FALSE);
            String passwordService = this.cabeceraController.getSecureService().decrypt(new SecureDTO(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                    configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
            String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
            String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.VALIDARESTUDIANTE.getTipo())).get(0).getValor();
            String s = serviceUrl + "?cedula=" + cedula + ";clave=" + passwordService;
            ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
            NetClientService conexion = new NetClientServiceImplement();
            String datosJson = conexion.response(seguridad);
            if (!datosJson.equalsIgnoreCase("")) {
                JsonParser parser = new JsonParser();
                JsonElement datos = parser.parse(datosJson);
                parserValidaEstudianteJson(datos);
            }
        } catch (Exception e) {
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
        }
    }

    /**
     * VALIDAR DATOS DE USUARIOD DE DOCENTE
     *
     * @param elemento
     * @throws Exception
     */
    private void parserValidaEstudianteJson(JsonElement elemento) throws Exception {
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
                        parserValidaEstudianteJson(jsonElement);

                    } catch (Exception e) {
                        parserValidaEstudianteJson(elemento);
                    }
                }
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserValidaEstudianteJson(entrada);
                }
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                sessionEstudianteUsuario.setValidarEstudianteWS(valor.getAsBoolean());
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }
//</editor-fold>

}

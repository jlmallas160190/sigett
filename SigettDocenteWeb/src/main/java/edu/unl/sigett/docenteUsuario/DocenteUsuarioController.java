/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteUsuario;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.unl.sigett.entity.DocenteUsuario;
import com.jlmallas.comun.enumeration.URLWSEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import edu.unl.sigett.service.DocenteUsuarioService;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.httpClient.ConexionDTO;
import org.jlmallas.httpClient.NetClientService;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.secure.Secure;
import org.jlmallas.seguridad.entity.Usuario;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author jorge-luis
 */
@Named(value = "docenteUsuarioController")
@SessionScoped

@URLMappings(mappings = {
    @URLMapping(
            id = "loginDocente",
            pattern = "/login",
            viewId = "/faces/login.xhtml"
    ),
    @URLMapping(
            id = "inicioDocente",
            pattern = "/inicio",
            viewId = "/faces/pages/inicio/index.xhtml"
    )
})
public class DocenteUsuarioController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    @Inject
    private CabeceraController cabeceraController;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private DocenteUsuarioService docenteUsuarioDao;
    @EJB
    private DocenteDao docenteDao;
    @EJB
    private PersonaDao personaDao;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DocenteUsuarioController.class.getName());

    public DocenteUsuarioController() {
    }

    /**
     * PERMITE LOGEAR A DOCENTE
     *
     * @return
     */
    public String logear() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String value = configuracionService.buscar(new Configuracion(ConfiguracionEnum.LOGINDOCENTEWS.getTipo())).get(0).getValor();
        if (value.equals(ValorEnum.NO.getTipo())) {
            int var = usuarioService.logear(docenteUsuarioDM.getDocenteUsuarioDTO().getUsuario().getUsername(),
                    cabeceraController.getSecureService().encrypt(
                            new Secure(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(), docenteUsuarioDM.getDocenteUsuarioDTO().getUsuario().getPassword())));
            if (var == 0) {
                this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.clave_incorrecta") + "", "");
            }
            if (var == 1) {
                List<Usuario> usuarios = usuarioService.buscar(new Usuario(null, null, docenteUsuarioDM.getDocenteUsuarioDTO().getUsuario().
                        getUsername(), null, null, null, Boolean.TRUE, Boolean.FALSE));
                if (usuarios == null) {
                    return "";
                }
                DocenteUsuario docenteUsuario = docenteUsuarioDao.buscarPorId(!usuarios.isEmpty() ? usuarios.get(0).getId() : null);
                if (docenteUsuario == null) {
                    this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.datos_incorrectos"), "");
                    return "";
                }
                docenteUsuarioDM.setDocenteUsuarioDTO(new DocenteUsuarioDTO(docenteDao.find(docenteUsuario.getDocenteId()),
                        !usuarios.isEmpty() ? usuarios.get(0) : null, personaDao.find(docenteUsuario.getDocenteId())));
                return "pretty:inicioDocente";
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
        sgaWebServicesValidaDocente(docenteUsuarioDM.getDocenteUsuarioDTO().getUsuario().
                getUsername(), docenteUsuarioDM.getDocenteUsuarioDTO().getUsuario().getPassword());
        if (!docenteUsuarioDM.getValidarDocenteWS()) {
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.usuario_incorrecto"), "");
            return "";
        }
        return "pretty:inicioDocente";
    }
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS WEB">

    public void sgaWebServicesValidaDocente(String cedula, String clave) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            docenteUsuarioDM.setValidarDocenteWS(Boolean.FALSE);
            String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralDTO().getSecureKey(),
                    configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
            String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
            String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.VALIDARDOCENTE.getTipo())).get(0).getValor();
            String s = serviceUrl + "?cedula=" + cedula + ";clave=" + passwordService;
            ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
            NetClientService conexion = new NetClientServiceImplement();
            String datosJson = conexion.response(seguridad);
            if (!datosJson.equalsIgnoreCase("")) {
                JsonParser parser = new JsonParser();
                JsonElement datos = parser.parse(datosJson);
                parserValidaDocenteJson(datos);
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
    private void parserValidaDocenteJson(JsonElement elemento) throws Exception {
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
                        parserValidaDocenteJson(jsonElement);

                    } catch (Exception e) {
                        parserValidaDocenteJson(elemento);
                    }
                }
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserValidaDocenteJson(entrada);
                }
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                docenteUsuarioDM.setValidarDocenteWS(valor.getAsBoolean());
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }
//</editor-fold>

    public String logout() {
        docenteUsuarioDM.setDocenteUsuarioDTO(new DocenteUsuarioDTO());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "pretty:loginDocente";
    }
}

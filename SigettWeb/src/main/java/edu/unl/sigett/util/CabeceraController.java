/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.ServidorCorreoEnum;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.webSemantica.service.implement.AreaAcademicaOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.AutorOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.AutorProyectoOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.CarreraOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.LineaInvestigacionOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.LineaInvestigacionProyectoOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.NivelAcademicoOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.PeriodoAcademicoOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.ProyectoCarreraOfertaOntServiceImplement;
import edu.unl.sigett.webSemantica.service.implement.ProyectoOntServiceImplement;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import edu.unl.sigett.webSemantica.vocabulay.Vocabulario;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.jlmallas.api.email.MailServiceImplement;
import org.jlmallas.api.email.MailDTO;
import org.jlmallas.api.email.MailService;
import org.jlmallas.api.secure.SecureDTO;
import org.jlmallas.api.secure.SecureService;
import org.jlmallas.api.secure.SecureServiceImplement;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author jorge-luis
 */
@Named(value = "cabeceraController")
@SessionScoped
public class CabeceraController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionUsuario sessionUsuario;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">

    @EJB
    private ConfiguracionDao configuracionDao;
    @EJB
    private UsuarioService usuarioService;
//</editor-fold>
    private MessageView messageView;
    private MailService mailService;
    private MailDTO mailDTO;
    private ConfiguracionGeneralDTO configuracionGeneralDTO;
    private SecureService secureService;
    private CabeceraWebSemantica cabeceraWebSemantica;
    private OntologyService ontologyService;
    private PermisoAdministrarProyecto permisoAdministrarProyecto;
    private static final Logger LOG = Logger.getLogger(CabeceraController.class.getName());

    public CabeceraController() {
    }

    public void init() {
        inicarOntologias();
        this.fijarParametrosWebSemantica();
        this.fijarParametrosMail();
        this.fijarConfiguraciones();
        this.mailService = new MailServiceImplement();
        this.messageView = new MessageView();
    }

    public void initLogin() {
        this.configuracionGeneralDTO = new ConfiguracionGeneralDTO();
        this.secureService = new SecureServiceImplement();
        this.fijarSecretKey();
    }

    private void fijarParametrosMail() {
        String puerto = configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.PUERTO.getTipo())).get(0).getValor();
        String smtp = configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.SMTP.getTipo())).get(0).getValor();
        String usuario = configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.USUARIO.getTipo())).get(0).getValor();
        String clave = this.secureService.encrypt(new SecureDTO(this.getConfiguracionGeneralDTO().getSecureKey(),
                configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.USUARIO.getTipo())).get(0).getValor()));
        mailDTO = new MailDTO(smtp, puerto, usuario, clave, null, null, null, null);

    }

    private void fijarConfiguraciones() {
        configuracionGeneralDTO.setTiempoMaximoPertinencia(configuracionDao.buscar(
                new Configuracion(ConfiguracionEnum.TIEMPOPERTINENCIA.getTipo())).get(0).getValor());
    }

    private void fijarSecretKey() {
        BufferedReader br = null;
        String secretKey = "";
        try {
            String pathSecretKey = configuracionDao.buscar(new Configuracion(ConfiguracionEnum.SECRETKEY.getTipo())).get(0).getValor();
            String sCurrentLine;
            br = new BufferedReader(new FileReader(pathSecretKey));
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                count++;
                if (count == 1) {
                    StringTokenizer st = new StringTokenizer(sCurrentLine, "='");
                    String value = "";
                    while (st.hasMoreElements()) {
                        value = st.nextToken();
                    }
                    secretKey = value;
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        this.configuracionGeneralDTO.setSecureKey(secretKey);
    }

    private void fijarParametrosWebSemantica() {
        try {
            String ruta = configuracionDao.buscar(new Configuracion(ConfiguracionEnum.RUTARDF.getTipo())).get(0).getValor();
            if (ruta == null) {
                return;
            }
            this.cabeceraWebSemantica = new CabeceraWebSemantica(new Vocabulario(), ruta);
        } catch (FileNotFoundException e) {
        }
    }

    private void inicarOntologias() {
        this.ontologyService = new OntologyService();
        this.ontologyService.setAutorOntService(new AutorOntServiceImplement());
        this.ontologyService.setAutorProyectoOntService(new AutorProyectoOntServiceImplement());
        this.ontologyService.setProyectoOntService(new ProyectoOntServiceImplement());
        this.ontologyService.setLineaInvestigacionOntService(new LineaInvestigacionOntServiceImplement());
        this.ontologyService.setLineaInvestigacionProyectoOntService(new LineaInvestigacionProyectoOntServiceImplement());
        this.ontologyService.setCarreraOntService(new CarreraOntServiceImplement());
        this.ontologyService.setAreaAcademicaOntService(new AreaAcademicaOntServiceImplement());
        this.ontologyService.setPeriodoAcademicoOntService(new PeriodoAcademicoOntServiceImplement());
        this.ontologyService.setNivelAcademicoOntService(new NivelAcademicoOntServiceImplement());
        this.ontologyService.setProyectoCarreraOfertaOntService(new ProyectoCarreraOfertaOntServiceImplement());
    }

    public String getValueFromProperties(final PropertiesFileEnum file,
            final String propiedad) {

        Properties propiedades = new Properties();
        try {
            InputStream is = this.getClass().getClassLoader()
                    .getResourceAsStream(file.getArchivo());
            propiedades.load(is);
            return propiedades.getProperty(propiedad);

        } catch (IOException ioe) {
            LOG.info(ioe.getMessage());
        }
        return null;
    }

    public MessageView getMessageView() {
        return messageView;
    }

    public void setMessageView(MessageView messageView) {
        this.messageView = messageView;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public MailDTO getMailDTO() {
        return mailDTO;
    }

    public void setMailDTO(MailDTO mailDTO) {
        this.mailDTO = mailDTO;
    }

    public ConfiguracionGeneralDTO getConfiguracionGeneralDTO() {
        return configuracionGeneralDTO;
    }

    public void setConfiguracionGeneralDTO(ConfiguracionGeneralDTO configuracionGeneralDTO) {
        this.configuracionGeneralDTO = configuracionGeneralDTO;
    }

    public SecureService getSecureService() {
        return secureService;
    }

    public void setSecureService(SecureService secureService) {
        this.secureService = secureService;
    }

    public CabeceraWebSemantica getCabeceraWebSemantica() {
        return cabeceraWebSemantica;
    }

    public void setCabeceraWebSemantica(CabeceraWebSemantica cabeceraWebSemantica) {
        this.cabeceraWebSemantica = cabeceraWebSemantica;
    }

    public OntologyService getOntologyService() {
        return ontologyService;
    }

    public void setOntologyService(OntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    public PermisoAdministrarProyecto getPermisoAdministrarProyecto() {
        return permisoAdministrarProyecto;
    }

    public void setPermisoAdministrarProyecto(PermisoAdministrarProyecto permisoAdministrarProyecto) {
        this.permisoAdministrarProyecto = permisoAdministrarProyecto;
    }

}

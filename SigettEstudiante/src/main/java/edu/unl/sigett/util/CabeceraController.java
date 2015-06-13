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
import java.io.BufferedReader;
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
import org.jlmallas.email.MailServiceImplement;
import org.jlmallas.email.MailDTO;
import org.jlmallas.email.MailService;
import org.jlmallas.secure.SecureDTO;
import org.jlmallas.secure.SecureService;
import org.jlmallas.secure.SecureServiceImplement;
import org.jlmallas.util.service.UtilService;
import org.jlmallas.util.service.UtilServiceImplement;

/**
 *
 * @author jorge-luis
 */
@Named(value = "cabeceraController")
@SessionScoped
public class CabeceraController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
//    @Inject
//    private SessionUsuario sessionUsuario;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ConfiguracionDao configuracionDao;
//</editor-fold>
    private MessageView messageView;
    private MailService mailService;
    private MailDTO mailDTO;
    private ConfiguracionGeneralUtil configuracionGeneralUtil;
    private SecureService secureService;
    private UtilService utilService;
    private static final Logger LOG = Logger.getLogger(CabeceraController.class.getName());

    public CabeceraController() {
    }

    /**
     * SE INICIA EN PÁGINA PRINCIPAL
     */
    public void preRenderView() {
        this.fijarParametrosMail();
        this.fijarConfiguraciones();
    }

    /**
     * SE INICIA EN EL LOGIN
     */
    public void preRenderViewInit() {
        this.configuracionGeneralUtil = new ConfiguracionGeneralUtil();
        this.secureService = new SecureServiceImplement();
        this.mailService = new MailServiceImplement();
        this.messageView = new MessageView();
        this.utilService = new UtilServiceImplement();
        this.fijarSecretKey();
    }

    private void fijarParametrosMail() {
        String puerto = configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.PUERTO.getTipo())).get(0).getValor();
        String smtp = configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.SMTP.getTipo())).get(0).getValor();
        String usuario = configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.USUARIO.getTipo())).get(0).getValor();
        String clave = this.secureService.decrypt(new SecureDTO(this.getConfiguracionGeneralUtil().getSecureKey(),
                configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.CLAVE.getTipo())).get(0).getValor()));
        mailDTO = new MailDTO(smtp, puerto, usuario, clave, null, null, null, null);
    }

    private void fijarConfiguraciones() {
        configuracionGeneralUtil.setTamanioArchivo(Double.parseDouble(configuracionDao.buscar(
                new Configuracion(ConfiguracionEnum.TAMANIOARCHIVO.getTipo())).get(0).getValor()));
    }

    private void fijarSecretKey() {
        @SuppressWarnings("UnusedAssignment")
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
        this.configuracionGeneralUtil.setSecureKey(secretKey);
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

    public ConfiguracionDao getConfiguracionDao() {
        return configuracionDao;
    }

    public void setConfiguracionDao(ConfiguracionDao configuracionDao) {
        this.configuracionDao = configuracionDao;
    }

    public ConfiguracionGeneralUtil getConfiguracionGeneralUtil() {
        return configuracionGeneralUtil;
    }

    public void setConfiguracionGeneralUtil(ConfiguracionGeneralUtil configuracionGeneralUtil) {
        this.configuracionGeneralUtil = configuracionGeneralUtil;
    }

    public SecureService getSecureService() {
        return secureService;
    }

    public void setSecureService(SecureService secureService) {
        this.secureService = secureService;
    }

    public UtilService getUtilService() {
        return utilService;
    }

    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

}

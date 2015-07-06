/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.service.ConfiguracionService;
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

    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;
//</editor-fold>
    private MessageView messageView;
    private MailService mailService;
    private MailDTO mailDTO;
    private ConfiguracionGeneralDTO configuracionGeneralDTO;
    private SecureService secureService;
    private UtilService utilService;
    private static final Logger LOG = Logger.getLogger(CabeceraController.class.getName());

    public CabeceraController() {
    }

    public void preRenderView() {
        this.configuracionGeneralDTO = new ConfiguracionGeneralDTO();
        this.secureService = new SecureServiceImplement();
        this.mailService = new MailServiceImplement();
        this.messageView = new MessageView();
        this.utilService = new UtilServiceImplement();
        this.fijarSecretKey();
    }

    private void fijarSecretKey() {
        BufferedReader br = null;
        String secretKey = "";
        try {
            String pathSecretKey = configuracionService.buscar(new Configuracion(ConfiguracionEnum.SECRETKEY.getTipo())).get(0).getValor();
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

    public UtilService getUtilService() {
        return utilService;
    }

    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

}

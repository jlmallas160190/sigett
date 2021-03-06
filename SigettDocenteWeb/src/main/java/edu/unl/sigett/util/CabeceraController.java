/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import com.jlmallas.comun.dao.ConfiguracionDao;
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
    private SecureService secureService;
    private UtilService utilService;
    private ConfiguracionGeneralUtil configuracionGeneralUtil;
    private static final Logger LOG = Logger.getLogger(CabeceraController.class.getName());

    public CabeceraController() {
    }

    public void preRenderView() {
        this.configuracionGeneralUtil = new ConfiguracionGeneralUtil();
        this.secureService = new SecureServiceImplement();
        this.utilService = new UtilServiceImplement();
        this.messageView = new MessageView();
        fijarSecretKey();
        fijarConfiguraciones();
    }

    private void fijarSecretKey() {
        @SuppressWarnings("UnusedAssignment")
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
        this.configuracionGeneralUtil.setSecureKey(secretKey);
    }

    private void fijarConfiguraciones() {
        configuracionGeneralUtil.setTamanioArchivo(Double.parseDouble(configuracionService.buscar(
                new Configuracion(ConfiguracionEnum.TAMANIOARCHIVO.getTipo())).get(0).getValor()));
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

    public ConfiguracionGeneralUtil getConfiguracionGeneralUtil() {
        return configuracionGeneralUtil;
    }

    public void setConfiguracionGeneralUtil(ConfiguracionGeneralUtil configuracionGeneralUtil) {
        this.configuracionGeneralUtil = configuracionGeneralUtil;
    }

}

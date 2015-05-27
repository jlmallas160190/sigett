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
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import org.jlmallas.api.email.MailServiceImplement;
import org.jlmallas.api.email.MailDTO;
import org.jlmallas.api.email.MailService;
import org.jlmallas.api.secure.SecureDTO;
import org.jlmallas.api.secure.SecureService;
import org.jlmallas.api.secure.SecureServiceImplement;

/**
 *
 * @author jorge-luis
 */
@Named(value = "cabeceraController")
@SessionScoped
public class CabeceraController implements Serializable {
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">

    @EJB
    private ConfiguracionDao configuracionDao;
//</editor-fold>
    private MessageView messageView;
    private MailService mailService;
    private MailDTO mailDTO;
    private ConfiguracionGeneralDTO configuracionGeneralDTO;
    private SecureService secureService;

    public CabeceraController() {
    }

    public void init() {
        this.fijarParametrosMail();
        this.fijarConfiguraciones();
        this.mailService = new MailServiceImplement();
        this.messageView = new MessageView();
    }

    public void initLogin() {
        this.configuracionGeneralDTO=new ConfiguracionGeneralDTO();
        this.secureService = new SecureServiceImplement();
        this.fijarSecretKey();
    }

    private void fijarParametrosMail() {
        String puerto = configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.PUERTO.getTipo())).get(0).getValor();
        String smtp = configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.SMTP.getTipo())).get(0).getValor();
        String usuario =configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.USUARIO.getTipo())).get(0).getValor();
        String clave =this.secureService.encrypt(new SecureDTO(this.getConfiguracionGeneralDTO().getSecureKey(),
                configuracionDao.buscar(new Configuracion(ServidorCorreoEnum.USUARIO.getTipo())).get(0).getValor()));
        mailDTO = new MailDTO(smtp, puerto, usuario, clave, null, null, null, null);

    }

    private void fijarConfiguraciones() {
        configuracionGeneralDTO = new ConfiguracionGeneralDTO();
        configuracionGeneralDTO.setTiempoMaximoPertinencia(configuracionDao.buscar(
                new Configuracion(ConfiguracionEnum.SECRETKEY.getTipo())).get(0).getValor());
        configuracionGeneralDTO.setSecureKey(null);
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

}

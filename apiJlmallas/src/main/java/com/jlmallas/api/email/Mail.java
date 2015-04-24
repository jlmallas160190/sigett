/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.api.email;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author JorgeLuis
 */
public class Mail {

    public void enviarNotificacionCorreo(String smtp, String puerto, String usrCorreo, String claveCorreo, String mensajeTexto, String mensajeArchivo, String correoDestino, String destinario) {
        Properties props = new Properties();
        // Nombre del host de correo, es smtp.gmail.com  
        props.setProperty("mail.smtp.host", smtp);
        // TLS si está disponible  
        props.setProperty("mail.smtp.starttls.enable", "true");
        // Puerto de gmail para envio de correos  
        props.setProperty("mail.smtp.port", puerto);
        // Nombre del usuario  
        props.setProperty("mail.smtp.user", usrCorreo);
        // Si requiere o no usuario y password para conectarse.  
        props.setProperty("mail.smtp.auth", "true");
        // Se inicia una nueva sesion  
        Session session = Session.getDefaultInstance(props);
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            BodyPart texto = new MimeBodyPart();
            BodyPart archivo = new MimeBodyPart();
            MimeMultipart correo = new MimeMultipart();
            correo.addBodyPart(texto);
            correo.addBodyPart(archivo);
            texto.setText(mensajeTexto);
            archivo.setText(mensajeArchivo);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDestino));
            mimeMessage.setSubject("Hola " + destinario + " Tienes un mensaje");
            mimeMessage.setContent(correo);
            Transport t = session.getTransport("smtp");
            t.connect(usrCorreo, claveCorreo);
            t.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            t.close();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}

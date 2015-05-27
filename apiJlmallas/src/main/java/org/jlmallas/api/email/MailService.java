/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.api.email;

/**
 *
 * @author jorge-luis
 */
public interface MailService {

    void enviar(final MailDTO mailDTO);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.secure;

/**
 *
 * @author jorge-luis
 */
public interface SecureService {

    String encrypt(final Secure secureDTO);

    String decrypt(final Secure secureDTO);
}

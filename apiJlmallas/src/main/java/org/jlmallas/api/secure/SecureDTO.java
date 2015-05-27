/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlmallas.api.secure;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class SecureDTO implements Serializable{
    private String key;
    private String password;

    public SecureDTO() {
    }

    public SecureDTO(String key, String password) {
        this.key = key;
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}

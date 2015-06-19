/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlmallas.secure;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class Secure implements Serializable{
    private String key;
    private String password;

    public Secure() {
    }

    public Secure(String key, String password) {
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

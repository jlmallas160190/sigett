/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import edu.unl.sigett.entity.ConfiguracionCarrera;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionConfiguracionCarrera implements Serializable {

    private ConfiguracionCarrera configuracionCarrera;

    public SessionConfiguracionCarrera() {
        this.configuracionCarrera = new ConfiguracionCarrera();
    }

    public ConfiguracionCarrera getConfiguracionCarrera() {
        return configuracionCarrera;
    }

    public void setConfiguracionCarrera(ConfiguracionCarrera configuracionCarrera) {
        this.configuracionCarrera = configuracionCarrera;
    }

}

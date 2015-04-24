/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import edu.unl.sigett.entity.ConfiguracionGeneral;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionConfiguracion implements Serializable {

    private ConfiguracionGeneral configuracionGeneral;

    public SessionConfiguracion() {
        this.configuracionGeneral = new ConfiguracionGeneral();
    }

    public ConfiguracionGeneral getConfiguracionGeneral() {
        return configuracionGeneral;
    }

    public void setConfiguracionGeneral(ConfiguracionGeneral configuracionGeneral) {
        this.configuracionGeneral = configuracionGeneral;
    }

}

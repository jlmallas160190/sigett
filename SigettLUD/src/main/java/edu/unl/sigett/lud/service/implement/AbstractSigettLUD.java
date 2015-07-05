/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.lud.service.implement;

import edu.unl.sigett.lud.util.CabeceraWebSemantica;

/**
 *
 * @author jorge-luis
 */
public class AbstractSigettLUD {
    private CabeceraWebSemantica cabecera;

    public CabeceraWebSemantica getCabecera() {
        return cabecera;
    }

    public void setCabecera(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }
    
}

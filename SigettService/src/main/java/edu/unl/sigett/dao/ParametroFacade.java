/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Parametro;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ParametroFacade extends AbstractDao<Parametro> implements ParametroFacadeLocal {
 
    public ParametroFacade() {
        super(Parametro.class);
    }
    
}

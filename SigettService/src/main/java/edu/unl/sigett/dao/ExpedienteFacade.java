/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Expediente;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class ExpedienteFacade extends AbstractDao<Expediente> implements ExpedienteFacadeLocal {

    public ExpedienteFacade() {
        super(Expediente.class);
    }
    
}

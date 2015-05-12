/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EstadoDirector;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstadoDirectorFacade extends AbstractDao<EstadoDirector> implements EstadoDirectorFacadeLocal {

    public EstadoDirectorFacade() {
        super(EstadoDirector.class);
    }

}

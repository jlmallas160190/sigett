/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Cronograma;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CronogramaFacade extends AbstractDao<Cronograma> implements CronogramaFacadeLocal {

    public CronogramaFacade() {
        super(Cronograma.class);
    }

}

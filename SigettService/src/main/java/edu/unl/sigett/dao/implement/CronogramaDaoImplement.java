/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.CronogramaDao;
import edu.unl.sigett.entity.Cronograma;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CronogramaDaoImplement extends AbstractDao<Cronograma> implements CronogramaDao {

    public CronogramaDaoImplement() {
        super(Cronograma.class);
    }

}

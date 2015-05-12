/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Cronograma;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CronogramaFacadeLocal {

    void create(Cronograma cronograma);

    void edit(Cronograma cronograma);

    void remove(Cronograma cronograma);

    Cronograma find(Object id);

    List<Cronograma> findAll();

    List<Cronograma> findRange(int[] range);

    int count();
    
}

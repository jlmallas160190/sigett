/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Tribunal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface TribunalDao {

    void create(Tribunal tribunal);

    void edit(Tribunal tribunal);

    void remove(Tribunal tribunal);

    Tribunal find(Object id);

    List<Tribunal> findAll();

    List<Tribunal> findRange(int[] range);

    List<Tribunal> buscar(final Tribunal tribunal);

    int count();

}

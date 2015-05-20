/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Aspirante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface AspiranteDao {

    void create(Aspirante aspirante);

    void edit(Aspirante aspirante);

    void remove(Aspirante aspirante);

    Aspirante find(Object id);

    List<Aspirante> findAll();

    List<Aspirante> findRange(int[] range);

    List<Aspirante> buscar(Aspirante aspirante);

    int count();

}

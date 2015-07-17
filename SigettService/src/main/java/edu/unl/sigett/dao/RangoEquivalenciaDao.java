/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.RangoEquivalencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface RangoEquivalenciaDao {

    void create(RangoEquivalencia rangoEquivalencia);

    void edit(RangoEquivalencia rangoEquivalencia);

    void remove(RangoEquivalencia rangoEquivalencia);

    RangoEquivalencia find(Object id);

    List<RangoEquivalencia> findAll();

    List<RangoEquivalencia> findRange(int[] range);

    List<RangoEquivalencia> buscar(final RangoEquivalencia rangoEquivalencia);

    int count();

}

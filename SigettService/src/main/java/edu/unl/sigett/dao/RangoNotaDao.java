/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.RangoNota;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface RangoNotaDao {

    void create(RangoNota rangoNota);

    void edit(RangoNota rangoNota);

    void remove(RangoNota rangoNota);

    RangoNota find(Object id);

    List<RangoNota> findAll();

    List<RangoNota> findRange(int[] range);

    List<RangoNota> buscar(final RangoNota rangoNota);

    int count();

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.RangoNota;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface RangoNotaFacadeLocal {

    void create(RangoNota rangoNota);

    void edit(RangoNota rangoNota);

    void remove(RangoNota rangoNota);

    RangoNota find(Object id);

    List<RangoNota> findAll();

    List<RangoNota> findRange(int[] range);

    int count();

}

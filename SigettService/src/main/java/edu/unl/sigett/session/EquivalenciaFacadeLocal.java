/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.session;

import edu.unl.sigett.entity.Equivalencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EquivalenciaFacadeLocal {

    void create(Equivalencia equivalencia);

    void edit(Equivalencia equivalencia);

    void remove(Equivalencia equivalencia);

    Equivalencia find(Object id);

    List<Equivalencia> findAll();

    List<Equivalencia> findRange(int[] range);

    int count();
    
}

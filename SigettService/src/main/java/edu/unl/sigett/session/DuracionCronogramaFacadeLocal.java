/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.DuracionCronograma;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DuracionCronogramaFacadeLocal {

    void create(DuracionCronograma duracionCronograma);

    void edit(DuracionCronograma duracionCronograma);

    void remove(DuracionCronograma duracionCronograma);

    DuracionCronograma find(Object id);

    List<DuracionCronograma> findAll();

    List<DuracionCronograma> findRange(int[] range);

    int count();

}

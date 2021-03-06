/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EvaluacionTribunal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EvaluacionTribunalDao {

    void create(EvaluacionTribunal evaluacionTribunal);

    void edit(EvaluacionTribunal evaluacionTribunal);

    void remove(EvaluacionTribunal evaluacionTribunal);

    EvaluacionTribunal find(Object id);

    List<EvaluacionTribunal> findAll();

    List<EvaluacionTribunal> findRange(int[] range);

    List<EvaluacionTribunal> buscar(final EvaluacionTribunal evaluacionTribunal);

    int count();

}

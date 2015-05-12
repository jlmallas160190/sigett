/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.PlazoEvaluacionTribunal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface PlazoEvaluacionTribunalFacadeLocal {

    void create(PlazoEvaluacionTribunal plazoEvaluacionTribunal);

    void edit(PlazoEvaluacionTribunal plazoEvaluacionTribunal);

    void remove(PlazoEvaluacionTribunal plazoEvaluacionTribunal);

    PlazoEvaluacionTribunal find(Object id);

    List<PlazoEvaluacionTribunal> findAll();

    List<PlazoEvaluacionTribunal> findRange(int[] range);

    int count();

}

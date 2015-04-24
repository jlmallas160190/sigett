/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Acta;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ActaFacadeLocal {

    void create(Acta acta);

    void edit(Acta acta);

    void remove(Acta acta);

    Acta find(Object id);

    List<Acta> findAll();

    List<Acta> findRange(int[] range);

    List<Acta> buscarPorEvaluacionTribunal(Long evaluacionTribunalId);

    Acta buscarPorEvaluacionCategoria(Long evaluacionId, Integer categoria);

    int count();

}

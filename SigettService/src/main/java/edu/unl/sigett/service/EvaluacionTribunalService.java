/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.EvaluacionTribunal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EvaluacionTribunalService {

    void guardar(final EvaluacionTribunal evaluacionTribunal);

    void actualizar(final EvaluacionTribunal evaluacionTribunal);

    void eliminar(final EvaluacionTribunal evaluacionTribunal);

    EvaluacionTribunal buscarPorId(final EvaluacionTribunal evaluacionTribunal);

    List<EvaluacionTribunal> buscar(final EvaluacionTribunal evaluacionTribunal);

    List<EvaluacionTribunal> buscarPorCarrera(Integer carreraId);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Pertinencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface PertinenciaService {

    void guardar(final Pertinencia pertinencia);

    void actualizar(final Pertinencia pertinencia);

    void eliminar(final Pertinencia pertinencia);

    Pertinencia buscarPorId(final Pertinencia pertinencia);

    List<Pertinencia> buscar(final Pertinencia pertinencia);
}

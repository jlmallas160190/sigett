/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface PeriodoCoordinacionService {

    void guardar(final PeriodoCoordinacion periodoCoordinacion);

    void actualizar(final PeriodoCoordinacion periodoCoordinacion);

    void eliminar(final PeriodoCoordinacion periodoCoordinacion);

    PeriodoCoordinacion buscarPorId(final PeriodoCoordinacion periodoCoordinacion);

    List<PeriodoCoordinacion> buscar(final PeriodoCoordinacion periodoCoordinacion);
}

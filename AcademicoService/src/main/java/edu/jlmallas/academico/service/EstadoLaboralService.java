/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.EstadoLaboral;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EstadoLaboralService {

    EstadoLaboral buscarPorTipoContrato(final EstadoLaboral estadoLaboral);

    void guardar(final EstadoLaboral estadoLaboral);

    void actualizar(final EstadoLaboral estadoLaboral);

    EstadoLaboral buscarPorId(final Long Long);

    List<EstadoLaboral> buscar(final EstadoLaboral estadoLaboral);

}

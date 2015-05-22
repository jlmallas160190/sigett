/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CoordinadorPeriodoService {

    void guardar(final CoordinadorPeriodo coordinadorPeriodo);

    void editar(final CoordinadorPeriodo coordinadorPeriodo);

    void eliminar(final CoordinadorPeriodo coordinadorPeriodo);

    CoordinadorPeriodo buscarPorId(final CoordinadorPeriodo coordinadorPeriodo);

    CoordinadorPeriodo vigente(final CoordinadorPeriodo coordinadorPeriodo);

    List<CoordinadorPeriodo> buscar(final CoordinadorPeriodo coordinadorPeriodo);

}

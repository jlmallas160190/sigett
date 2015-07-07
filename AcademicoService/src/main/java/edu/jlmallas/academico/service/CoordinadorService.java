/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.Coordinador;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CoordinadorService {

    void guardar(final Coordinador coordinador);

    void actualizar(final Coordinador coordinador);

    void eliminar(final Coordinador coordinador);

    Coordinador buscarPorId(final Coordinador coordinador);

    List<Coordinador> buscar(final Coordinador coordinador);

}

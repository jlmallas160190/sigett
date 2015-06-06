/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.DocenteCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocenteCarreraService {

    void guardar(final DocenteCarrera docenteCarrera);

    void actualizar(final DocenteCarrera docenteCarrera);

    void eliminar(final DocenteCarrera docenteCarrera);

    DocenteCarrera buscarPorId(final DocenteCarrera docenteCarrera);

    List<DocenteCarrera> buscar(final DocenteCarrera docenteCarrera);
}

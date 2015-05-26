/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.DocenteProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocenteProyectoService {

    void guardar(final DocenteProyecto docenteProyecto);

    void actualizar(final DocenteProyecto docenteProyecto);

    void eliminar(final DocenteProyecto docenteProyecto);

    DocenteProyecto buscarPorId(final DocenteProyecto docenteProyecto);

    List<DocenteProyecto> buscar(final DocenteProyecto docenteProyecto);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.CalificacionParametro;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CalificacionParametroService {

    void guardar(final CalificacionParametro calificacionParametro);

    void actualizar(final CalificacionParametro calificacionParametro);

    void eliminar(final CalificacionParametro calificacionParametro);

    CalificacionParametro buscarPorId(final CalificacionParametro calificacionParametro);

    List<CalificacionParametro> buscar(final CalificacionParametro calificacionParametro);
}

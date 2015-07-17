package edu.unl.sigett.service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.unl.sigett.entity.CalificacionMiembro;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CalificacionMiembroTribunalService {

    void guardar(final CalificacionMiembro calificacionMiembro);

    void actualizar(final CalificacionMiembro calificacionMiembro);

    void eliminar(final CalificacionMiembro calificacionMiembro);

    CalificacionMiembro buscarPorId(final CalificacionMiembro calificacionMiembro);

    List<CalificacionMiembro> buscar(final CalificacionMiembro calificacionMiembro);
}

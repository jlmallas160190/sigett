/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Actividad;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ActividadService {

    void guardar(final Actividad actividad);

    void actualizar(final Actividad actividad);

    void eliminar(final Actividad actividad);

    Actividad buscarPorId(final Actividad actividad);

    List<Actividad> buscar(final Actividad actividad);

    BigDecimal sumatoriaDuracion(final Actividad actividad);
}

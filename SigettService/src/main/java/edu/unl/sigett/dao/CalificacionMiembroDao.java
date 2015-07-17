/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CalificacionMiembro;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CalificacionMiembroDao {

    void create(CalificacionMiembro calificacionMiembro);

    void edit(CalificacionMiembro calificacionMiembro);

    void remove(CalificacionMiembro calificacionMiembro);

    CalificacionMiembro find(Object id);

    List<CalificacionMiembro> buscar(final CalificacionMiembro calificacionMiembro);
}

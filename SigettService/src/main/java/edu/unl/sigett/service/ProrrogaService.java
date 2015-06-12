/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Prorroga;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ProrrogaService {

    void guardar(final Prorroga prorroga);

    void actualizar(final Prorroga prorroga);

    void eliminar(final Prorroga prorroga);

    Prorroga buscarPorId(final Prorroga prorroga);

    List<Prorroga> buscar(final Prorroga prorroga);

    Prorroga obtenerPorFechaMayor(final Prorroga prorroga);
}

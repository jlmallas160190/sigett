/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Proyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ProyectoService {

    void guardar(final Proyecto proyecto);

    void actualizar(final Proyecto proyecto);

    void eliminar(final Proyecto proyecto);

    Proyecto buscarPorId(final Proyecto proyecto);

    List<Proyecto> buscar(final Proyecto proyecto);

}

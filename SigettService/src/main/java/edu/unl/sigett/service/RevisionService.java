/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Revision;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface RevisionService {

    void guardar(final Revision revision);

    void actualizar(final Revision revision);

    void eliminar(final Revision revision);

    Revision buscarPorId(final Revision revision);

    List<Revision> buscar(final Revision revision);

}

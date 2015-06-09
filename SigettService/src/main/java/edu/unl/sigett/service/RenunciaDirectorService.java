/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.RenunciaDirector;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface RenunciaDirectorService {

    void guardar(final RenunciaDirector renunciaDirector);

    void actualizar(final RenunciaDirector renunciaDirector);

    void eliminar(final RenunciaDirector renunciaDirector);

    RenunciaDirector buscarPorId(final RenunciaDirector renunciaDirector);

    List<RenunciaDirector> buscar(final RenunciaDirector renunciaDirector);

}

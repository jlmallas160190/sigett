/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.MiembroTribunal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface MiembroTribunalService {

    void guardar(final MiembroTribunal miembroTribunal);

    void actualizar(final MiembroTribunal miembroTribunal);

    void eliminar(final MiembroTribunal miembroTribunal);

    MiembroTribunal buscarPorId(final MiembroTribunal miembroTribunal);

    List<MiembroTribunal> buscar(final MiembroTribunal miembroTribunal);
}

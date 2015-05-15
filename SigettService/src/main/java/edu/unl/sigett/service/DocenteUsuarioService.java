/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.service;

import edu.unl.sigett.entity.DocenteUsuario;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocenteUsuarioService {

    DocenteUsuario buscarPorDocente(final DocenteUsuario docenteUsuario);

    void guardar(final DocenteUsuario docenteUsuario);

    void actualizar(final DocenteUsuario docenteUsuario);

    DocenteUsuario buscarPorId(final Long docenteUsuarioId);
    
}

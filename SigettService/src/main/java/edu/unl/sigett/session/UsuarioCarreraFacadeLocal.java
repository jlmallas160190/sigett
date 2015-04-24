/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.UsuarioCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface UsuarioCarreraFacadeLocal {

    void create(UsuarioCarrera usuarioCarrera);

    void edit(UsuarioCarrera usuarioCarrera);

    void remove(UsuarioCarrera usuarioCarrera);

    UsuarioCarrera find(Object id);

    List<UsuarioCarrera> findAll();

    List<UsuarioCarrera> findRange(int[] range);

    List<UsuarioCarrera> buscarPorUsuario(Long usuarioId);

    int count();

}

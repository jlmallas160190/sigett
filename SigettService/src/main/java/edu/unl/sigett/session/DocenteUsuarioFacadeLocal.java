/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.DocenteUsuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocenteUsuarioFacadeLocal {

    void create(DocenteUsuario docenteUsuario);

    void edit(DocenteUsuario docenteUsuario);

    void remove(DocenteUsuario docenteUsuario);

    DocenteUsuario find(Object id);

    List<DocenteUsuario> findAll();

    List<DocenteUsuario> findRange(int[] range);

    DocenteUsuario buscarPorDocente(Long id);

    int count();

}

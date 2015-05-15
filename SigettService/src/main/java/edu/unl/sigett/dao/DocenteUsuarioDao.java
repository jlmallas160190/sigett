/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DocenteUsuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocenteUsuarioDao {

    void create(DocenteUsuario docenteUsuario);

    void edit(DocenteUsuario docenteUsuario);

    void remove(DocenteUsuario docenteUsuario);

    DocenteUsuario find(Object id);

    List<DocenteUsuario> findAll();

    List<DocenteUsuario> findRange(int[] range);

//    DocenteUsuario buscarPorDocente(Long id);

    List<DocenteUsuario> buscar(DocenteUsuario docenteUsuario);

    int count();

}

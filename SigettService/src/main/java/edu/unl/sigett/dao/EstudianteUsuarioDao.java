/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EstudianteUsuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstudianteUsuarioDao {

    void create(EstudianteUsuario estudianteUsuario);

    void edit(EstudianteUsuario estudianteUsuario);

    void remove(EstudianteUsuario estudianteUsuario);

    EstudianteUsuario find(Object id);

    List<EstudianteUsuario> findAll();

    List<EstudianteUsuario> findRange(int[] range);

    List<EstudianteUsuario> buscar(EstudianteUsuario estudianteUsuario);

    int count();

}

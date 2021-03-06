/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DirectorProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DirectorProyectoDao {

    void create(DirectorProyecto directorProyecto);

    void edit(DirectorProyecto directorProyecto);

    void remove(DirectorProyecto directorProyecto);

    DirectorProyecto find(Object id);

    List<DirectorProyecto> findAll();

    List<DirectorProyecto> findRange(int[] range);

    List<DirectorProyecto> buscar(final DirectorProyecto directorProyecto);

    int count();

}

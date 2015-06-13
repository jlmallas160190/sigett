/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.AutorProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface AutorProyectoDao {

    void create(AutorProyecto autorProyecto);

    void edit(AutorProyecto autorProyecto);

    void remove(AutorProyecto autorProyecto);

    AutorProyecto find(Object id);

    List<AutorProyecto> findAll();

    List<AutorProyecto> findRange(int[] range);

    List<AutorProyecto> buscar(AutorProyecto autorProyecto);

    int count();

}

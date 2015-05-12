/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Director;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DirectorFacadeLocal {

    void create(Director director);

    void edit(Director director);

    void remove(Director director);

    Director find(Object id);

    List<Director> findAll();

    List<Director> findRange(int[] range);

    List<Director> buscarAptos(Integer carreraId);

    int count();

}

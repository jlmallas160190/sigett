/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.RenunciaDirector;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface RenunciaDirectorDao {

    void create(RenunciaDirector renunciaDirector);

    void edit(RenunciaDirector renunciaDirector);

    void remove(RenunciaDirector renunciaDirector);

    RenunciaDirector find(Object id);

    List<RenunciaDirector> findAll();

    List<RenunciaDirector> findRange(int[] range);

    List<RenunciaDirector> buscar(final RenunciaDirector renunciaDirector);

    int count();

}

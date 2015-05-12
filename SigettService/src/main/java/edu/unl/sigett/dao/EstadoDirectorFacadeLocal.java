/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EstadoDirector;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstadoDirectorFacadeLocal {

    void create(EstadoDirector estadoDirector);

    void edit(EstadoDirector estadoDirector);

    void remove(EstadoDirector estadoDirector);

    EstadoDirector find(Object id);

    List<EstadoDirector> findAll();

    List<EstadoDirector> findRange(int[] range);

    int count();

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface LineaInvestigacionCarreraDao {

    void create(LineaInvestigacionCarrera lineaInvestigacionCarrera);

    void edit(LineaInvestigacionCarrera lineaInvestigacionCarrera);

    void remove(LineaInvestigacionCarrera lineaInvestigacionCarrera);

    LineaInvestigacionCarrera find(Object id);

    List<LineaInvestigacionCarrera> findAll();

    List<LineaInvestigacionCarrera> findRange(int[] range);

    List<LineaInvestigacionCarrera> buscar(LineaInvestigacionCarrera lineaInvestigacionCarrera);

    int count();

}

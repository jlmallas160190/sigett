/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.LineaInvestigacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface LineaInvestigacionFacadeLocal {

    void create(LineaInvestigacion lineaInvestigacion);

    void edit(LineaInvestigacion lineaInvestigacion);

    void remove(LineaInvestigacion lineaInvestigacion);

    LineaInvestigacion find(Object id);

    List<LineaInvestigacion> findAll();

    List<LineaInvestigacion> findRange(int[] range);

    List<LineaInvestigacion> buscarPorCriterio(String nombre);

    int count();

}

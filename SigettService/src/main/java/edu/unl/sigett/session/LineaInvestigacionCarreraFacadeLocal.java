/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface LineaInvestigacionCarreraFacadeLocal {

    void create(LineaInvestigacionCarrera lineaInvestigacionCarrera);

    void edit(LineaInvestigacionCarrera lineaInvestigacionCarrera);

    void remove(LineaInvestigacionCarrera lineaInvestigacionCarrera);

    LineaInvestigacionCarrera find(Object id);

    List<LineaInvestigacionCarrera> findAll();

    List<LineaInvestigacionCarrera> buscarPorCarrera(Integer carreraId);

    List<LineaInvestigacionCarrera> findRange(int[] range);

    List<LineaInvestigacionCarrera> buscarPorLineaInvestigacion(Long lineaId);

    int count();

}

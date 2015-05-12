/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface LineaInvestigacionProyectoFacadeLocal {

    void create(LineaInvestigacionProyecto lineaInvestigacionProyecto);

    void edit(LineaInvestigacionProyecto lineaInvestigacionProyecto);

    void remove(LineaInvestigacionProyecto lineaInvestigacionProyecto);

    LineaInvestigacionProyecto find(Object id);

    List<LineaInvestigacionProyecto> findAll();

    List<LineaInvestigacionProyecto> findRange(int[] range);

    List<LineaInvestigacionProyecto> buscarPorProyecto(Long proyectoId);

    List<LineaInvestigacionProyecto> buscarPorLi(Long lineaInvestigacionId);

    int count();

}

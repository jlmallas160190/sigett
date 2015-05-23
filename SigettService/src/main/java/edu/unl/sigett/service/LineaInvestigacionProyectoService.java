/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface LineaInvestigacionProyectoService {

    void guardar(final LineaInvestigacionProyecto lineaInvestigacionProyecto);

    void actulizar(final LineaInvestigacionProyecto lineaInvestigacionProyecto);

    void eliminar(final LineaInvestigacionProyecto lineaInvestigacionProyecto);

    LineaInvestigacionProyecto buscarPorId(final LineaInvestigacionProyecto lineaInvestigacionProyecto);

    List<LineaInvestigacionProyecto> buscar(final LineaInvestigacionProyecto lineaInvestigacionProyecto);

    List<LineaInvestigacion> buscarLineaInvestigacion(final LineaInvestigacionProyecto lineaInvestigacionProyecto);

    Integer count(final LineaInvestigacionProyecto lineaInvestigacionProyecto);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface LineaInvestigacionService {

    List<LineaInvestigacion> buscarPorCarrera(final LineaInvestigacionCarrera lineaInvestigacionCarrera);

    List<LineaInvestigacion> buscarPorDocente(final LineaInvestigacionDocente lineaInvestigacionDocente);

    List<LineaInvestigacion> buscarDiferenciaDocenteCarrera(final LineaInvestigacionCarrera lineaInvestigacionCarrera,
            final LineaInvestigacionDocente lineaInvestigacionDocente);

}
